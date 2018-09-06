package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user

import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.net.Uri
import android.view.View

class UserBinding(
        //stats
        @Bindable var appreciations: String = "",
        @Bindable var views: String = "",
        @Bindable var followers: String = "",
        @Bindable var following: String = "",
        //owner
        @Bindable var firstLastName: String = "",
        @Bindable var occupation: String = "",
        @Bindable var location: String = "",
        @Bindable var about: String = "",
        @Bindable var profileImage: String = "",
        @Bindable var pinterestURL: String = "",
        @Bindable var instagramURL: String = "",
        @Bindable var facebookURL: String = "",
        @Bindable var behanceURL: String = "",
        @Bindable var dribbbleURL: String = "",
        @Bindable var twitterURL: String = ""
) : BaseObservable() {
    fun openLink(url: String, view: View) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            view.context.startActivity(i)
        } catch (ex: android.content.ActivityNotFoundException) {
        }
    }
}