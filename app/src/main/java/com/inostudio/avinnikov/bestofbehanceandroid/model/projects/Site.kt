package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Site(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("parent_id") val parentId: Int = 0,
        @SerializedName("name") val name: String = "",
        @SerializedName("key") val key: String = "",
        @SerializedName("icon") val icon: String = "",
        @SerializedName("app_icon") val appIcon: String = "",
        @SerializedName("domain") val domain: String = "",
        @SerializedName("url") val url: String = "",
        @SerializedName("ribbon") val ribbon: Ribbon? = null
)