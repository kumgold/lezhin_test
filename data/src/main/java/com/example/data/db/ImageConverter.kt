package com.example.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class ImageConverter {
    @TypeConverter
    fun listToJson(value: List<ImageLocal>): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<ImageLocal>? {
        return Gson().fromJson(value, Array<ImageLocal>::class.java).toList()
    }
}