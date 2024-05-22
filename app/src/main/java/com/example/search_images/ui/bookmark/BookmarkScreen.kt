package com.example.search_images.ui.bookmark

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.db.LocalImage
import com.example.search_images.R
import com.example.search_images.ui.compose.CircularLoading
import com.example.search_images.ui.compose.DefaultText
import com.example.search_images.ui.compose.EditImagesAppBar
import com.example.search_images.ui.compose.LazyImageGridView
import com.example.search_images.ui.compose.SearchTextField
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    goToDetailScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        EditImagesAppBar(
            titleRes = R.string.bookmark,
            updateEditMode = { viewModel.updateEditMode() },
            deleteImages = { viewModel.deleteImages() },
            isEditMode = uiState.isEditMode
        )
        SearchTextField { keyword ->
            viewModel.stopEditMode()
            viewModel.searchImages(keyword)
        }
        SavedImageGridView(
            images = uiState.result,
            isEditMode = uiState.isEditMode,
            deleteImages = { id -> viewModel.updateDeleteImages(id) },
            isLoading = uiState.isLoading,
            goToDetailScreen = { imageUrl -> goToDetailScreen(imageUrl) },
            updateEditMode = { viewModel.updateEditMode() }
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(uiState.isDeleteImages) {
        if (uiState.isDeleteImages) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = ContextCompat.getString(context, R.string.success_delete_images),
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.updateEditMode()
        }
    }

    LaunchedEffect(uiState.userMessage) {
        if (uiState.userMessage != null) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = ContextCompat.getString(context, uiState.userMessage!!),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

/**
 * 저장된 이미지를 보여주는 Lazy Grid View.
 * 저장된 이미지를 편집할 수 있는 기능이 추가되어 있다. (이미지 삭제 기능)
 */
@Composable
private fun SavedImageGridView(
    images: List<LocalImage>,
    isEditMode: Boolean,
    deleteImages: (String) -> Unit,
    isLoading: Boolean,
    goToDetailScreen: (String) -> Unit,
    updateEditMode: () -> Unit
) {
    when (isLoading) {
        true -> {
            CircularLoading(modifier = Modifier.fillMaxSize())
        }
        false -> {
            if (images.isEmpty()) {
                DefaultText(
                    modifier = Modifier.fillMaxWidth(),
                    stringRes = R.string.save_your_images
                )
            } else {
                val configuration = LocalConfiguration.current

                LazyImageGridView {
                    val screenWidth = configuration.screenWidthDp.dp

                    items(images) { image ->
                        SavedImageItem(
                            imageHeight = screenWidth/2,
                            imageUrl = image.imageUrl,
                            imageId = image.id,
                            isEditMode = isEditMode,
                            deleteImages = { id -> deleteImages(id) },
                            goToDetailScreen = { imageUrl -> goToDetailScreen(imageUrl) },
                            updateEditMode = { updateEditMode() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SavedImageItem(
    imageHeight: Dp,
    imageUrl: String,
    imageId: String,
    isEditMode: Boolean,
    deleteImages: (String) -> Unit,
    goToDetailScreen: (String) -> Unit,
    updateEditMode: () -> Unit
) {
    Box {
        AsyncImage(
            modifier = Modifier.height(imageHeight)
                .combinedClickable(
                    onLongClick = {
                        updateEditMode()
                    }
                ) {
                    if (!isEditMode) {
                        val url = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                        goToDetailScreen(url)
                    }
                },
            placeholder = painterResource(id = R.drawable.ic_android_black),
            error = painterResource(id = R.drawable.ic_launcher_background),
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        if (isEditMode) {
            val isChecked = rememberSaveable { mutableStateOf(false) }

            Checkbox(
                modifier = Modifier.padding(2.dp),
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = !isChecked.value
                    deleteImages(imageId)
                }
            )
        }
    }
}