package com.example.data.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("meta") val metaData: MetaData,
    @SerializedName("documents") val result: List<ImageResult>
)