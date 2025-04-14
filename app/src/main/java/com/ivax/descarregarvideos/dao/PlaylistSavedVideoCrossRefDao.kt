package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef

@Dao
interface PlaylistSavedVideoCrossRefDao {
    @Insert
    fun insert(playlistSavedVideoCrossRef : PlaylistSavedVideoCrossRef)
    @Query("SELECT * FROM playlistsavedvideocrossref WHERE playListId=:playlistId AND videoId=:videoId LIMIT 1")
    fun first(playlistId: Int, videoId: String): PlaylistSavedVideoCrossRef?
    @Query("DELETE FROM playlistsavedvideocrossref WHERE playListId=:playListId")
    fun deletePlaylist(playListId: Int)
    @Query("DELETE FROM playlistsavedvideocrossref")
    fun deleteAll()
    @Query("SELECT * FROM playlistsavedvideocrossref WHERE playListId=:playListId")
    fun getByPlaylistId(playListId: Int): List<PlaylistSavedVideoCrossRef>
}