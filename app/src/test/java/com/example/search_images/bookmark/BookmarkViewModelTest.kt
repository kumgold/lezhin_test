package com.example.search_images.bookmark

import com.example.data.db.LocalImage
import com.example.data.repository.ImageRepository
import com.example.search_images.MainCoroutineRule
import com.example.search_images.ui.bookmark.BookmarkViewModel
import com.example.search_images.data.FakeImageDao
import com.example.search_images.data.FakeImageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookmarkViewModelTest {
    private lateinit var bookmarkViewModel: BookmarkViewModel

    private lateinit var imageRepository: ImageRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        val fakeImageDao = FakeImageDao(
            listOf(
                LocalImage(
                    id = "",
                    imageUrl = "",
                    date = "",
                    width = 100,
                    height = 100,
                    keyword = "카리나"
                ),
                LocalImage(
                    id = "",
                    imageUrl = "",
                    date = "",
                    width = 100,
                    height = 100,
                    keyword = "미연"
                ),
            )
        )
        imageRepository = FakeImageRepository(fakeImageDao)

        bookmarkViewModel = BookmarkViewModel(imageRepository)
    }

    @Test
    fun loadAllImagesFromRepository_searchForKeyword() = runTest {
        bookmarkViewModel.getAllImages()

        val images = bookmarkViewModel.uiState.value.result

        Assert.assertEquals(images.size, 2)
    }
}