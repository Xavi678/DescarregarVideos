package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.SearchResponseFoo
import com.ivax.descarregarvideos.classes.VideoDownloadedData
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.FileRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import com.ivax.descarregarvideos.repository.YoutubeRepository
import com.ivax.descarregarvideos.responses.PlayerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val videoRepository: VideoRepository,
    private val youtubeRepository: YoutubeRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    //private val _currentVideos= MutableStateFlow( videoRepository.getAllVideos())
    public val searchModel = MutableStateFlow<SearchResponseFoo?>(null)
    public val isLoading = MutableStateFlow<Boolean>(false)
    public val videoDownloadedData = MutableStateFlow<VideoDownloadedData?>(null)
    val videoExists : MutableStateFlow<Boolean> by lazy {
        MutableStateFlow<Boolean>(false)
    }
    //private val useCase = SearchVideosUseCase()
    //private val getVideoUseCase = GetVideoDataUseCase()
    //private val downloadStreamUseCase = DownloadStreamUseCase()


    /*fun insertVideo(video: SavedVideo){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insetVideo(video)
            }
        }

    }*/
    fun hasVideo(videoId : String)  {
        viewModelScope.launch(Dispatchers.IO) {
            videoExists.update { videoRepository.videoExists(videoId) }
        }
    }
    fun SearchVideos(searchQuery: String, nextToken: String?=null) {
        viewModelScope.launch {
            try {
                isLoading.value = true

                val result = youtubeRepository.Search(searchQuery, nextToken)
                searchModel.update { result }
                //continuationToken=result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos",e.message.toString())
            } finally {
                isLoading.value = false
            }
        }
    }

    fun downloadVideoResponse(savedVideo: SavedVideo, finished: () -> Unit) {
        this.viewModelScope.launch(Dispatchers.IO) {

                var playerResponse: PlayerResponse =
                    youtubeRepository.GetVideoData(savedVideo.videoId)
                var listFormats = playerResponse.streamingData.adaptiveFormats
                var found = listFormats.firstOrNull { it.mimeType.contains("audio") }
                if (found != null) {

                    var bytes =
                        youtubeRepository.DownloadVideoStream("${found.url}&range=0-9898989")
                    var videoUrl = fileRepository.saveFile(savedVideo.videoId, bytes)
                    savedVideo.videoUrl = videoUrl
                    videoRepository.insetVideo(savedVideo)
                    finished()


                }
                //videoInfo.value=vi

                Log.d("DescarregarVide", savedVideo.videoId)
            /*}else{

            }*/
        }
    }

    val text: LiveData<String> = _text
    //val currentVideos : StateFlow<List<SavedVideo>> =_currentVideos
}