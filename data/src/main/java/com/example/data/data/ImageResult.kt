package com.example.data.data

import com.google.gson.annotations.SerializedName

data class ImageResult(
    @SerializedName("collection") val collection: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("display_sitename") val displaySite: String,
    @SerializedName("doc_url") val documentUrl: String,
    @SerializedName("datetime") val dateTime: String
)
