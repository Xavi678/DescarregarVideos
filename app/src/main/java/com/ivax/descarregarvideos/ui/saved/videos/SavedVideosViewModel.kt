package com.ivax.descarregarvideos.ui.saved.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.UIRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SavedVideosViewModel @Inject
constructor(
    private val repository: VideoRepository,
    private val mediaPlayerRepository: MediaPlayerRepository,
    private val uiRepository: UIRepository
) : ViewModel() {

    val _filterFlow = MutableStateFlow<String?>(null)
    val filterFlow = _filterFlow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    var allSavedVideos = filterFlow.flatMapLatest {
        if (it.isNullOrEmpty()) {
            repository.getAllVideos()
        } else {
            repository.getAllVideos(it.toString())
        }
    }

    private val _isBottomSheetVisible = MutableStateFlow<Boolean>(false)
    private val _bottomSheetParameter = MutableStateFlow<String?>(null)
    private val _showPlaylistMenu=MutableStateFlow<Boolean>(false)
    private val _selectedVideoId=MutableStateFlow<String?>(null)

    val bottomSheetParameter = _bottomSheetParameter.asStateFlow()
    val showPlaylistMenu=_showPlaylistMenu.asStateFlow()
    val selectedVideoId=_selectedVideoId.asStateFlow()
    fun addSingleItemMedia(savedVideo: SavedVideo) {
        mediaPlayerRepository.clear()
        val mediaItem = mediaPlayerRepository.SavedVideoToMediaItem(savedVideo)
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play() {
        mediaPlayerRepository.play()
    }

    fun setSavedVideo(savedVideo: SavedVideo) {
        mediaPlayerRepository.setSavedVideo(savedVideo)
    }

    fun setMediaVisibility(visibility: Boolean) {
        mediaPlayerRepository.isMediaPlayerMaximized().value=visibility
    }

    fun filterSavedVideos(savedVideoName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (savedVideoName != "") {
                _filterFlow.update {
                    savedVideoName
                }

            } else {
                _filterFlow.update {
                    null
                }
            }
        }
    }

    fun deleteVideo(videoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVideo(videoId)
        }

    }

    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    fun setBottomSheetVisibility(state: Boolean) {
        _isBottomSheetVisible.value = state
    }

    fun setBottomSheetVideoId(videoId: String) {
        _bottomSheetParameter.value = videoId
    }

    fun showPlaylistMenu(showMenu: Boolean){
        _showPlaylistMenu.value=showMenu
    }
    fun setSelectedVideoId(videoId : String){
        _selectedVideoId.value=videoId
    }

    fun resetSelectedVideo() {
        _bottomSheetParameter.value=null
    }
}