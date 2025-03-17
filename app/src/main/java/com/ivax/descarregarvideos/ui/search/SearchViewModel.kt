package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import com.ivax.descarregarvideos.responses.PlayerResponse
import domain.DownloadStreamUseCase
import domain.GetVideoDataUseCase
import domain.SearchVideosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    public val searchModel= MutableStateFlow<List<VideoItem>?>(null)
    public val isLoading = MutableStateFlow<Boolean>(false)
    val adaptativeFormats = MutableLiveData<List<AdaptiveFormats>?>(null)
    public val downloadedFile = MutableStateFlow<File?>(null)

    private val useCase = SearchVideosUseCase()
    private val getVideoUseCase=GetVideoDataUseCase()
    private val downloadStreamUseCase= DownloadStreamUseCase()
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

               var playerResponse: PlayerResponse= getVideoUseCase(videoId)
               var listFormats= playerResponse.streamingData.adaptiveFormats
                adaptativeFormats.value=listFormats

            Log.d("DescarregarVide", videoId)
        }
    }

    fun downloadVideoStream(url: String?, file: File) {
        this.viewModelScope.launch {
            if(url!=null) {
                downloadStreamUseCase(url,file)
                downloadedFile.value =file
            }
        }

    }

    val text: LiveData<String> = _text
}