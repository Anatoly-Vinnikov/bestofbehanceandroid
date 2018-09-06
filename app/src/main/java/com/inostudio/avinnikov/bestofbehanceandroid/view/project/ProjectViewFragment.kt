package com.inostudio.avinnikov.bestofbehanceandroid.view.project

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.databinding.FragmentProjectViewBinding
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.CombineModule
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.Module
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments.Comment
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project.ProjectBinding
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project.ProjectVM
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project.ProjectVMFactory
import kotlinx.android.synthetic.main.fragment_project_view.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.doAsync


class ProjectViewFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    var menu: Menu? = null
    private var combineAdapter: CombineAdapter? = null
    private var combList = mutableListOf<CombineModule>()
    private var modulesList = mutableListOf<Module>()
    private var commentsList = mutableListOf<Comment>()

    private var fragmentBinding: FragmentProjectViewBinding? = null
    var binding = ProjectBinding()

    var previousTotal = 0
    var loading = true
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var myVM: ProjectVM? = null
    private val network = Networking.instance
    var isScrolled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        combineAdapter = CombineAdapter(mutableListOf())
        val factory = ProjectVMFactory(arguments?.getString(getString(R.string.my_project_id))!!)
        myVM = ViewModelProviders.of(this, factory).get(ProjectVM::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.project_menu, menu)
        if (myVM?.checkIfInDB()!!) {
            menu.getItem(1).icon = ContextCompat.getDrawable(activity!!, R.drawable.ic_bookmark_border_menu_active)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.shareProject -> {
            myVM?.shareProject(activity!! as AppCompatActivity)
            true
        }
        R.id.bookmark -> {
            if (myVM?.checkIfInDB()!!) {
                menu?.getItem(1)?.icon = ContextCompat.getDrawable(activity!!, R.drawable.ic_bookmark_border_menu_inactive)
                myVM?.removeFromDB()
            } else {
                menu?.getItem(1)?.icon = ContextCompat.getDrawable(activity!!, R.drawable.ic_bookmark_border_menu_active)
                myVM?.addToDB()
            }
            true
        }
        android.R.id.home -> {
            Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigateUp()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_project_view, container, false)
        return fragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.project_title)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentBinding?.project = binding

        combineList.layoutManager = LinearLayoutManager(context)
        combineList.isSaveEnabled = true

        profileImage.setOnClickListener {
            val bundle = Bundle().also { it.putString(getString(R.string.user_id), myVM?.project?.project?.owners?.get(0)?.id.toString()) }
            Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.action_projectViewFragment_to_userFragment, bundle)
        }

        myVM?.getProject()?.observe(this, Observer<ProjectBinding> { project ->
            fragmentBinding?.project = project
            combineAdapter?.setCommentCount(project?.comments!!)
        })

        myVM?.getModules()?.observe(this, Observer<MutableList<Module>> { list ->
            modulesList = list!!
            combineAdapter?.setColor(myVM?.project?.project?.styles?.background?.color!!)
            generateList()
        })

        myVM?.getComments()?.observe(this, Observer<MutableList<Comment>> { list ->
            commentsList = list!!
            generateList()
        })
    }

    fun generateList() {
        combList.clear()
        modulesList.forEach { combList.add(CombineModule(0, it, null)) }
        combList.add(CombineModule(2, null, null))
        commentsList.forEach { combList.add(CombineModule(1, null, it)) }
        val oldSize = combineAdapter?.itemCount
        combineAdapter?.setModules(combList)
        if (isScrolled) {
            isScrolled = false
            combineAdapter?.notifyItemRangeInserted(0, modulesList.size)
        } else {
            combineAdapter?.notifyItemRangeInserted(oldSize!!, combList.size)
        }
    }

    fun setupUI() {
        if (!network.isNetworkAvailable(activity!!)) {
            longSnackbar(view!!, getString(R.string.network_connection)).view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }

        if (combineList.adapter == null) {
            combineList.adapter = combineAdapter
        }

        combineList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolled) {
                    generateList()
                }
                visibleItemCount = combineList.layoutManager?.childCount!!
                totalItemCount = combineList.layoutManager?.itemCount!!
                firstVisibleItem = (combineList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0)) {
                    doAsync {
                        myVM?.loadMoreComments()
                    }
                    loading = true
                }
            }
        })

        commentsCount1.setOnClickListener {
            isScrolled = true
            combList.clear()
            combList.add(CombineModule(2, null, null))
            commentsList.forEach { combList.add(CombineModule(1, null, it)) }
            combineAdapter?.setModules(combList)
            combineAdapter?.notifyItemRangeRemoved(0, modulesList.size)
        }
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + getString(R.string.must_implement))
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
