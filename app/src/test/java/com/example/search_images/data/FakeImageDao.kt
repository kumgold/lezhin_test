package com.example.search_images.data

import com.example.data.db.ImageDao
import com.example.data.db.LocalImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeImageDao(
    private val initialImages: List<LocalImage> = emptyList()
) : ImageDao {
    private val images = initialImages.toMutableList()

    override fun getAllImages(): Flow<List<LocalImage>> {
        return flow {
            emit(images)
        }
    }

    override fun searchImages(keyword: String): Flow<List<LocalImage>> {
        return flow {
            emit(
                images.filter {
                    it.keyword.contains(keyword)
                }
            )
        }
    }

    override suspend fun insert(item: LocalImage) {
        images.add(item)
    }

    override suspend fun deleteImages(list: List<String>) {
        images.removeIf {
            list.contains(it.id)
        }
    }
}