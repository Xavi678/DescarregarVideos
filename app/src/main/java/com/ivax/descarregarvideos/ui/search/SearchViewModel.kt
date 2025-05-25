package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.FileRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import com.ivax.descarregarvideos.repository.YoutubeRepository
import com.ivax.descarregarvideos.responses.AdaptiveFormats
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
    private val _formats= MutableStateFlow<List<AdaptiveFormats>>(emptyList())
    val formats=_formats.asStateFlow()
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

                _videos.value+=result.videos
                continuationToken.value= result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos",e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAudioUrlsResponse(savedVideo: SavedVideo, callback: (List<AdaptiveFormats>) -> Unit) {
        this.viewModelScope.launch(Dispatchers.IO) {

                val playerResponse: PlayerResponse =
                    youtubeRepository.GetVideoData(savedVideo.videoId)
                val listFormats = playerResponse.streamingData.adaptiveFormats
                val lists = listFormats.filter { it.mimeType.contains("audio") }
                if(lists.isNotEmpty()){
                    callback(lists)
                }
                /*if (found != null) {

                    var bytes =
                        youtubeRepository.DownloadVideoStream("${found.url}&range=0-9898989")
                    var videoUrl = fileRepository.saveFile(savedVideo.videoId, bytes)
                    savedVideo.videoUrl = videoUrl
                    videoRepository.insetVideo(savedVideo)
                    finished()


                }*/

                Log.d("DescarregarVide", savedVideo.videoId)
        }
    }

    fun downloadVideo(selectedUrl: String) {
        var bytes =
            youtubeRepository.DownloadVideoStream("${found.url}&range=0-9898989")
        var videoUrl = fileRepository.saveFile(savedVideo.videoId, bytes)
        savedVideo.videoUrl = videoUrl
        videoRepository.insetVideo(savedVideo)
        //finished()
    }

    val text: LiveData<String> = _text
}