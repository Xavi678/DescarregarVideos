package com.ivax.descarregarvideos.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.ExportData
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val videoRepository: VideoRepository,
) : ViewModel() {


    fun deleteVideo(videoId: String) {
        videoRepository.deleteVideo(videoId)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun importData(res: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            res?.let {
                context.contentResolver.openInputStream(it).use {
                    it?.let {
                        val exportData = Json.decodeFromStream<ExportData>(it)

                        exportData.videos.forEach {
                            videoRepository.insertVideo(it)
                        }
                        exportData.playlists.forEach {
                            videoRepository.upsertPlaylist(it)
                        }
                        exportData.videosPlaylists.forEach {
                            videoRepository.addPlaylistSavedVideo(it)
                        }
                    }
                }
            }
        }
    }

    fun exportData(res: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val videos = videoRepository.getAllCurrentVideos()
            val playlists = videoRepository.getAllCurrentPlaylists()
            val playlistSavedVideos = videoRepository.getAllCurrentPlaylistSavedVideoCrossRef()

            val exportData = ExportData(
                videos = videos,
                playlists = playlists,
                videosPlaylists = playlistSavedVideos
            )
            val data = Json.encodeToString(exportData)
            res?.let {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.contentResolver.openOutputStream(it).use {
                    it?.write(data.toByteArray())
                }
            }
        }
    }

    /*fun exportData(): String {
        viewModelScope.launch {
            val savedVideos = videoRepository.getAllCurrentVideos()
        }


        return jsonArray.toString()
    }*/
}