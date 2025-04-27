package com.ivax.descarregarvideos.ui.saved.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedVideosViewModel @Inject constructor(private val repository: VideoRepository, private val mediaPlayerRepository: MediaPlayerRepository) : ViewModel() {
    val allSavedVideos: LiveData<List<SavedVideo>> = repository.getAllVideos().asLiveData()

    private val _isBottomSheetVisible= MutableStateFlow<Boolean>(false)
    private val _bottomSheetParameter= MutableStateFlow<String?>(null)
    val bottomSheetParameter=_bottomSheetParameter.asStateFlow()
    fun addSingleItemMedia(savedVideo: SavedVideo){
        mediaPlayerRepository.clear()
        val mediaItem=mediaPlayerRepository.SavedVideoToMediaItem(savedVideo)
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play(){
        mediaPlayerRepository.play()
    }

    fun setSavedVideo(savedVideo: SavedVideo){
        mediaPlayerRepository.setSavedVideo(savedVideo)
    }

    fun setMediaVisibility(visibility: Boolean){
        mediaPlayerRepository.isMediaPlayerMaximized().postValue(visibility)
    }

    fun filterSavedVideos(savedVideoName: String): List<SavedVideo> {
        if(savedVideoName==""){
            return  allSavedVideos.value!!
        }
        return allSavedVideos.value!!.filter { it.title.lowercase().contains(savedVideoName.lowercase()) }
    }
    fun deleteVideo(videoId: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVideo(videoId)
        }

    }
    val isBottomSheetVisible : StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    fun setBottomSheetVisibility(state: Boolean){
        _isBottomSheetVisible.value=state
    }
    fun setBottomSheetVideoId(videoId: String){
        _bottomSheetParameter.value=videoId
    }
}