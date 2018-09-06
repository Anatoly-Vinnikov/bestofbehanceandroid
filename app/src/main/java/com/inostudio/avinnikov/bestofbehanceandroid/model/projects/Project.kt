package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Project(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("url") val url: String = "",
        @SerializedName("covers") val covers: Covers? = null,
        @SerializedName("owners") val owners: List<Owner>? = null,
        @SerializedName("stats") val stats: Stats? = null
) : BaseObservable()