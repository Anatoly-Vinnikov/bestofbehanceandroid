package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.model.user.User
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class UserVM(val userID: String = "") : ViewModel() {
    private val db = Database.INSTANCE
    private val network = Networking.instance
    var user: User? = null
    private var listProjects = mutableListOf<Project>()
    var projects = MutableLiveData<MutableList<Project>>()
    private var pageNumber = 1
    private var viewModel = MutableLiveData<UserBinding>()
    private var allLoaded = false

    init {
        doAsync {
            user = network.getUser(userID).user
            uiThread {
                user?.let {
                    val images = user?.images
                    var curProfileImage = ""
                    images?.let {
                        when {
                            it.image276 != "" -> curProfileImage = it.image276
                            it.image130 != "" -> curProfileImage = it.image130
                            it.image138 != "" -> curProfileImage = it.image138
                            it.image115 != "" -> curProfileImage = it.image115
                            it.image100 != "" -> curProfileImage = it.image100
                            it.image50 != "" -> curProfileImage = it.image50
                        }
                    }
                    val aboutTmp: String
                    if (user?.sections?.isEmpty()!!) {
                        aboutTmp = ""
                    } else {
                        aboutTmp = user?.sections?.values?.first()!!
                    }
                    var pinterestURL = ""
                    var instagramURL = ""
                    var facebookURL = ""
                    var dribbbleURL = ""
                    var twitterURL = ""
                    user?.socialLinks?.forEach {
                        when (it.serviceName) {
                            "Pinterest" -> pinterestURL = it.url
                            "Instagram" -> instagramURL = it.url
                            "Facebook" -> facebookURL = it.url
                            "Dribbble" -> dribbbleURL = it.url
                            "Twitter" -> twitterURL = it.url
                        }
                    }
                    viewModel.postValue(UserBinding(user?.stats?.appreciations.toString(),
                            user?.stats?.views.toString(), user?.stats?.followers.toString(),
                            user?.stats?.following.toString(), user?.displayName!!, user?.occupation!!,
                            user?.location!!, aboutTmp, curProfileImage, pinterestURL, instagramURL,
                            facebookURL, user?.url!!, dribbbleURL, twitterURL))
                }
            }
        }

        doAsync {
            listProjects = network.getUserProjects(userID, pageNumber).projects
            uiThread {
                projects.postValue(listProjects)
                pageNumber++
            }
        }
    }

    fun getUser(): LiveData<UserBinding> {
        return viewModel
    }

    fun getProjects(): LiveData<MutableList<Project>> {
        return projects
    }

    fun loadMoreProjects() {
        if (!allLoaded) {
            doAsync {
                val newProjects = network.getUserProjects(userID, pageNumber).projects
                if (newProjects.size == 0) {
                    allLoaded = true
                }
                listProjects.addAll(newProjects)
                uiThread {
                    projects.postValue(listProjects)
                    pageNumber++
                }
            }
        }
    }

    fun shareUser(activity: AppCompatActivity) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, user?.url)
        activity.startActivity(Intent.createChooser(share, activity.getString(R.string.share)))
    }

    fun checkIfInDB(): Boolean {
        return db.bookmarkedPeopleDB.contains(userID)
    }

    fun addToDB() {
        user?.let {
            try {
                db.peopleDB.add(user!!)
                db.myRefPeople?.setValue(db.peopleDB)
                db.bookmarkedPeopleDB.add(userID)
                db.myRefBookmarkedPeople?.setValue(db.bookmarkedPeopleDB)
            } catch (e: com.google.firebase.database.DatabaseException) {

            }
        }
    }

    fun removeFromDB() {
        db.bookmarkedPeopleDB.remove(userID)
        db.peopleDB.removeAll { p -> p.id.toString().equals(userID) }
        db.myRefPeople?.setValue(db.peopleDB)
        db.myRefBookmarkedPeople?.setValue(db.bookmarkedPeopleDB)
    }
}