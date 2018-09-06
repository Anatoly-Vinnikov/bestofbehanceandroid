package com.inostudio.avinnikov.bestofbehanceandroid.model.user

import com.google.gson.annotations.SerializedName

data class Users(
        @SerializedName("user") val user: User,
        @SerializedName("http_code") val httpCode: Int
)