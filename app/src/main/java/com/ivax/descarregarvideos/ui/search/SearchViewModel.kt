package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import com.ivax.descarregarvideos.responses.PlayerResponse
import domain.GetVideoDataUseCase
import domain.SearchVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    public val searchModel= MutableStateFlow<List<VideoItem>?>(null)
    public val isLoading = MutableStateFlow<Boolean>(false)
    public val adaptativeFormats = MutableStateFlow<List<AdaptiveFormats>?>(null)
    private val useCase = SearchVideosUseCase()
    private val getVideoUseCase=GetVideoDataUseCase()
    fun SearchVideos(searchQuery: String) {
        viewModelScope.launch {
            try {
                isLoading.value=true
                val result = useCase(searchQuery)
                searchModel.value = result
            }catch (e: Exception){

            }finally{
                isLoading.value=false
            }
        }
    }

    fun downloadVideoResponse(videoId: String) {
        this.viewModelScope.launch {
            try {
               var playerResponse: PlayerResponse= getVideoUseCase(videoId)
               var listFormats= playerResponse.streamingData.adaptiveFormats
                adaptativeFormats.value=listFormats
            } catch (e: Exception) {
                e.message?.let { Log.d("DescarregarVideos", it) }
            }
            Log.d("DescarregarVide", videoId)
        }
    }

    fun downloadVideoStream(url: String){
        this.viewModelScope.launch {

        }
    }

    val text: LiveData<String> = _text
}