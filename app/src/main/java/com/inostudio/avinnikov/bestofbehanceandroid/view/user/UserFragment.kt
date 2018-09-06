package com.inostudio.avinnikov.bestofbehanceandroid.view.user

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.databinding.FragmentUserBinding
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.ImageBindingAdapter
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user.UserBinding
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user.UserVM
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user.UserVMFactory
import kotlinx.android.synthetic.main.fragment_user.*
import org.jetbrains.anko.design.longSnackbar


class UserFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    var previousTotal = 0
    var loading = true
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var menu: Menu? = null
    private var userAdapter: UserAdapter? = null

    private var fragmentBinding: FragmentUserBinding? = null
    var binding = UserBinding()
    var myVM: UserVM? = null
    private val network = Networking.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        userAdapter = UserAdapter(mutableListOf())
        val factory = UserVMFactory(arguments?.getString(getString(R.string.user_id))!!)
        myVM = ViewModelProviders.of(this, factory).get(UserVM::class.java)
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
            myVM?.shareUser(activity!! as AppCompatActivity)
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
                inflater, R.layout.fragment_user, container, false)
        return fragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.profile_title)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (ImageBindingAdapter.isGrid) {
            changeMode.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_view_agenda_blue))
            projectsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        } else {
            changeMode.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_view_column_blue))
            projectsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
        projectsList.isSaveEnabled = true
        fragmentBinding?.user = binding

        myVM?.getUser()?.observe(this, Observer<UserBinding> { user ->
            fragmentBinding?.user = user
        })

        myVM?.getProjects()?.observe(this, Observer<MutableList<Project>> { list ->
            userAdapter?.setProjectsList(list!!)
        })
    }

    fun setupUI() {
        if (!network.isNetworkAvailable(activity!!)) {
            longSnackbar(view!!, getString(R.string.network_connection)).view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }

        if (projectsList.adapter == null) {
            projectsList.adapter = userAdapter
        }

        projectsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = projectsList.layoutManager!!.childCount
                totalItemCount = projectsList.layoutManager!!.itemCount
                firstVisibleItem = (projectsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0)) {
                    myVM?.loadMoreProjects()
                    loading = true
                }
            }
        })

        changeMode.setOnClickListener {
            if (ImageBindingAdapter.isGrid) {
                changeMode.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_view_column_blue))
                ImageBindingAdapter.isGrid = false
                projectsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
            } else {
                changeMode.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_view_agenda_blue))
                ImageBindingAdapter.isGrid = true
                projectsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            }
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
