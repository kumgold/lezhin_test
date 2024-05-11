package com.example.data.mapper

import com.example.data.data.NetworkImage
import com.example.data.db.LocalImage

object ImageMapper {
    fun imageResultToImageLocal(
        keyword: String,
        result: NetworkImage
    ): LocalImage {
        return LocalImage(
            imageUrl = result.imageUrl,
            date = result.dateTime,
            width = result.width,
            height = result.height,
            keyword = keyword
        )
    }
}