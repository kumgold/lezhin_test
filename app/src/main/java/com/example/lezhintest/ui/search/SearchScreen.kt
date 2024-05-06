package com.example.lezhintest.ui.search

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.data.data.ImageResult
import com.example.lezhintest.R
import com.example.lezhintest.ui.compose.CircularLoading
import com.example.lezhintest.ui.compose.LezhinDefaultText
import com.example.lezhintest.ui.compose.SearchTextField
import com.example.lezhintest.ui.compose.TitleAppBar
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }

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
    }
}

@Composable
private fun SearchedImageGridView(
    images: LazyPagingItems<ImageResult>,
    insertImage: (ImageResult) -> Unit,
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
                LezhinDefaultText(
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
                                .height(screenWidth/2)
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
            LezhinDefaultText(
                modifier = Modifier.fillMaxWidth(),
                stringRes = R.string.error_message
            )
        }
    }
}