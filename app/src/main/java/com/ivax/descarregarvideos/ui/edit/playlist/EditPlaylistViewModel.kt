package com.ivax.descarregarvideos.ui.edit.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository) :
    ViewModel() {
    /*private val _playlistWithSavedVideos: MutableStateFlow<List<PlaylistWithSavedVideos>?> by lazy {
        MutableStateFlow<List<PlaylistWithSavedVideos>?>(null)
    }*/
    private val _playlistIdWithPositions: MutableStateFlow<List<VideosWithPositionFoo>?> by lazy {
        MutableStateFlow<List<VideosWithPositionFoo>?>(null)
    }

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistIdWithPositions.update {
                videoRepository.getByPlaylistIdWithPositions(playlistId)
            }
        }

    }

    fun UpdatePlaylistSavedVideoCrossRef(videosWithPositionFoo: VideosWithPositionFoo) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef? =videoRepository.getPlaylistSavedVideoCrossRefByFoo(videosWithPositionFoo)
            if(playlistSavedVideoCrossRef!=null) {
                videoRepository.UpdatePlaylistSavedVideoCrossRef(playlistSavedVideoCrossRef)
            }
        }
    }

    //val playlistWithSavedVideos get() = _playlistWithSavedVideos
    val playlistIdWithPositions get() = _playlistIdWithPositions
}