package com.inostudio.avinnikov.bestofbehanceandroid.model.user

import com.google.gson.annotations.SerializedName

data class Images(
        @SerializedName("50") val image50: String = "",
        @SerializedName("100") val image100: String = "",
        @SerializedName("115") val image115: String = "",
        @SerializedName("138") val image138: String = "",
        @SerializedName("130") val image130: String = "",
        @SerializedName("276") val image276: String = ""
)