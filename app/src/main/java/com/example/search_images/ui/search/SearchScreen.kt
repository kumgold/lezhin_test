package com.example.search_images.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.data.data.NetworkImage
import com.example.search_images.R
import com.example.search_images.ui.compose.CircularLoading
import com.example.search_images.ui.compose.DefaultText
import com.example.search_images.ui.compose.SearchTextField
import com.example.search_images.ui.compose.TitleAppBar
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TitleAppBar(titleRes = R.string.search) },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->

        val items = viewModel.image.collectAsLazyPagingItems()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchTextField { query ->
                viewModel.searchImage(query)
            }
            SearchedImageGridView(
                images = items,
                insertImage = { image -> viewModel.insertImage(image) },
                loadState = items.loadState.refresh,
                snackBarHostState = snackBarHostState
            )
        }

        val message = viewModel.userMessage.collectAsState()
        val context = LocalContext.current
        LaunchedEffect(message.value) {
            if (message.value != null) {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = ContextCompat.getString(context, message.value!!),
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.clearUserMessage()
            }
        }
    }
}

@Composable
private fun SearchedImageGridView(
    images: LazyPagingItems<NetworkImage>,
    insertImage: (NetworkImage) -> Unit,
    loadState: LoadState,
    snackBarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    when (loadState) {
        is LoadState.Loading -> {
            CircularLoading(modifier = Modifier.fillMaxSize())
        }
        is LoadState.NotLoading -> {
            if (images.itemCount == 0) {
                DefaultText(
                    modifier = Modifier.fillMaxWidth(),
                    stringRes = R.string.please_search_images
                )
            } else {
                val context = LocalContext.current
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp

                LazyVerticalGrid(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin_small)),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small)),
                    state = rememberLazyGridState(initialFirstVisibleItemScrollOffset = 0)
                ) {
                    items(images.itemCount) { index ->
                        AsyncImage(
                            modifier = Modifier
                                .height(screenWidth / 2)
                                .clickable {
                                    coroutineScope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = ContextCompat.getString(
                                                context,
                                                R.string.save_image_message
                                            ),
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    images[index]?.let { insertImage(it) }
                                },
                            placeholder = painterResource(id = R.drawable.ic_android_black),
                            error = painterResource(id = R.drawable.ic_launcher_background),
                            model = images[index]?.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        is LoadState.Error -> {
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                stringRes = R.string.error_message
            )
        }
    }
}