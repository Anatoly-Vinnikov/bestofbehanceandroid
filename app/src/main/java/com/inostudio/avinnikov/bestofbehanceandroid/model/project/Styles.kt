package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Styles(
        @SerializedName("spacing") val spacing: Spacing,
        @SerializedName("background") val background: Background
)