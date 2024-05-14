package com.example.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "image_local", primaryKeys = ["id"])
data class LocalImage(
    @ColumnInfo val id: String = System.currentTimeMillis().toString(),
    @ColumnInfo val imageUrl: String = "",
    @ColumnInfo val date: String = "",
    @ColumnInfo val width: Int = 0,
    @ColumnInfo val height: Int = 0,
    @ColumnInfo val keyword: String
)