package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


class VideoRepository @Inject constructor(private val videoDao: VideoDao) {
    fun insetVideo(video: SavedVideo) {
        videoDao.insertAll(video)
    }

    fun getAllVideos(): Flow<List<SavedVideo>> {
       return videoDao.getAll()
    }

    fun addVideoToPlaylist(playListId: Int,videoId: String) {
        videoDao.addVideoToPlaylist(playListId,videoId)
    }
}