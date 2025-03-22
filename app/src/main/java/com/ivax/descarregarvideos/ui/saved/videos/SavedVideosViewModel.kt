package com.ivax.descarregarvideos.ui.saved.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.ivax.descarregarvideos.MyApplication
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.VideoRepository
import com.ivax.descarregarvideos.ui.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SavedVideosViewModel @Inject constructor(private val repository: VideoRepository) : ViewModel() {
    val allSavedVideos: LiveData<List<SavedVideo>> = repository.getAllVideos().asLiveData()

    /*companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return SavedVideosViewModel(
                    (application as MyApplication).videoRepository
                ) as T
            }
        }
    }*/
}