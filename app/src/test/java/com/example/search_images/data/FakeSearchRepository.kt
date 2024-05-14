package com.example.search_images.data

import androidx.paging.PagingData
import com.example.data.data.NetworkImage
import com.example.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSearchRepository(
    private val images: List<NetworkImage> = emptyList()
) : SearchRepository {
    override fun searchImages(query: String): Flow<PagingData<NetworkImage>> {
        return flow {
            emit(
                PagingData.from(images)
            )
        }
    }
}