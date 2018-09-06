package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("followers") val followers: Int,
        @SerializedName("following") val following: Int,
        @SerializedName("appreciations") val appreciations: Int,
        @SerializedName("views") val views: Int,
        @SerializedName("comments") val comments: Int
)