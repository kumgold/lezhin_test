package com.example.lezhintest.const

import androidx.annotation.StringRes
import com.example.lezhintest.R

sealed class LezhinScreen(val route: String, @StringRes val resourceId: Int) {
    data object Search : LezhinScreen(LezhinScreenDestination.SEARCH, R.string.search)
    data object Bookmark : LezhinScreen(LezhinScreenDestination.BOOKMARK, R.string.bookmark)
}

object LezhinScreenDestination {
    const val SEARCH = "search"
    const val BOOKMARK = "bookmark"
}