package com.ivax.descarregarvideos.ui.search

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.net.toUri
import com.ivax.descarregarvideos.classes.DownloadState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val fileRepository: FileRepository,
    private val videoRepository: VideoRepository,
    private val youtubeRepository: YoutubeRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    private val _formats = MutableStateFlow<List<AdaptiveFormats>>(emptyList())
    val formats = _formats.asStateFlow()
    private val _videos = MutableStateFlow<SnapshotStateList<VideoItem>>(mutableStateListOf())
    val continuationToken = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    val videos = _videos.asStateFlow()
    val videoExists: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow<Boolean>(false)
    }
    private val _currentVideo = MutableStateFlow<VideoItem?>(null)

    val currentVideo = _currentVideo.asStateFlow()
    var searchQuery: String? = null
    fun hasVideo(videoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            videoExists.update { videoRepository.videoExists(videoId) }
        }
    }

    fun SearchVideos(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                this@SearchViewModel.searchQuery = searchQuery
                val result = youtubeRepository.Search(searchQuery)
                result.videos.forEach {
                    it.videoDownloaded = IsDownloaded(it.videoId)
                }
                _videos.update {
                    it.clear()
                    it.addAll(result.videos)
                    it
                    //result.videos
                }
                continuationToken.value = result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val result =
                    youtubeRepository.SearchMore(token = continuationToken.value.toString())


                _videos.update {
                    stateList ->
                    result.videos.forEach {
                        if(stateList.firstOrNull { x -> x.videoId==it.videoId }==null) {
                            it.videoDownloaded = IsDownloaded(it.videoId)
                            stateList.add(it)
                        }
                    }
                    stateList
                }
                continuationToken.value = result.nextToken
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun IsDownloaded(videoId: String) =
        if (videoRepository.videoExists(videoId)) {
            DownloadState.Downloaded
        } else {
            DownloadState.NotDownloaded
        }

    fun getAudioUrlsResponse(savedVideo: VideoItem, callback: (List<AdaptiveFormats>) -> Unit) {
        this.viewModelScope.launch(Dispatchers.IO) {

            val playerResponse: PlayerResponse =
                youtubeRepository.GetVideoData(savedVideo.videoId)
            val listFormats = playerResponse.streamingData.adaptiveFormats
            val lists = listFormats.filter { it.mimeType.contains("audio/mp4") }
            if (lists.isNotEmpty()) {
                callback(lists)
            }

            Log.d("DescarregarVide", savedVideo.videoId)
        }
    }

    fun downloadVideo(
        selectedFormat: AdaptiveFormats,
        video: VideoItem,
        finished: (success: Boolean,errorMessage: String?) -> Unit
    ) {
        this.viewModelScope.launch(Dispatchers.IO) {
            val savedVideo =toSavedVideo(video)
            try {
                val uri = selectedFormat.url.toUri()
                val segmentLength = if (uri.getQueryParameter("ratebypass") == "yes") 9898989L
                else {
                    if (selectedFormat.contentLength == null) {
                        uri.getQueryParameter("clen")!!.toLong()
                    } else {
                        selectedFormat.contentLength.toLong()
                    }

                }
                val bytes =
                    youtubeRepository.DownloadVideoStream("${selectedFormat.url}&range=0-${segmentLength}")
                val videoUrl = fileRepository.saveFile(savedVideo.videoId, bytes)
                savedVideo.videoUrl = videoUrl
                videoRepository.insetVideo(savedVideo)
                finished(true,null)
            }catch (e: Exception){
                Log.d("DescarregarVideos","Error al descarregar el video")
                finished(false,"No s'ha pogut descarregar el video")
            }

        }

    }

    fun toSavedVideo(video: VideoItem) : SavedVideo{

        val imgPath = "${video.videoId}_thumbnail.bmp"

        var dir = File("${context.filesDir}/fotos")
        var d = dir.mkdir()
        var f = File("${dir}/${imgPath}")

        if (f.exists()) {
            f.delete()
        }
        f.createNewFile()
        f.outputStream().use {
            video.imgUrl?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return SavedVideo(
            video.videoId,
            video.title,
            imgUrl = "${dir}/${imgPath}",
            video.duration,
            video.viewCount,
            author = video.author
        )
    }
    fun setFormats(currentVideo: VideoItem, formats: List<AdaptiveFormats>) {
        _currentVideo.value = currentVideo
        _formats.value = formats
    }

    fun setDownloaded(currentVideo: VideoItem) {
        setDownloadStatus(currentVideo, DownloadState.Downloaded)
    }

    fun resetDialog() {
        //_videos.value = emptyList()
        _currentVideo.value = null
    }

    fun setDownloading(currentVideo: VideoItem) {
        setDownloadStatus(currentVideo, DownloadState.Downloading)
    }

    private fun setDownloadStatus(currentVideo: VideoItem, downloadState: DownloadState) {
        _videos.update {
            val idx=it.indexOfFirst { it.videoId == currentVideo.videoId }
            if(idx!=-1) {
                it[idx] = it[idx].copy(videoDownloaded = downloadState)
            }
            it
        }

    }

    fun setNotDownloaded(currentVideo: VideoItem) {

        setDownloadStatus(currentVideo,DownloadState.NotDownloaded)
    }

    fun resetFormats() {
        _formats.value= emptyList()
    }

}