package com.inostudio.avinnikov.bestofbehanceandroid.model.user

import com.google.gson.annotations.SerializedName

data class SocialLink(
        @SerializedName("url") val url: String = "",
        @SerializedName("service_name") val serviceName: String = ""
)