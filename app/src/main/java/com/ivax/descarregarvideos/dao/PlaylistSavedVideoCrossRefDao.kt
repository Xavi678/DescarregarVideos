package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistSavedVideoCrossRefDao {
    @Insert
    fun insert(playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef)

    @Query("SELECT * FROM playlistsavedvideocrossref WHERE playListId=:playlistId AND videoId=:videoId LIMIT 1")
    fun first(playlistId: Int, videoId: String): PlaylistSavedVideoCrossRef?

    @Query("DELETE FROM playlistsavedvideocrossref WHERE playListId=:playListId")
    fun deletePlaylist(playListId: Int)

    //@Delete
    @Query("DELETE FROM playlistsavedvideocrossref")
    fun deleteAll(): Int

    @Query("SELECT * FROM playlistsavedvideocrossref WHERE playListId=:playListId")
    fun getByPlaylistId(playListId: Int): List<PlaylistSavedVideoCrossRef>

    @Query("SELECT r.playListId,r.videoId,r.position,s.duration,s.imgUrl,s.title,s.videoUrl,s.viewCount,s.downloadDate FROM playlistsavedvideocrossref r JOIN savedvideo s ON r.videoId=s.videoId  WHERE r.playListId=:playlistId order by r.position")
    fun getByPlaylistIdWithPositions(playlistId: Int): List<VideosWithPositionFoo>

    @Query("SELECT r.playListId,r.videoId,r.position,s.duration,s.imgUrl,s.title,s.videoUrl,s.viewCount,s.downloadDate FROM playlistsavedvideocrossref r JOIN savedvideo s ON r.videoId=s.videoId order by r.position")
    fun getAllVideosWithPositionFoo(): Flow<List<VideosWithPositionFoo>>

    @Update
    fun updatePlaylistSavedVideoCrossRef(playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef)

    @Query("DELETE FROM playlistsavedvideocrossref WHERE videoId=:videoId")
    fun deleteVideo(videoId: String)

    @Query("SELECT * FROM playlistsavedvideocrossref order by position")
    fun getAll(): Flow<List<PlaylistSavedVideoCrossRef>>

    @Query("UPDATE playlistsavedvideocrossref SET position=:position WHERE playListId=:playlistId AND videoId=:videoId")
    fun updatePosition(playlistId: Int, videoId: String, position: Int): Int
}