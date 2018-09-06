package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Project(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("published_on") val publishedOn: Int,
        @SerializedName("url") val url: String,
        @SerializedName("covers") val covers: Covers,
        @SerializedName("owners") val owners: List<Owner>,
        @SerializedName("stats") val stats: Stats,
        @SerializedName("description") val description: String,
        @SerializedName("modules") var modules: MutableList<Module>,
        @SerializedName("styles") val styles: Styles
)