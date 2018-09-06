package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.main

import android.databinding.BaseObservable
import android.databinding.Bindable

class ProjectListBinding(
        @Bindable val cover404: String = "",
        //stats
        @Bindable val appreciations: String = "",
        @Bindable val views: String = "",
        @Bindable val comments: String = "",
        //owner
        @Bindable val firstLastName: String = "",
        @Bindable val occupation: String = "",
        @Bindable val image276: String = "",
        @Bindable val grid: Boolean = false
) : BaseObservable()