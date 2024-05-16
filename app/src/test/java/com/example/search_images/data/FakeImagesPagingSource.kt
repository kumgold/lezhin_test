package com.example.search_images.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.data.NetworkImage

class FakeImagesPagingSource(
    private val images: List<NetworkImage>
) : PagingSource<Int, NetworkImage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkImage> {
        return try {
            val page = params.key ?: 1
            LoadResult.Page(
                data = images,
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