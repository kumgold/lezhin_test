package com.example.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class ImageConverter {
    @TypeConverter
    fun listToJson(value: List<LocalImage>): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<LocalImage>? {
        return Gson().fromJson(value, Array<LocalImage>::class.java).toList()
    }
}