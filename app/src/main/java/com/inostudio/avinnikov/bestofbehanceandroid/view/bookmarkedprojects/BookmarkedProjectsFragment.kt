package com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedprojects

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.ImageBindingAdapter
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bookmared_projects_list.*
import org.jetbrains.anko.design.longSnackbar


class BookmarkedProjectsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var adapter: BookmarkedProjectsAdapter? = null

    var menu: Menu? = null
    private val db = Database.INSTANCE
    private val network = Networking.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = BookmarkedProjectsAdapter(db.projectsDB)
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
                bookmarkedPostsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
            } else {
                menu?.getItem(0)?.icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_view_agenda_24dp)
                ImageBindingAdapter.isGrid = true
                bookmarkedPostsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookmared_projects_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.projects_title)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        if (ImageBindingAdapter.isGrid) {
            bookmarkedPostsList.layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        } else {
            bookmarkedPostsList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        }
        bookmarkedPostsList.isSaveEnabled = true
    }

    fun setupUI() {
        activity?.navigation?.menu?.findItem(R.id.navigation_bookmarked)?.isChecked = true

        if (!network.isNetworkAvailable(activity!!)) {
            longSnackbar(view!!, getString(R.string.network_connection)).view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }

        if (bookmarkedPostsList.adapter == null) {
            bookmarkedPostsList.adapter = adapter
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
