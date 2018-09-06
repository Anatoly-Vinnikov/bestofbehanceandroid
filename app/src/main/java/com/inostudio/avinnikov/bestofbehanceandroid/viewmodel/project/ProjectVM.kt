package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.Module
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.Projects
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments.Comment
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.*
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.Networking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ProjectVM(val projectID: String = "") : ViewModel() {
    private val db = Database.INSTANCE
    private val network = Networking.instance
    var project: Projects? = null
    private var listComments = mutableListOf<Comment>()
    var comments = MutableLiveData<MutableList<Comment>>()
    private var modules = MutableLiveData<MutableList<Module>>()
    private var viewModel = MutableLiveData<ProjectBinding>()
    private var pageNumber = 1
    private var allLoaded = false

    init {
        doAsync {
            project = network.getProject(projectID)
            uiThread {
                project?.project?.let {
                    val owner = project?.project?.owners?.get(0)
                    val images = owner?.images
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
                    viewModel.postValue(ProjectBinding(project?.project?.stats?.appreciations.toString(),
                            project?.project?.stats?.views.toString(), project?.project?.stats?.comments.toString(),
                            owner?.displayName!!, owner.occupation, curProfileImage, "COMMENTS (${project?.project?.stats?.comments})", "Comments(${project?.project?.stats?.comments})"))

                    val listModules = mutableListOf<Module>()
                    for (module in project?.project?.modules!!) {
                        if (module.type.equals("embed")) {
                            val videoURL = module.embed.split("\"")
                            module.embed = videoURL[1]
                        }
                        if (module.type.equals("audio")) {
                            val audioStr = module.embed.split("\n")
                            val audioURL = audioStr[2].split("\"")
                            module.embed = audioURL[3]
                        }
                        if (module.type.equals("media_collection")) {
                            for (component in module.components) {
                                listModules.add(Module(module.id, module.projectId,
                                        "image", "", "", "", module.components, component.sizes, ""))
                            }
                        } else {
                            listModules.add(module)
                        }
                    }
                    project?.project?.modules = listModules
                    modules.postValue(project?.project?.modules)
                }
            }
        }

        doAsync {
            listComments = network.getComments(projectID, pageNumber).comments
            uiThread {
                comments.postValue(listComments)
                pageNumber++
            }
        }
    }

    fun getProject(): LiveData<ProjectBinding> {
        return viewModel
    }

    fun getModules(): LiveData<MutableList<Module>> {
        return modules
    }

    fun getComments(): LiveData<MutableList<Comment>> {
        return comments
    }

    fun loadMoreComments() {
        if (!allLoaded) {
            doAsync {
                val newComments = network.getComments(projectID, pageNumber).comments
                if (newComments.size == 0) {
                    allLoaded = true
                }
                listComments.addAll(newComments)
                uiThread {
                    comments.postValue(listComments)
                    pageNumber++
                }
            }
        }
    }

    fun shareProject(activity: AppCompatActivity) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, project?.project?.url)
        activity.startActivity(Intent.createChooser(share, activity.getString(R.string.share)))
    }

    fun checkIfInDB(): Boolean {
        return db.bookmarkedProjectsDB.contains(projectID)
    }

    fun addToDB() {
        project?.project?.let {
            try {
                val proj = Project(project?.project?.id!!, project?.project?.url!!, Covers(project?.project?.covers?.cover404!!, project?.project?.covers?.cover202!!,
                        project?.project?.covers?.cover230!!, project?.project?.covers?.cover115!!, project?.project?.covers?.coverOrig!!),
                        mutableListOf<Owner>(Owner(project?.project?.owners?.get(0)?.id!!, project?.project?.owners?.get(0)?.firstName!!,
                                project?.project?.owners?.get(0)?.lastName!!, project?.project?.owners?.get(0)?.occupation!!,
                                project?.project?.owners?.get(0)?.url!!, Images(project?.project?.owners?.get(0)?.images?.image276!!), project?.project?.owners?.get(0)?.displayName!!)),
                        Stats(project?.project?.stats?.appreciations!!, project?.project?.stats?.views!!, project?.project?.stats?.comments!!))
                db.projectsDB.add(proj)
                db.myRefProjects?.setValue(db.projectsDB)
                db.bookmarkedProjectsDB.add(projectID)
                db.myRefBookmarkedProjects?.setValue(db.bookmarkedProjectsDB)
            } catch (e: com.google.firebase.database.DatabaseException) {

            }
        }
    }

    fun removeFromDB() {
        db.bookmarkedProjectsDB.remove(projectID)
        db.projectsDB.removeAll { p -> p.id.toString().equals(projectID) }
        db.myRefProjects?.setValue(db.projectsDB)
        db.myRefBookmarkedProjects?.setValue(db.bookmarkedProjectsDB)
    }
}