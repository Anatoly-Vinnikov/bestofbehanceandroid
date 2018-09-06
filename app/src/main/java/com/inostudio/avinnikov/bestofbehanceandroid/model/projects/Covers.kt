package com.inostudio.avinnikov.bestofbehanceandroid.model.projects

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class Covers(
        @SerializedName("404") val cover404: String = "",
        @SerializedName("202") val cover202: String = "",
        @SerializedName("230") val cover230: String = "",
        @SerializedName("115") val cover115: String = "",
        @SerializedName("original") val coverOrig: String = ""
) : BaseObservable()