package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("appreciations") val appreciations: Int = 0,
        @SerializedName("views") val views: Int = 0,
        @SerializedName("comments") val comments: Int = 0
) : BaseObservable()