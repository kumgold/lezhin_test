package com.example.search_images.bookmark

import com.example.data.db.LocalImage
import com.example.data.repository.ImageRepository
import com.example.search_images.MainCoroutineRule
import com.example.search_images.data.FakeImageDao
import com.example.search_images.data.FakeImageRepository
import com.example.search_images.ui.bookmark.BookmarkViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookmarkViewModelTest {
    private lateinit var viewModel: BookmarkViewModel

    private lateinit var repository: ImageRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        val fakeImageDao = FakeImageDao(
            listOf(
                LocalImage(
                    id = "00",
                    keyword = "카리나"
                ),
                LocalImage(
                    id = "01",
                    keyword = "미연"
                ),
                LocalImage(
                    id = "02",
                    keyword = "카리나"
                ),
                LocalImage(
                    id = "03",
                    keyword = "카리나"
                ),
            )
        )
        repository = FakeImageRepository(fakeImageDao)

        viewModel = BookmarkViewModel(repository)
    }

    @Test
    fun loadAllImages_whenScreenInit() {
        Assert.assertEquals(false, viewModel.uiState.value.isLoading)
        Assert.assertEquals(4, viewModel.uiState.value.result.size)
    }

    @Test
    fun loadFilteredImages_whenSearchKeywordImages() = runTest {
        viewModel.searchImages(keyword = "카리나")

        Assert.assertEquals(3, viewModel.uiState.value.result.size)
    }

    @Test
    fun deleteImages() = runTest {
        Assert.assertEquals(4, viewModel.uiState.value.result.size)

        viewModel.updateEditMode()
        viewModel.updateDeleteImages(id = "01")
        viewModel.updateDeleteImages(id = "02")
        viewModel.deleteImages()

        Assert.assertEquals(2, viewModel.uiState.value.result.size)
    }
}