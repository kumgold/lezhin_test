package com.example.search_images.bookmark

import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.data.NetworkImage
import com.example.data.repository.ImageRepository
import com.example.search_images.MainActivity
import com.example.search_images.R
import com.example.search_images.ui.bookmark.BookmarkScreen
import com.example.search_images.ui.bookmark.BookmarkViewModel
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
class BookmarkScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: ImageRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun bookmarkScreen_whenEditButtonClick() {
        setContent()

        composeTestRule.onNodeWithText(activity.getString(R.string.edit)).isDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.edit)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.edit)).isNotDisplayed()

        composeTestRule.onNodeWithText(activity.getString(R.string.delete)).isDisplayed()
    }

    private fun setContent() {
        activity.setContent {
            SearchImagesTheme {
                Surface {
                    BookmarkScreen(
                        viewModel = BookmarkViewModel(repository)
                    )
                }
            }
        }
    }
}