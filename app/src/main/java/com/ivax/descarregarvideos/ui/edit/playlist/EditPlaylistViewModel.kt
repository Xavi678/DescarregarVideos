package com.ivax.descarregarvideos.ui.edit.playlist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val videoRepository: VideoRepository,
    private val mediaPlayerRepository: MediaPlayerRepository
) :
    ViewModel() {
    val isMediaVisible = mediaPlayerRepository.isMediaPlayerMaximized()

    private val _bottomSheetParameter = MutableStateFlow<String?>(null)
    val bottomSheetParameter = _bottomSheetParameter.asStateFlow()
    private val _playlist: MutableStateFlow<Playlist?> by lazy {
        MutableStateFlow(null)
    }
    private val _playlistIdWithPositions: MutableStateFlow<List<VideosWithPositionFoo>?> by lazy {
        MutableStateFlow(null)
    }
    private var playlistId: Int = savedStateHandle.get<Int>("playlistId")!!

    val playlistIdWithPositions = _playlistIdWithPositions.asStateFlow()
    val playlist get() = _playlist

    init {
        updatePlaylist(playlistId)
        Log.d("DescarregarVideos", playlistId.toString())
    }

    fun updatePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistIdWithPositions.update {
                videoRepository.getByPlaylistIdWithPositions(playlistId)
            }

            _playlist.update {
                videoRepository.firstPlaylist(playlistId)
            }


        }

    }

    fun UpdatePlaylistSavedVideoCrossRef(videosWithPositionFoo: VideosWithPositionFoo) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef? =
                videoRepository.getPlaylistSavedVideoCrossRefByFoo(videosWithPositionFoo)

            if (playlistSavedVideoCrossRef != null) {
                playlistSavedVideoCrossRef.position = videosWithPositionFoo.position
                videoRepository.UpdatePlaylistSavedVideoCrossRef(playlistSavedVideoCrossRef)
            }
        }
    }

    fun shuffle() {
        var playlistShuffle = _playlistIdWithPositions.value
        if (playlistShuffle != null) {
            mediaPlayerRepository.addPlaylistShuffle(playlistShuffle, playlist.value?.name)
        }

    }

    fun playAll() {
        val playlistPos = _playlistIdWithPositions.value?.sortedBy { it.position }
        if (playlistPos != null) {
            mediaPlayerRepository.addPlaylist(playlistPos, playlist.value?.name)
        }
    }

    fun setBottomSheetVideoId(videoId: String) {
        _bottomSheetParameter.value = videoId
    }

    fun resetSelectedVideo() {

        _bottomSheetParameter.value = null
        updatePlaylist(playlistId)
    }

    fun changePosition(
        currentItemKey: Any, currentItemIndex: Int, previousItemKey: Any,
        previousItemIndex: Int
    ) {
        viewModelScope.launch(context = Dispatchers.IO) {
            videoRepository.updatePosition(currentItemKey.toString(), previousItemIndex, playlistId)
            videoRepository.updatePosition(previousItemKey.toString(), currentItemIndex, playlistId)
            updatePlaylist(playlistId)
        }

    }

    fun detectChanges(playlistIdWithPositions: List<VideosWithPositionFoo>) {
        for (changed in playlistIdWithPositions) {
            val found =
                _playlistIdWithPositions.value?.firstOrNull { it.videoId == changed.videoId }
            if (found != null && changed.position!=found.position) {
                viewModelScope.launch(context = Dispatchers.IO) {
                    videoRepository.updatePosition(found.videoId,found.position,playlistId)
                }
            }
        }
    }


}