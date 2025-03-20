package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.ivax.descarregarvideos.MyApplication
import com.ivax.descarregarvideos.classes.VideoDownloadedData
import com.ivax.descarregarvideos.classes.VideoInfo
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.FileRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import com.ivax.descarregarvideos.responses.PlayerResponse
import domain.DownloadStreamUseCase
import domain.GetVideoDataUseCase
import domain.SearchVideosUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val fileRepository: FileRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    public val searchModel = MutableStateFlow<List<VideoItem>?>(null)
    public val isLoading = MutableStateFlow<Boolean>(false)
    val videoInfo = MutableLiveData<VideoInfo>(null)
    public val videoDownloadedData = MutableStateFlow<VideoDownloadedData?>(null)

    private val useCase = SearchVideosUseCase()
    private val getVideoUseCase = GetVideoDataUseCase()
    private val downloadStreamUseCase = DownloadStreamUseCase()

    init{

    }

    /*fun insertVideo(video: SavedVideo){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insetVideo(video)
            }
        }

    }*/
    fun SearchVideos(searchQuery: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val result = useCase(searchQuery)
                searchModel.value = result
            } catch (e: Exception) {

            } finally {
                isLoading.value = false
            }
        }
    }

    fun downloadVideoResponse(videoId: String) {
        this.viewModelScope.launch {

            var playerResponse: PlayerResponse = getVideoUseCase(videoId)
            var listFormats = playerResponse.streamingData.adaptiveFormats
            val vi = VideoInfo(adaptativeFormats = listFormats, videoId = videoId)
            var found=listFormats.firstOrNull{ it.mimeType.contains("audio") }
            if(found!=null) {
                withContext(Dispatchers.IO) {
                    var bytes=downloadStreamUseCase(found.url)
                    fileRepository.saveFile(bytes)
                }
            }
            //videoInfo.value=vi

                Log.d("DescarregarVide", videoId)
        }
    }

    fun downloadVideoStream(videoId: String,url: String?) {
        this.viewModelScope.launch {
            if (url != null) {
                var resultArray=downloadStreamUseCase(url)
                val vid=VideoDownloadedData(resultArray,videoId)
                videoDownloadedData.value =vid
                // =file
            }
        }

    }

    val text: LiveData<String> = _text

    /*companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return SearchViewModel(
                    (application as MyApplication).videoRepository
                ) as T
            }
        }
    }*/
}