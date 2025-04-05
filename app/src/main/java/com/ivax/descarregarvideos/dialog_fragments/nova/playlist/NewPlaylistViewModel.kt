package com.ivax.descarregarvideos.dialog_fragments.nova.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewPlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    private val _insertedId = MutableLiveData<Int?>().apply {
        value = null
    }

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val res=videoRepository.addPlaylist(playlist)
            _insertedId.postValue(res)

        }
    }
    val insertedId: LiveData<Int?> = _insertedId
}