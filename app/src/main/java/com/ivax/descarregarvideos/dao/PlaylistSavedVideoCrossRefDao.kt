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
}