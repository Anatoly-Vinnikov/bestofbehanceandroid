package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.bookmarkedpeople

import android.arch.lifecycle.ViewModel
import com.inostudio.avinnikov.bestofbehanceandroid.model.firebase.Database
import com.inostudio.avinnikov.bestofbehanceandroid.model.user.User


class BookmarkedPeopleVM : ViewModel() {
    private val db = Database.INSTANCE

    fun checkIfInDB(userID: String): Boolean {
        return db.bookmarkedPeopleDB.contains(userID)
    }

    fun removeFromDB(user: User) {
        db.bookmarkedPeopleDB.remove(user.id.toString())
        db.peopleDB.removeAll { p -> p.id == user.id }
        db.myRefPeople?.setValue(db.peopleDB)
        db.myRefBookmarkedPeople?.setValue(db.bookmarkedPeopleDB)
    }
}