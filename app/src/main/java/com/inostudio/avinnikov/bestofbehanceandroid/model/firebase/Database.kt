package com.inostudio.avinnikov.bestofbehanceandroid.model.firebase

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.firebase.database.*
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.model.user.User


class Database private constructor() {
    init {
        //println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = Database()
    }

    companion object {
        val INSTANCE: Database by lazy { Holder.INSTANCE }
    }

    private val prefsFileName = "com.inostudio.avinnikov.prefs"

    private val database = FirebaseDatabase.getInstance()
    private val persistence = database.setPersistenceEnabled(true)
    private var myRef: DatabaseReference? = null

    var myRefProjects: DatabaseReference? = null
    var myRefPeople: DatabaseReference? = null
    var myRefBookmarkedProjects: DatabaseReference? = null
    var myRefBookmarkedPeople: DatabaseReference? = null

    var projectsDB = mutableListOf<Project>()
    var bookmarkedProjectsDB = mutableListOf<String>()
    var peopleDB = mutableListOf<User>()
    var bookmarkedPeopleDB = mutableListOf<String>()

    fun load(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, MODE_PRIVATE)
        var userID: String? = prefs.getString("userID", "-1")
        if (userID.equals("-1")) {
            //Timber.tag("-----------------------").d("welcome new user")
            val users = database.getReference("users")
            users.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val editor = prefs.edit()
                    userID = try {
                        editor.putString("userID", (snapshot.children.last().key!!.toInt() + 1).toString())
                        (snapshot.children.last().key!!.toInt() + 1).toString()
                    } catch (e: java.util.NoSuchElementException) {
                        editor.putString("userID", "0")
                        "0"
                    }
                    editor.apply()
                    myRef = database.getReference("users").child(userID!!)
                    myRef?.child("holder")?.setValue("holder $userID")
                    load2(userID!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } else {
            load2(userID!!)
        }
    }

    fun load2(userID: String) {

        myRef = database.getReference("users").child(userID)
        myRef?.keepSynced(true)
        myRefProjects = myRef?.child("projects")
        myRefPeople = myRef?.child("people")
        myRefBookmarkedProjects = myRef?.child("bookmarkedProjects")
        myRefBookmarkedPeople = myRef?.child("bookmarkedPeople")

        myRefProjects?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<MutableList<Project>>() {}
                projectsDB.clear()
                try {
                    projectsDB.addAll(snapshot.getValue(t)!!)
                } catch (e: KotlinNullPointerException) {
                    // handler
                }
                /*Timber.tag("-----------------------").d("db synced")
                Timber.tag("-----------------------").d("number of posts in DB ${projectsDB.size}")*/
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        myRefBookmarkedProjects?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<MutableList<String>>() {}
                bookmarkedProjectsDB.clear()
                try {
                    bookmarkedProjectsDB.addAll(snapshot.getValue(t)!!)
                } catch (e: KotlinNullPointerException) {
                    // handler
                }
                /*Timber.tag("-----------------------").d("db synced")
                Timber.tag("-----------------------").d("number of bookmarks in DB ${bookmarkedProjectsDB.size}")*/
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        myRefPeople?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<MutableList<User>>() {}
                peopleDB.clear()
                try {
                    peopleDB.addAll(snapshot.getValue(t)!!)
                } catch (e: KotlinNullPointerException) {
                    // handler
                }
                /*Timber.tag("-----------------------").d("db synced")
                Timber.tag("-----------------------").d("number of posts in DB ${projectsDB.size}")*/
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        myRefBookmarkedPeople?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<MutableList<String>>() {}
                bookmarkedPeopleDB.clear()
                try {
                    bookmarkedPeopleDB.addAll(snapshot.getValue(t)!!)
                } catch (e: KotlinNullPointerException) {
                    // handler
                }
                /*Timber.tag("-----------------------").d("db synced")
                Timber.tag("-----------------------").d("number of bookmarked people in DB ${bookmarkedPeopleDB.size}")*/
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}