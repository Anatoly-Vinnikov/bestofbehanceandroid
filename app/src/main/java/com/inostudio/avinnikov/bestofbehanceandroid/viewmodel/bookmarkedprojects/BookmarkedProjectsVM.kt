package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.bookmarkedprojects

import android.arch.lifecycle.ViewModel
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project


class BookmarkedProjectsVM : ViewModel() {
    private val db = Database.INSTANCE

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