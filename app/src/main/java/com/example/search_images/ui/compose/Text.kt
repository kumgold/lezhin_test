package com.example.search_images.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DefaultText(
    modifier: Modifier,
    @StringRes stringRes: Int
) {
    Text(
        modifier = modifier,
        text = stringResource(id = stringRes),
        textAlign = TextAlign.Center
    )
}