package com.example.search_images.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.LocalImage
import com.example.data.repository.ImageRepository
import com.example.search_images.R
import com.example.search_images.const.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkUiState(
    val isLoading: Boolean = false,
    val result: List<LocalImage> = emptyList(),
    val isEditMode: Boolean = false,
    val isDeleteImages: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState

    private val _images = mutableListOf<String>()

    init {
        getAllImages()
    }

    fun getAllImages() {
        viewModelScope.launch {
            loading()
            imageRepository.getAllImages().collectLatest { images ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        result = images
                    )
                }
            }
        }
    }

    /**
     * 이미지 편집을 실행하거나 그만두기 위한 EditMode on/off 함수.
     */
    fun updateEditMode() {
        _uiState.update {
            it.copy(
                isEditMode = !it.isEditMode,
                isDeleteImages = false
            )
        }
        _images.clear()
    }

    /**
     * 삭제를 위해 체크박스가 선택된 이미지를 추가하거나, 체크박스가 해제된 이미지를 제거한다.
     */
    fun updateDeleteImages(id: String) {
        if (_images.contains(id)) {
            _images.remove(id)
        } else {
            _images.add(id)
        }
    }

    fun searchImages(keyword: String) {
        viewModelScope.launch {
            loading()
            imageRepository.searchImages(keyword).collectLatest { images ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        result = images
                    )
                }
            }
        }
    }

    private suspend fun loading() {
        _uiState.update { it.copy(isLoading = true) }
        delay(Const.LOADING_DELAY_TIME)
    }

    fun deleteImages() {
        if (_images.isNotEmpty()) {
            viewModelScope.launch {
                val deleteImage = imageRepository.deleteImages(_images)

                if (deleteImage.isSuccess) {
                    _uiState.update {
                        it.copy(isDeleteImages = deleteImage.isSuccess)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isDeleteImages = deleteImage.isSuccess,
                            userMessage = R.string.error_message
                        )
                    }
                }
            }
        }
    }
}