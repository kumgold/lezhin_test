package com.example.search_images.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class ImageDetailUiState(
    val imageUrl: String
)

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val imageUrl: String = checkNotNull(savedStateHandle["imageUrl"])

    val uiState = MutableStateFlow(ImageDetailUiState(imageUrl))
}