package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("first_name") val firstName: String = "",
        @SerializedName("last_name") val lastName: String = "",
        @SerializedName("occupation") val occupation: String = "",
        @SerializedName("url") val url: String = "",
        @SerializedName("images") val images: Images? = null,
        @SerializedName("display_name") val displayName: String = ""
) : BaseObservable()