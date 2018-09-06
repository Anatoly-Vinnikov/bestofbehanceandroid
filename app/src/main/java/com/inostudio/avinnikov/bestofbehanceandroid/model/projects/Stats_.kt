package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Stats_(
        @SerializedName("views") val views: Int,
        @SerializedName("appreciations") val appreciations: Int,
        @SerializedName("comments") val comments: Int
)