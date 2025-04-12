package com.ivax.descarregarvideos.ui.edit.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    private val _playlistWithSavedVideos : MutableStateFlow<PlaylistWithSavedVideos?> by lazy {
        MutableStateFlow<PlaylistWithSavedVideos?>(null)
    }

    fun getPlaylist(playlistId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _playlistWithSavedVideos.update {
                videoRepository.firstPlaylistWithSavedVideos(playlistId)
            }
        }

    }

    val playlistWithSavedVideos get()=_playlistWithSavedVideos
}