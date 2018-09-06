package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("username") val username: String,
        @SerializedName("city") val city: String,
        @SerializedName("state") val state: String,
        @SerializedName("country") val country: String,
        @SerializedName("location") val location: String,
        @SerializedName("company") val company: String,
        @SerializedName("occupation") val occupation: String,
        @SerializedName("created_on") val createdOn: Int,
        @SerializedName("url") val url: String,
        @SerializedName("images") val images: Images,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("fields") val fields: List<String>,
        @SerializedName("has_default_image") val hasDefaultImage: Int,
        @SerializedName("website") val website: String,
        @SerializedName("stats") val stats: Stats
)