package com.example.lezhintest.ui.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.data.db.ImageLocal
import com.example.lezhintest.R
import com.example.lezhintest.ui.compose.CircularLoading
import com.example.lezhintest.ui.compose.EditActionTitleAppBar
import com.example.lezhintest.ui.compose.LezhinDefaultText
import com.example.lezhintest.ui.compose.SearchTextField
import kotlinx.coroutines.launch

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            EditActionTitleAppBar(
                titleRes = R.string.bookmark,
                updateEditMode = { viewModel.updateEditMode() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BookmarkScreenHeader(
                snackBarHostState = snackBarHostState,
                searchImages = { keyword -> viewModel.searchImages(keyword) },
                deleteImages = { viewModel.deleteImages() },
                isEditMode = uiState.isEditMode
            )
            SavedImageGridView(
                images = uiState.result,
                isEditMode = uiState.isEditMode,
                updateDeleteImages = { id -> viewModel.updateDeleteImages(id) },
                isLoading = uiState.isLoading
            )
        }
    }
}

/**
 * 저장된 이미지를 편집할 때 나타나는 삭제 버튼
 */
@Composable
private fun BookmarkScreenHeader(
    snackBarHostState: SnackbarHostState,
    searchImages: (String) -> Unit,
    deleteImages: () -> Unit,
    isEditMode: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val isDelete = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isDelete.value) {
        if (isDelete.value) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = ContextCompat.getString(context, R.string.success_delete_images),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    if (isEditMode) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin_small))
        ) {
            TextButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    isDelete.value = true
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
    images: List<ImageLocal>,
    isEditMode: Boolean,
    updateDeleteImages: (String) -> Unit,
    isLoading: Boolean
) {
    when (isLoading) {
        true -> {
            CircularLoading(modifier = Modifier.fillMaxSize())
        }
        false -> {
            if (images.isEmpty()) {
                LezhinDefaultText(
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
                    items(images.size) { index ->
                        Box {
                            AsyncImage(
                                modifier = Modifier.height(screenWidth/2),
                                placeholder = painterResource(id = R.drawable.ic_android_black),
                                error = painterResource(id = R.drawable.ic_launcher_background),
                                model = images[index].imageUrl,
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
                                        updateDeleteImages(images[index].id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}