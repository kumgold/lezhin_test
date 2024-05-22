package com.example.search_images.const

import androidx.annotation.StringRes
import com.example.search_images.R

sealed class AppScreen(val route: String, @StringRes val resourceId: Int) {
    data object Search : AppScreen(AppScreenDestination.SEARCH, R.string.search)
    data object Bookmark : AppScreen(AppScreenDestination.BOOKMARK, R.string.bookmark)
    data object ImageDetail : AppScreen(AppScreenDestination.IMAGE_DETAIL, R.string.image_detail)
}

object AppScreenDestination {
    const val SEARCH = "search"
    const val BOOKMARK = "bookmark"
    const val IMAGE_DETAIL = "image_detail"
}