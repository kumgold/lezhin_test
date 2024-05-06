package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM image_local")
    fun getAllImages(): Flow<List<ImageLocal>>

    @Query("SELECT * FROM image_local WHERE keyword LIKE '%' || :keyword || '%'")
    fun searchImages(keyword: String): Flow<List<ImageLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ImageLocal)

    @Query("DELETE FROM image_local WHERE id IN (:list)")
    suspend fun deleteImages(list: List<String>)
}