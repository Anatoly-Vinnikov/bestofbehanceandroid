package com.inostudio.avinnikov.bestofbehanceandroid.model.user

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("first_name") val firstName: String = "",
        @SerializedName("last_name") val lastName: String = "",
        @SerializedName("location") val location: String = "",
        @SerializedName("occupation") val occupation: String = "",
        @SerializedName("url") val url: String = "",
        @SerializedName("images") val images: Images? = null,
        @SerializedName("display_name") val displayName: String = "",
        @SerializedName("sections") val sections: Map<String, String>? = null,
        @SerializedName("stats") val stats: Stats? = null,
        @SerializedName("social_links") val socialLinks: MutableList<SocialLink>? = null
)