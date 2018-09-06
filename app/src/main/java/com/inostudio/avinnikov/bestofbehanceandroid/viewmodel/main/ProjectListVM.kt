package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ProjectListVM : ViewModel() {
    private val db = Database.INSTANCE
    private val network = Networking.instance
    private var pageNumber = 1
    var projects = MutableLiveData<MutableList<Project>>()
    private var listProjects = mutableListOf<Project>()

    fun init() {
        pageNumber = 1
        doAsync {
            listProjects = network.getProjects(pageNumber).projects
            uiThread {
                projects.postValue(listProjects)
                pageNumber++
            }
        }
    }

    fun getProjects(): LiveData<MutableList<Project>> {
        return projects
    }

    fun loadMoreProjects() {
        doAsync {
            listProjects.addAll(network.getProjects(pageNumber).projects)
            uiThread {
                projects.postValue(listProjects)
                pageNumber++
            }
        }
    }

    fun checkIfInDB(projectID: String): Boolean {
        return db.bookmarkedProjectsDB.contains(projectID)
    }

    fun addToDB(project: Project) {
        try {
            db.projectsDB.add(project)
            db.myRefProjects?.setValue(db.projectsDB)
            db.bookmarkedProjectsDB.add(project.id.toString())
            db.myRefBookmarkedProjects?.setValue(db.bookmarkedProjectsDB)
        } catch (e: com.google.firebase.database.DatabaseException) {

        }
    }

    fun removeFromDB(project: Project) {
        db.bookmarkedProjectsDB.remove(project.id.toString())
        db.projectsDB.removeAll { p -> p.id == project.id }
        db.myRefProjects?.setValue(db.projectsDB)
        db.myRefBookmarkedProjects?.setValue(db.bookmarkedProjectsDB)
    }
}