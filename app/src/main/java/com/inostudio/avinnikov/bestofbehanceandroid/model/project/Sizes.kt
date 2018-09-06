package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Sizes(
        @SerializedName("max_1920") val max1920: String = "",
        @SerializedName("max_1400") val max1400: String = "",
        @SerializedName("max_1240") val max1240: String = "",
        @SerializedName("max_1200") val max1200: String = "",
        @SerializedName("max_2800") val max2800: String = "",
        @SerializedName("max_3840") val max3840: String = "",
        @SerializedName("hd") val hd: String = "",
        @SerializedName("original") val original: String = "",
        @SerializedName("disp") val disp: String = ""
)