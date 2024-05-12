package com.example.data.mapper

import com.example.data.data.NetworkImage
import com.example.data.db.LocalImage

fun NetworkImage.toLocalImage(keyword: String): LocalImage {
    return LocalImage(
        imageUrl = this.imageUrl,
        date = this.dateTime,
        width = this.width,
        height = this.height,
        keyword = keyword
    )
}