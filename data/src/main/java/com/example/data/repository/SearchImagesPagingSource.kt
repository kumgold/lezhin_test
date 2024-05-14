package com.example.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.KakaoApiService
import com.example.data.data.NetworkImage

class SearchImagesPagingSource(
    private val api: KakaoApiService,
    private val query: String
) : PagingSource<Int, NetworkImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkImage> {
        return try {
            val page = params.key ?: 1

            val items = api.searchImages(
                query = query,
                page = page
            )

            LoadResult.Page(
                data = items.result,
                prevKey = page - 1,
                nextKey = page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkImage>): Int? {
        return state.anchorPosition
    }
}