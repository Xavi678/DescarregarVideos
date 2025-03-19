package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.datasource.VideoLocalSource
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.coroutines.flow.Flow

class VideoRepository(private val videoDataSource: VideoLocalSource) {
    fun insetVideo(video: SavedVideo) {
        videoDataSource.insert(video)
    }

    fun getAllVideos(): Flow<List<SavedVideo>> {
       return videoDataSource.getAllVideos()
    }
}