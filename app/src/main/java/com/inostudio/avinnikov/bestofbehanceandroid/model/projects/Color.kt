package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import com.google.gson.annotations.SerializedName

data class Color(
        @SerializedName("h") val colorh: Double = 0.0,
        @SerializedName("s") val colors: Double = 0.0,
        @SerializedName("v") val colorv: Double = 0.0,
        @SerializedName("r") val colorr: Int = 0,
        @SerializedName("g") val colorg: Int = 0,
        @SerializedName("b") val colorb: Int = 0
)