package com.inostudio.avinnikov.bestofbehanceandroid.view.main

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.BuildConfig
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedpeople.BookmarkedPeopleFragment
import com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedprojects.BookmarkedProjectsFragment
import com.inostudio.avinnikov.bestofbehanceandroid.view.project.ProjectViewFragment
import com.inostudio.avinnikov.bestofbehanceandroid.view.user.UserFragment
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber


class MainActivity : AppCompatActivity(), ProjectsListFragment.OnFragmentInteractionListener,
        ProjectViewFragment.OnFragmentInteractionListener, BookmarkedProjectsFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener, BookmarkedPeopleFragment.OnFragmentInteractionListener {

    private val db = Database.INSTANCE

    override fun onFragmentInteraction(uri: Uri) {
        Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        db.load(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            LeakCanary.install(application)
            /*StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())*/
        }
    }

    /*override fun onBackPressed() {
        if (!Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()) {
            super.onBackPressed()
        }
    }*/
}
