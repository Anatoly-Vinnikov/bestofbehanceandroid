package com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("images") val images: Images,
        @SerializedName("display_name") val displayName: String
)