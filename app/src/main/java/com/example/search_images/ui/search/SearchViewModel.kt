package com.example.search_images.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.data.NetworkImage
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import com.example.search_images.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val images: List<NetworkImage> = emptyList(),
    val keyword: String = "",
    val page: Int = 1,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun searchImage(query: String) {
        if (query.isEmpty()) return

        if (_uiState.value.keyword != query) {
            _uiState.update {
                it.copy(
                    images = emptyList(),
                    page = 1
                )
            }
        }

        viewModelScope.launch {
            try {
                val images = searchRepository.searchImages(query, _uiState.value.page)

                _uiState.update {
                    it.copy(
                        images = it.images + images,
                        keyword = query,
                        page = it.page + 1,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        page = 1,
                        isLoading = false,
                        userMessage = R.string.error_message
                    )
                }
            }
        }
    }

    /**
     * 마음에 드는 이미지를 데이터베이스에 저장하는 함수.
     */
    fun insertImage(image: NetworkImage) {
        viewModelScope.launch {
            try {
                imageRepository.insertImage(
                    keyword = _uiState.value.keyword,
                    image = image
                )
                _uiState.update { it.copy(userMessage = R.string.save_image_message) }
            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = R.string.error_message) }
            }
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}