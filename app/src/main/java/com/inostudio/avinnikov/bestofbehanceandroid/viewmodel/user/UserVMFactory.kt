package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class UserVMFactory(val userID: String = "") : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            UserVM(this.userID) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}