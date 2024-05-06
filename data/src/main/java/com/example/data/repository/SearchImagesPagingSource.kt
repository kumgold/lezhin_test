package com.example.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.BuildConfig
import com.example.data.api.KakaoApiService
import com.example.data.data.ImageResult

class SearchImagesPagingSource(
    private val api: KakaoApiService,
    private val query: String
) : PagingSource<Int, ImageResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResult> {
        return try {
            val page = params.key ?: 1

            val items = api.searchImages(
                key = BuildConfig.KAKAO_API_KEY,
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

    override fun getRefreshKey(state: PagingState<Int, ImageResult>): Int? {
        return state.anchorPosition
    }
}