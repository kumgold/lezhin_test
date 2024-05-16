package com.example.search_images.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.data.NetworkImage
import com.example.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class FakeSearchRepository(
    private val images: List<NetworkImage> = emptyList()
) : SearchRepository {
    override fun searchImages(query: String): Flow<PagingData<NetworkImage>> {
        return Pager(
            PagingConfig(10)
        ) {
            FakeImagesPagingSource(images)
        }.flow
    }
}