package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Projects(
        @SerializedName("projects") val projects: MutableList<Project>,
        @SerializedName("http_code") val httpCode: Int
)