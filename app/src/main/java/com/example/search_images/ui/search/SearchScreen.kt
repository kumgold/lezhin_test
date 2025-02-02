package com.example.search_images.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.data.NetworkImage
import com.example.search_images.R
import com.example.search_images.ui.compose.DefaultText
import com.example.search_images.ui.compose.LazyImageGridView
import com.example.search_images.ui.compose.SearchTextField
import com.example.search_images.ui.compose.TitleAppBar
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    goToDetailScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TitleAppBar(titleRes = R.string.search)
        SearchTextField { query ->
            viewModel.searchImage(query)
        }
        SearchedImageGridView(
            images = uiState.images,
            insertImage = { image -> viewModel.insertImage(image) },
            goToDetailScreen = { imageUrl -> goToDetailScreen(imageUrl) },
            searchImages = { query -> viewModel.searchImage(query) },
            keyword = uiState.keyword
        )
    }

    uiState.userMessage?.let { message ->
        val context = LocalContext.current
        LaunchedEffect(message) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = ContextCompat.getString(context, message),
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearUserMessage()
        }
    }
}

@Composable
private fun SearchedImageGridView(
    images: List<NetworkImage>,
    insertImage: (NetworkImage) -> Unit,
    goToDetailScreen: (String) -> Unit,
    searchImages: (String) -> Unit,
    keyword: String,
) {
    if (images.isNotEmpty()) {
        LazyImageGridView {
            items(images) { image ->
                ImageItem(
                    image = image,
                    insertImage = insertImage,
                    goToDetailScreen = goToDetailScreen
                )
            }
            if (images.isNotEmpty()) {
                item(span = { GridItemSpan(this.maxLineSpan) }) {
                    TextButton(
                        onClick = { searchImages(keyword) }
                    ) {
                        Text(text = "More")
                    }
                }
            }
        }
    } else {
        DefaultText(
            modifier = Modifier.fillMaxWidth(),
            stringRes = R.string.please_search_images
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageItem(
    image: NetworkImage,
    insertImage: (NetworkImage) -> Unit,
    goToDetailScreen: (String) -> Unit,
) {
    AsyncImage(
        modifier = Modifier
            .aspectRatio(1.0f)
            .combinedClickable(
                onLongClick = {
                    insertImage(image)
                }
            ) {
                val imageUrl = URLEncoder.encode(
                    image.imageUrl,
                    StandardCharsets.UTF_8.toString()
                )

                goToDetailScreen(imageUrl)
            },
        placeholder = painterResource(id = R.drawable.ic_android_black),
        error = painterResource(id = R.drawable.ic_launcher_background),
        model = image.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}