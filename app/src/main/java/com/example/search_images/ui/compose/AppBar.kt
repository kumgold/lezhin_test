package com.example.search_images.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
fun EditImagesAppBar(
    @StringRes titleRes: Int,
    updateEditMode: () -> Unit,
    deleteImages: () -> Unit,
    isEditMode: Boolean,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = titleRes))
        },
        actions = {
            if (isEditMode) {
                Row {
                    TextButton(
                        onClick = {
                            deleteImages()
                        }
                    ) {
                        Text(stringResource(id = R.string.delete))
                    }
                    TextButton(
                        onClick = {
                            updateEditMode()
                        }
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                }
            } else {
                TextButton(
                    onClick = {
                        updateEditMode()
                    }
                ) {
                    Text(stringResource(id = R.string.edit))
                }
            }
        }
    )
}

@Preview
@Composable
private fun EditImagesAppBarPreview() {
    EditImagesAppBar(
        titleRes = R.string.bookmark,
        updateEditMode = { },
        deleteImages = { },
        isEditMode = true
    )
}