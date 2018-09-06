package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Ribbon(
        @SerializedName("image") val image: String = "",
        @SerializedName("image_2x") val image2x: String = ""
)