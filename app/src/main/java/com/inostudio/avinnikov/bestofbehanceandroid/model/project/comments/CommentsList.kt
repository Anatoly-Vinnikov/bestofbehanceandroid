package com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments

import com.google.gson.annotations.SerializedName

data class CommentsList(
        @SerializedName("comments") val comments: MutableList<Comment>,
        @SerializedName("http_code") val httpCode: Int
)