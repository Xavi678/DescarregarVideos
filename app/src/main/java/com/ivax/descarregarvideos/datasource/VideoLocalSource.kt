package com.ivax.descarregarvideos.datasource

import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.entities.SavedVideo

class VideoLocalSource(private val videoDao: VideoDao) {

    fun insert(video: SavedVideo) {
        videoDao.insertAll(video)
    }

    /*fun getAllMovies(): Flow<List<MovieDb>> {
        return movieDao.getAll()
    }

    suspend fun updateTitle(id: Int, newTitle: String) {
        movieDao.updateTitle(movieId = id, newTitle = newTitle)
    }

    fun delete(movieId: List<Int>) {
        movieDao.delete(movieId)
    }*/
}