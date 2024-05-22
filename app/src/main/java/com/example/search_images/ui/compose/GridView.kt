package com.example.search_images.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.search_images.R

@Composable
fun LazyImageGridView(
    images: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin_small)),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin_small)),
        content = images
    )
}