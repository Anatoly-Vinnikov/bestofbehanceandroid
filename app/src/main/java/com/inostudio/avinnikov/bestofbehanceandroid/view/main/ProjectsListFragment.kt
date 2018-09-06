package com.inostudio.avinnikov.bestofbehanceandroid.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.ImageBindingAdapter
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.main.ProjectListVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects_list.*
import org.jetbrains.anko.design.longSnackbar


class ProjectsListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    var previousTotal = 0
    var loading = true
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    private var adapter: ProjectsListAdapter? = null

    var menu: Menu? = null
    var myVM: ProjectListVM? = null
    private val network = Networking.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = ProjectsListAdapter(mutableListOf())
        myVM = ViewModelProviders.of(this).get(ProjectListVM::class.java)
        myVM?.init()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_main -> {
                Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.projectsListFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookmarked -> {
                Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.bookmarkedProjectsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_people -> {
                Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(R.id.bookmarkedPeopleFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu, menu)
        if (ImageBindingAdapter.isGrid) {
            menu.getItem(0).icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_view_agenda_24dp)
        } else {
            menu.getItem(0).icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_view_column_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.changeMode -> {
            if (ImageBindingAdapter.isGrid) {
                menu?.getItem(0)?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_view_column_24dp)
                ImageBindingAdapter.isGrid = false
                postsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
            } else {
                menu?.getItem(0)?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_view_agenda_24dp)
                ImageBindingAdapter.isGrid = true
                postsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_projects_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        if (ImageBindingAdapter.isGrid) {
            postsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        } else {
            postsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
        postsList.isSaveEnabled = true
        myVM?.getProjects()?.observe(this, Observer<MutableList<Project>> { list ->
            adapter?.setProjectsList(list!!)
        })
    }

    fun setupUI() {
        activity?.navigation?.menu?.findItem(R.id.navigation_main)?.isChecked = true

        if (!network.isNetworkAvailable(activity!!)) {
            longSnackbar(view!!, getString(R.string.network_connection)).view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }

        if (postsList.adapter == null) {
            postsList.adapter = adapter
        }

        postsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = postsList.layoutManager?.childCount!!
                totalItemCount = postsList.layoutManager?.itemCount!!
                firstVisibleItem = (postsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0)) {
                    myVM?.loadMoreProjects()
                    loading = true
                }
            }
        })
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
