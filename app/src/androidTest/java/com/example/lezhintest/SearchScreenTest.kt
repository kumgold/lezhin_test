package com.example.lezhintest

import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import com.example.lezhintest.ui.search.SearchScreen
import com.example.lezhintest.ui.search.SearchViewModel
import com.example.lezhintest.ui.theme.LezhinTestTheme
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

    @Inject
    lateinit var searchRepository: SearchRepository

    @Inject
    lateinit var imageRepository: ImageRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun displaySearchScreen_whenInit() = runTest {
        setContent()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.please_search_images)).isDisplayed()
    }

    private fun setContent() {
        composeTestRule.activity.setContent {
            LezhinTestTheme {
                Surface {
                    SearchScreen(
                        viewModel = SearchViewModel(searchRepository, imageRepository)
                    )
                }
            }
        }
    }
}