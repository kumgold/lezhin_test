package com.example.data.data

import com.google.gson.annotations.SerializedName

data class NetworkImage(
    @SerializedName("collection") val collection: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("display_sitename") val displaySite: String? = null,
    @SerializedName("doc_url") val documentUrl: String? = null,
    @SerializedName("datetime") val dateTime: String = ""
)
