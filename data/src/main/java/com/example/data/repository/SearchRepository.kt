package com.example.data.repository

import com.example.data.api.KakaoApiService
import com.example.data.data.NetworkImage
import javax.inject.Inject

interface SearchRepository {
    suspend fun searchImages(query: String, page: Int): List<NetworkImage>
}

class SearchRepositoryImpl @Inject constructor(
    private val searchApiService: KakaoApiService
) : SearchRepository {
    override suspend fun searchImages(query: String, page: Int): List<NetworkImage> {
        try {
            val response = searchApiService.searchImages(query, page)
            return response.result
        } catch (e: Exception) {
            return emptyList()
        }
    }
}