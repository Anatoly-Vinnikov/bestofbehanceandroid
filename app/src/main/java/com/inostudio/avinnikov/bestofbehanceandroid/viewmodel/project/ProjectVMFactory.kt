package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class ProjectVMFactory(val projectID: String = "") : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProjectVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            ProjectVM(this.projectID) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}