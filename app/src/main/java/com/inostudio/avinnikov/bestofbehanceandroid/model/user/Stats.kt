package com.inostudio.avinnikov.bestofbehanceandroid.model.user

import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("followers") val followers: Int = 0,
        @SerializedName("following") val following: Int = 0,
        @SerializedName("appreciations") val appreciations: Int = 0,
        @SerializedName("views") val views: Int = 0,
        @SerializedName("comments") val comments: Int = 0
)