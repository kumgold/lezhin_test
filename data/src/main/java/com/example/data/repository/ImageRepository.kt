package com.example.data.repository

import com.example.data.data.NetworkImage
import com.example.data.db.ImageDao
import com.example.data.db.LocalImage
import com.example.data.mapper.ImageMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageDao: ImageDao
) {
    fun getAllImages(): Flow<List<LocalImage>> {
        return imageDao.getAllImages()
    }

    fun searchImages(keyword: String): Flow<List<LocalImage>> {
        return imageDao.searchImages(keyword)
    }

    suspend fun insertImage(keyword: String, image: NetworkImage): Result<Boolean> {
        return try {
            imageDao.insert(ImageMapper.imageResultToImageLocal(keyword, image))
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteImages(ids: List<String>): Result<Boolean> {
        return try {
            imageDao.deleteImages(ids)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}