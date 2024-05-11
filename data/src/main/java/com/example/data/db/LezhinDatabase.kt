package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocalImage::class], version = 1, exportSchema = false)
@TypeConverters(ImageConverter::class)
abstract class LezhinDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        private const val LEZHIN = "lezhin_database"

        fun getInstance(context: Context): LezhinDatabase {
            return buildDatabase(context)
        }

        private fun buildDatabase(context: Context): LezhinDatabase {
            return Room.databaseBuilder(context, LezhinDatabase::class.java, LEZHIN)
                .build()
        }
    }
}