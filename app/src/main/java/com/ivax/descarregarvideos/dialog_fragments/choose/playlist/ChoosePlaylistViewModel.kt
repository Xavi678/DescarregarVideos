package com.ivax.descarregarvideos.dialog_fragments.choose.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChoosePlaylistViewModel @Inject constructor(private val mediaRepository: VideoRepository): ViewModel() {
    val allPlaylists: LiveData<List<Playlist>> = mediaRepository.getAllPlaylists().asLiveData()

}