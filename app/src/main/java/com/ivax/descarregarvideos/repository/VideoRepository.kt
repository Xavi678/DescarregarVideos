package com.ivax.descarregarvideos.repository

import android.util.Log
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.dao.PlayListDao
import com.ivax.descarregarvideos.dao.PlaylistSavedVideoCrossRefDao
import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


class VideoRepository @Inject constructor(private val videoDao: VideoDao,
                                          private val playListDao: PlayListDao,
                                          private val playlistSavedVideoCrossRefDao: PlaylistSavedVideoCrossRefDao) {
    fun insetVideo(video: SavedVideo) {
        videoDao.insertAll(video)
    }

    fun getAllVideos(): Flow<List<SavedVideo>> {
       return videoDao.getAll()
    }
    fun getPlaylistSavedVideoCrossRefbyPlaylistId(playlistId: Int) : List<PlaylistSavedVideoCrossRef>{
        return playlistSavedVideoCrossRefDao.getByPlaylistId(playlistId)
    }
    fun addVideoToPlaylist(playListId: Int,videoId: String) {
        videoDao.addVideoToPlaylist(playListId,videoId)
    }
    fun getAllPlaylists(): Flow<List<Playlist>>{
        return playListDao.getAllPlaylists()
    }

    fun addPlaylist(playlist: Playlist):Int {
      return  playListDao.insert(playlist).toInt()
    }
    fun firstPlaylist(id: Int): Playlist?{
       return playListDao.first(id)
    }
    fun addPlaylistSavedVideo(playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef){
        if(!playlistSavedVideoCrossRefExists(playlistSavedVideoCrossRef)) {
            playlistSavedVideoCrossRefDao.insert(playlistSavedVideoCrossRef)
        }
    }
    fun getByPlaylistIdWithPositions(playlistId: Int) : List<VideosWithPositionFoo> {
        return playlistSavedVideoCrossRefDao.getByPlaylistIdWithPositions(playlistId)
    }
    fun playlistSavedVideoCrossRefExists(playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef): Boolean {

        return playlistSavedVideoCrossRefDao.first(playlistSavedVideoCrossRef.playListId,playlistSavedVideoCrossRef.videoId)!=null
    }

    fun getAllPlaylistsWithVideos() : Flow<List<PlaylistWithSavedVideos>>  {
        return playListDao.getAllPlaylistsWithVideos()
    }

    fun deletePlaylist(playlistId: Int) {
        playListDao.deletePlaylist(playlistId)
        playlistSavedVideoCrossRefDao.deletePlaylist(playlistId)
        //val res=playlistSavedVideoCrossRefDao.deleteAll()
    }

    fun firstPlaylistWithSavedVideos(playlistId: Int): PlaylistWithSavedVideos {
       return playListDao.firstWithSavedVideos(playlistId)
    }

    fun UpdatePlaylistSavedVideoCrossRef(videosWithPositionFoo: PlaylistSavedVideoCrossRef) {
        Log.d("DescarregarVideos","Video Id: ${videosWithPositionFoo.videoId} Position: ${videosWithPositionFoo.position}")
        playlistSavedVideoCrossRefDao.updatePlaylistSavedVideoCrossRef(videosWithPositionFoo)
    }

    fun getPlaylistSavedVideoCrossRefByFoo(videosWithPositionFoo: VideosWithPositionFoo): PlaylistSavedVideoCrossRef? {

       return playlistSavedVideoCrossRefDao.first(videosWithPositionFoo.playListId,videosWithPositionFoo.videoId)
    }

    fun deleteVideo(videoId: String) {
        videoDao.delete(videoId)
        playlistSavedVideoCrossRefDao.deleteVideo(videoId = videoId)
    }
}
