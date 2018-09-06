package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.google.gson.annotations.SerializedName

data class Module(
        @SerializedName("id") val id: Int,
        @SerializedName("project_id") val projectId: Int,
        @SerializedName("type") val type: String,
        @SerializedName("text_plain") val textPlain: String,
        @SerializedName("text") val text: String,
        @SerializedName("collection_type") val collectionType: String,
        @SerializedName("components") val components: List<Component>,
        @SerializedName("sizes") val sizes: Sizes,
        @SerializedName("embed") var embed: String
)