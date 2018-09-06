package com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project

import android.databinding.BaseObservable
import android.databinding.Bindable

class CommentBinding(
        @Bindable val commentText: String = "",
        @Bindable val date: String = "",
        //owner
        @Bindable val firstLastName: String = "",
        @Bindable val image138: String = ""
) : BaseObservable()