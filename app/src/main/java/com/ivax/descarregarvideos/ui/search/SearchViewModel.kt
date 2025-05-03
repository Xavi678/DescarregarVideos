package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    //public val searchResponseFoo = MutableStateFlow<SearchResponseFoo?>(null)
    private val _videos = MutableStateFlow<List<VideoItem>>(emptyList())
    val continuationToken = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading=_isLoading.asStateFlow()
    val videos=_videos.asStateFlow()
    val videoExists : MutableStateFlow<Boolean> by lazy {
        MutableStateFlow<Boolean>(false)
    }
    var searchQuery : String?=null
    fun hasVideo(videoId : String)  {
        viewModelScope.launch(Dispatchers.IO) {
            videoExists.update { videoRepository.videoExists(videoId) }
        }
    }
    fun SearchVideos(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                this@SearchViewModel.searchQuery=searchQuery
                val result = youtubeRepository.Search(searchQuery)
                result.videos.forEach {
                    it.videoDownloaded=videoRepository.videoExists(it.videoId)
                }
                _videos.update { result.videos }
                continuationToken.value= result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos",e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadMoreVideos(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val result = youtubeRepository.SearchMore(token  = continuationToken.value.toString())
                result.videos.forEach {
                    it.videoDownloaded=videoRepository.videoExists(it.videoId)
                }
                //videos.value.addAll(result.videos)


                _videos.value+=result.videos
                //_videos.value+=result.videos
                //videos.emit(result.videos)
                continuationToken.value= result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos",e.message.toString())
            } finally {
                _isLoading.value = false
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

                Log.d("DescarregarVide", savedVideo.videoId)
        }
    }

    val text: LiveData<String> = _text
}