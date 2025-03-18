package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.datasource.VideoLocalSource
import com.ivax.descarregarvideos.entities.SavedVideo

class VideoRepository(private val videoDataSource: VideoLocalSource) {
    fun insetVideo(video: SavedVideo) {
        videoDataSource.insert(video)
    }
}