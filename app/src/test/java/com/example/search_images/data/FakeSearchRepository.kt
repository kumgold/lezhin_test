package com.example.search_images.data

import com.example.data.data.NetworkImage
import com.example.data.repository.SearchRepository

class FakeSearchRepository(
    private val images: List<NetworkImage> = emptyList()
) : SearchRepository {
    override suspend fun searchImages(query: String, page: Int): List<NetworkImage> {
        return images
    }
}