package com.example.data.repository

import com.example.data.data.NetworkImage
import com.example.data.db.ImageDao
import com.example.data.db.LocalImage
import com.example.data.mapper.toLocalImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ImageRepository {
    fun getAllImages(): Flow<List<LocalImage>>

    fun searchImages(keyword: String): Flow<List<LocalImage>>

    suspend fun insertImage(keyword: String, image: NetworkImage)

    suspend fun deleteImages(ids: List<String>): Result<Boolean>
}

class ImageRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
) : ImageRepository {
    override fun getAllImages(): Flow<List<LocalImage>> {
        return imageDao.getAllImages()
    }

    override fun searchImages(keyword: String): Flow<List<LocalImage>> {
        return imageDao.searchImages(keyword)
    }

    override suspend fun insertImage(keyword: String, image: NetworkImage) {
        imageDao.insert(image.toLocalImage(keyword))
    }

    override suspend fun deleteImages(ids: List<String>): Result<Boolean> {
        return try {
            imageDao.deleteImages(ids)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}