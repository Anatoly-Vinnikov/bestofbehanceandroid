package com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments

import com.google.gson.annotations.SerializedName

data class Comment(
        @SerializedName("comment") val comment: String,
        @SerializedName("user") val user: User,
        @SerializedName("created_on") val createdOn: Int
)