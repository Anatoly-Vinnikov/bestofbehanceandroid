package com.inostudio.avinnikov.bestofbehanceandroid.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.Gson
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments.CommentsList
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Projects
import com.inostudio.avinnikov.bestofbehanceandroid.model.user.Users
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class Networking private constructor() {
    init {
        //println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = Networking()
    }

    companion object {
        val instance: Networking by lazy { Holder.INSTANCE }
    }

    private var client = OkHttpClient()
    private val keys = mutableListOf("xMrW480v8SrR9J02koQXiIEEMr3uzIfd", "F1lwShkjM3XUL5swuxcJAewCtdpPk3Al",
            "6pPaPMPRDFpGbHnWzyClx7ukYSmKX4Ah", "59m1jjBwql9ktg40RzAUveytYC4dEe6z", "T4FXzBbZPcDyHgQhEj7ilwmf1iCw6yCy",
            "kkmATJDBwV4GBmYVLQu7x1MBIe3kGykV", "tJRDZvGpcE5ayyvbfXWm9KqX76Aaah1U", "aPaDma9Ie8TxYft961SSQLXoCN9lG4Ts",
            "2kvLu3fsslZ1UTX1Ybp5c37XmD3kMGKP", "0QmPh684DRz1SpWHDikkyFCzLShGiHPi")
    //xMrW480v8SrR9J02koQXiIEEMr3uzIfd
    //F1lwShkjM3XUL5swuxcJAewCtdpPk3Al
    //6pPaPMPRDFpGbHnWzyClx7ukYSmKX4Ah
    //59m1jjBwql9ktg40RzAUveytYC4dEe6z
    //T4FXzBbZPcDyHgQhEj7ilwmf1iCw6yCy
    //kkmATJDBwV4GBmYVLQu7x1MBIe3kGykV
    //tJRDZvGpcE5ayyvbfXWm9KqX76Aaah1U
    //aPaDma9Ie8TxYft961SSQLXoCN9lG4Ts
    //2kvLu3fsslZ1UTX1Ybp5c37XmD3kMGKP
    //0QmPh684DRz1SpWHDikkyFCzLShGiHPi
    //1500 requests per hour in total

    //userFragment
    @Throws(IOException::class)
    fun getUser(userID: String): Users {
        lateinit var response: Response
        lateinit var users: Users
        for (key in keys) {
            //Timber.tag("-----------------------").d("Get user with key $key")
            val request = Request.Builder()
                    .url("https://api.behance.net/v2/users/$userID?client_id=$key")
                    .build()

            response = client.newCall(request).execute()
            users = Gson().fromJson(response.body()!!.string(), Users::class.java)
            if (users.httpCode != 429) {
                return users
            }
        }
        return users
    }

    @Throws(IOException::class)
    fun getUserProjects(userID: String, page: Int): Projects {
        lateinit var response: Response
        lateinit var projects: Projects
        for (key in keys) {
            //Timber.tag("-----------------------").d("Get user's projects with key $key")
            val request = Request.Builder()
                    .url("https://api.behance.net/v2/users/$userID/projects?page=$page&client_id=$key")
                    .build()

            response = client.newCall(request).execute()
            projects = Gson().fromJson(response.body()!!.string(), Projects::class.java)
            if (projects.httpCode != 429) {
                return projects
            }
        }
        return projects
    }

    //ProjectsListFragment
    @Throws(IOException::class)
    fun getProjects(page: Int): Projects {
        lateinit var response: Response
        lateinit var projects: Projects
        for (key in keys) {
            //Timber.tag("-----------------------").d("Get projects with key $key")
            val request = Request.Builder()
                    .url("https://api.behance.net/v2/projects?page=$page&sort=appreciations&client_id=$key")
                    .build()

            response = client.newCall(request).execute()
            projects = Gson().fromJson(response.body()!!.string(), Projects::class.java)
            if (projects.httpCode != 429) {
                return projects
            }
        }
        return projects
    }

    //ProjectViewFragment
    @Throws(IOException::class)
    fun getProject(projectID: String): com.inostudio.avinnikov.bestofbehanceandroid.model.project.Projects {
        lateinit var response: Response
        lateinit var projects: com.inostudio.avinnikov.bestofbehanceandroid.model.project.Projects
        for (key in keys) {
            //Timber.tag("-----------------------").d("Get project with key $key")
            val request = Request.Builder()
                    .url("https://api.behance.net/v2/projects/$projectID?client_id=$key")
                    .build()

            response = client.newCall(request).execute()
            projects = Gson().fromJson(response.body()!!.string(), com.inostudio.avinnikov.bestofbehanceandroid.model.project.Projects::class.java)
            if (projects.httpCode != 429) {
                return projects
            }
        }
        return projects
    }

    @Throws(IOException::class)
    fun getComments(projectID: String, page: Int): CommentsList {
        lateinit var response: Response
        lateinit var comments: CommentsList
        for (key in keys) {
            //Timber.tag("-----------------------").d("Get comments with key $key")
            val request = Request.Builder()
                    .url("https://api.behance.net/v2/projects/$projectID/comments?page=$page&client_id=$key")
                    .build()

            response = client.newCall(request).execute()
            comments = Gson().fromJson(response.body()!!.string(), CommentsList::class.java)
            if (comments.httpCode != 429) {
                return comments
            }
        }
        return comments
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}