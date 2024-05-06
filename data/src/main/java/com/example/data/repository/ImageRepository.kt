package com.example.data.repository

import com.example.data.data.ImageResult
import com.example.data.db.ImageDao
import com.example.data.db.ImageLocal
import com.example.data.mapper.ImageMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageDao: ImageDao
) {
    fun getAllImages(): Flow<List<ImageLocal>> {
        return imageDao.getAllImages()
    }

    fun searchImages(keyword: String): Flow<List<ImageLocal>> {
        return imageDao.searchImages(keyword)
    }

    suspend fun insertImage(keyword: String, image: ImageResult) {
        withContext(Dispatchers.IO) {
            imageDao.insert(ImageMapper.imageResultToImageLocal(keyword, image))
        }
    }

    suspend fun deleteImages(ids: List<String>) {
        imageDao.deleteImages(ids)
    }
}