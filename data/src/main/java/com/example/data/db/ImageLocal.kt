package com.example.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "image_local", primaryKeys = ["id"])
data class ImageLocal(
    @ColumnInfo val id: String = System.currentTimeMillis().toString(),
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val date: String,
    @ColumnInfo val width: Int,
    @ColumnInfo val height: Int,
    @ColumnInfo val keyword: String
)