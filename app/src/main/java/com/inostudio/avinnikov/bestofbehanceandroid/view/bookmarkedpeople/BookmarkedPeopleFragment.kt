package com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedpeople

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bookmared_users_list.*
import org.jetbrains.anko.design.longSnackbar


class BookmarkedPeopleFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var adapter: BookmarkedPeopleAdapter? = null
    private val db = Database.INSTANCE
    private val network = Networking.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookmarkedPeopleAdapter(db.peopleDB)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookmared_users_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.people_title)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        peopleList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        peopleList.isSaveEnabled = true
    }

    fun setupUI() {
        activity?.navigation?.menu?.findItem(R.id.navigation_people)?.isChecked = true

        if (!network.isNetworkAvailable(activity!!)) {
            longSnackbar(view!!, getString(R.string.network_connection)).view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }

        if (peopleList.adapter == null) {
            peopleList.adapter = adapter
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
