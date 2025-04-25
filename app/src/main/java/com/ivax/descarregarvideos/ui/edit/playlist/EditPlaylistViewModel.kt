package com.ivax.descarregarvideos.ui.edit.playlist

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository,
private val mediaPlayerRepository: MediaPlayerRepository) :
    ViewModel() {
    val isMediaVisible=mediaPlayerRepository.isMediaPlayerMaximized()
    /*private val _playlistWithSavedVideos: MutableStateFlow<List<PlaylistWithSavedVideos>?> by lazy {
        MutableStateFlow<List<PlaylistWithSavedVideos>?>(null)
    }*/
    private val _playlist: MutableStateFlow<Playlist?> by lazy {
        MutableStateFlow(null)
    }
    private val _playlistIdWithPositions: MutableStateFlow<List<VideosWithPositionFoo>?> by lazy {
        MutableStateFlow<List<VideosWithPositionFoo>?>(null)
    }

    fun getPlaylist(playlistId: Int) {
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
       var playlistShuffle= _playlistIdWithPositions.value
        if(playlistShuffle!=null){
            mediaPlayerRepository.addPlaylistShuffle(playlistShuffle,playlist.value?.name)
        }

    }
    fun playAll() {
        val playlistPos=_playlistIdWithPositions.value?.sortedBy { it.position }
        if(playlistPos!=null){
            mediaPlayerRepository.addPlaylist(playlistPos,playlist.value?.name)
        }
    }

    //val playlistWithSavedVideos get() = _playlistWithSavedVideos
    val playlistIdWithPositions get() = _playlistIdWithPositions
    val playlist get() = _playlist
}