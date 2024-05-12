package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.KakaoApiService
import com.example.data.data.NetworkImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchRepository {
    fun searchImages(query: String): Flow<PagingData<NetworkImage>>
}

class SearchRepositoryImpl @Inject constructor(
    private val searchApiService: KakaoApiService
) : SearchRepository {
    override fun searchImages(query: String): Flow<PagingData<NetworkImage>> {
        return Pager(
            PagingConfig(10)
        ) {
            SearchImagesPagingSource(searchApiService, query)
        }.flow
    }
}