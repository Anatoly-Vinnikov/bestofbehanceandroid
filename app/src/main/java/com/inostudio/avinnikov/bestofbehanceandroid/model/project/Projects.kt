package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Projects(
        @SerializedName("project") val project: Project? = null,
        @SerializedName("http_code") val httpCode: Int = 0
)