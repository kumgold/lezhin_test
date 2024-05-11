package com.example.search_images.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.search_images.R

/**
 * 오직 Title만 표시하는 Title App Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAppBar(
    @StringRes titleRes: Int
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = titleRes))
        }
    )
}

/**
 * 편집 모드가 포함된 Title App Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActionTitleAppBar(
    @StringRes titleRes: Int,
    updateEditMode: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.bookmark))
        },
        actions = {
            TextButton(
                onClick = {
                    updateEditMode()
                }
            ) {
                Text(stringResource(id = R.string.edit))
            }
        }
    )
}