package com.example.search_images.search

import androidx.paging.map
import app.cash.turbine.test
import com.example.data.data.NetworkImage
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import com.example.search_images.MainCoroutineRule
import com.example.search_images.data.FakeImageDao
import com.example.search_images.data.FakeImageRepository
import com.example.search_images.data.FakeSearchRepository
import com.example.search_images.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel

    private lateinit var searchRepository: SearchRepository
    private lateinit var imageRepository: ImageRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        val fakeImageDao = FakeImageDao()
        imageRepository = FakeImageRepository(fakeImageDao)
        searchRepository = FakeSearchRepository(
            listOf(
                NetworkImage(
                    width = 100,
                    height = 100,
                    imageUrl = "image url"
                ),
                NetworkImage(
                    width = 100,
                    height = 100,
                    imageUrl = "image url"
                )
            )
        )

        viewModel = SearchViewModel(searchRepository, imageRepository)
    }

    @Test
    fun searchImages() = runTest {
        viewModel.searchImage("카리나")

        viewModel.images.test {
            val item = awaitItem()

            item.map {
                Assert.assertEquals("image", it.imageUrl)
            }
        }
    }

    @Test
    fun insertImage() = runTest {
        viewModel.insertImage(
            NetworkImage(
                imageUrl = "test image url",
                width = 0,
                height = 0
            )
        )

        Assert.assertEquals("test image url", imageRepository.getAllImages().first()[0].imageUrl)
    }
}