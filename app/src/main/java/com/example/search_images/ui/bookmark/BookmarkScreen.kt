package com.example.search_images.ui.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.search_images.ui.compose.EditActionTitleAppBar
import com.example.search_images.ui.compose.SearchTextField
import kotlinx.coroutines.launch

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        EditActionTitleAppBar(
            titleRes = R.string.bookmark,
            updateEditMode = { viewModel.updateEditMode() }
        )
        BookmarkScreenHeader(
            searchImages = { keyword -> viewModel.searchImages(keyword) },
            deleteImages = { viewModel.deleteImages() },
            isEditMode = uiState.isEditMode,
        )
        SavedImageGridView(
            images = uiState.result,
            isEditMode = uiState.isEditMode,
            deleteImages = { id -> viewModel.updateDeleteImages(id) },
            isLoading = uiState.isLoading
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
 * 저장된 이미지를 편집할 때 나타나는 삭제 버튼
 */
@Composable
private fun BookmarkScreenHeader(
    searchImages: (String) -> Unit,
    deleteImages: () -> Unit,
    isEditMode: Boolean,
) {
    if (isEditMode) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin_small))
        ) {
            TextButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    deleteImages()
                }
            ) {
                Text(stringResource(id = R.string.delete))
            }
        }
    } else {
        SearchTextField { keyword ->
            searchImages(keyword)
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
    isLoading: Boolean
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
                val screenWidth = configuration.screenWidthDp.dp

                LazyVerticalGrid(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin_small)),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small))
                ) {
                    items(images) { image ->
                        SavedImageItem(
                            imageHeight = screenWidth/2,
                            imageUrl = image.imageUrl,
                            imageId = image.id,
                            isEditMode = isEditMode,
                            deleteImages = { id -> deleteImages(id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedImageItem(
    imageHeight: Dp,
    imageUrl: String,
    imageId: String,
    isEditMode: Boolean,
    deleteImages: (String) -> Unit
) {
    Box {
        AsyncImage(
            modifier = Modifier.height(imageHeight),
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