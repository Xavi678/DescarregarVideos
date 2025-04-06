package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>
    @Insert
    fun insert(playlist: Playlist) : Long
    @Query("SELECT * FROM playlist where playListId=:id LIMIT 1")
    fun first(id: Int) : Playlist?
    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getAllPlaylistsWithVideos() : Flow<List<PlaylistWithSavedVideos>>
}