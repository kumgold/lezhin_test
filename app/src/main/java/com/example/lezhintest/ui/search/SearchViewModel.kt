package com.example.lezhintest.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.data.NetworkImage
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import com.example.lezhintest.R
import com.example.lezhintest.const.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _images = MutableStateFlow<PagingData<NetworkImage>>(PagingData.empty())
    val image: StateFlow<PagingData<NetworkImage>> = _images

    private val _userMessage = MutableStateFlow<Int?>(null)
    val userMessage: StateFlow<Int?> = _userMessage

    private val keyword = MutableLiveData<String>()

    fun searchImage(query: String) {
        if (query.isEmpty()) return

        keyword.value = query

        viewModelScope.launch {
            delay(Const.LOADING_DELAY_TIME)

            searchRepository.searchImages(query)
                .catch {
                    _userMessage.value = R.string.error_message
                }
                .cachedIn(viewModelScope)
                .collectLatest { result ->
                    _images.value = result
                }
        }
    }

    /**
     * 마음에 드는 이미지를 데이터베이스에 저장하는 함수.
     */
    fun insertImage(image: NetworkImage) {
        viewModelScope.launch {
            val insertImage = imageRepository.insertImage(
                keyword = keyword.value ?: "",
                image = image
            )

            if (insertImage.isFailure) {
                _userMessage.value = R.string.error_message
            }
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}