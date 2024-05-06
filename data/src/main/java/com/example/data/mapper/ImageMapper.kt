package com.example.data.mapper

import com.example.data.data.ImageResult
import com.example.data.db.ImageLocal

object ImageMapper {
    fun imageResultToImageLocal(
        keyword: String,
        result: ImageResult
    ): ImageLocal {
        return ImageLocal(
            imageUrl = result.imageUrl,
            date = result.dateTime,
            width = result.width,
            height = result.height,
            keyword = keyword
        )
    }
}