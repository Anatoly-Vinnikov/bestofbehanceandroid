package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Component(
        @SerializedName("id") val id: Int,
        @SerializedName("sizes") val sizes: Sizes
)