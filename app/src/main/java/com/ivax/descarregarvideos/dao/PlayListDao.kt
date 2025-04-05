package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ivax.descarregarvideos.entities.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>
    @Insert
    fun insert(playlist: Playlist) : Long
    @Query("SELECT * FROM playlist where playListId=:id LIMIT 1")
    fun first(id: Int) : Playlist?
}