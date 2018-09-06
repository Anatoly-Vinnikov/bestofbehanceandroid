package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Feature(
        @SerializedName("featured_on") val featuredOn: Int = 0,
        @SerializedName("site") val site: Site? = null,
        @SerializedName("url") val url: String = ""
)