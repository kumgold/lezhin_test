package com.example.search_images.search

import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import com.example.search_images.MainActivity
import com.example.search_images.R
import com.example.search_images.ui.search.SearchScreen
import com.example.search_images.ui.search.SearchViewModel
import com.example.search_images.ui.theme.SearchImagesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var searchRepository: SearchRepository

    @Inject
    lateinit var imageRepository: ImageRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun displaySearchScreen_whenInit() {
        setContent()

        composeTestRule.onNodeWithText(activity.getString(R.string.please_search_images)).isDisplayed()
    }

    private fun setContent() {
        composeTestRule.activity.setContent {
            SearchImagesTheme {
                Surface {
                    SearchScreen(
                        viewModel = SearchViewModel(searchRepository, imageRepository)
                    )
                }
            }
        }
    }
}