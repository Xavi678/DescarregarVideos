package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Query
import com.ivax.descarregarvideos.entities.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>
}