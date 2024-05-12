package com.example.search_images.data

import com.example.data.data.NetworkImage
import com.example.data.db.LocalImage
import com.example.data.mapper.toLocalImage
import com.example.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class FakeImageRepository(
    private val imageDao: FakeImageDao
) : ImageRepository {
    override fun getAllImages(): Flow<List<LocalImage>> {
        return imageDao.getAllImages()
    }

    override fun searchImages(keyword: String): Flow<List<LocalImage>> {
        return imageDao.searchImages(keyword)
    }

    override suspend fun insertImage(keyword: String, image: NetworkImage): Result<Boolean> {
        return try {
            imageDao.insert(image.toLocalImage(keyword))
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
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