package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface VideoDao {
    /*@Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User*/

    @Query("SELECT * FROM savedvideo")
    fun getAll(): List<SavedVideo>

    @Query("SELECT * FROM savedvideo WHERE title LIKE '%' || :filter || '%'")
    fun getAll(filter: String): List<SavedVideo>

    @Upsert
    fun insertAll(vararg savedVideo: SavedVideo)

    @Query("UPDATE savedvideo SET playListId=:playListId WHERE videoId=:videoId")
    fun addVideoToPlaylist(playListId: Int,videoId: String)
    @Query("DELETE FROM savedvideo WHERE videoId=:videoId")
    fun delete(videoId: String)
    @Query("SELECT * FROM savedvideo WHERE videoId=:videoId LIMIT 1")
    fun first(videoId: String): SavedVideo?
    @Query("SELECT sv.* FROM playlistsavedvideocrossref psvr INNER JOIN savedvideo sv " +
            "ON psvr.videoId=sv.videoId WHERE psvr.playlistId=:playListId " +
            "ORDER BY psvr.position LIMIT 1")
    fun getFirstByPlaylist(playListId: Int) : SavedVideo?

    /*@Delete
    fun delete(user: User)*/
}