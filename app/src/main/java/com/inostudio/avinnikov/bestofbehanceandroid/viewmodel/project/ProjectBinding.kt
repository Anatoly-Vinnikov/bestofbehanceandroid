package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project

import android.databinding.BaseObservable
import android.databinding.Bindable

class ProjectBinding(
        //stats
        @Bindable var appreciations: String = "",
        @Bindable var views: String = "",
        @Bindable var comments: String = "",
        //owner
        @Bindable var firstLastName: String = "",
        @Bindable var occupation: String = "",
        @Bindable var profileImage: String = "",
        //strings
        @Bindable var comments1: String = "COMMENTS ()",
        @Bindable var comments2: String = "Comments()"
) : BaseObservable()