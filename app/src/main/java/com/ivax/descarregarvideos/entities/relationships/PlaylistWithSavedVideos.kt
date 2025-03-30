package com.ivax.descarregarvideos.entities.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.SavedVideo

class PlaylistWithSavedVideos(    @Embedded val playlist: Playlist,
                                  @Relation(
                                      parentColumn = "uid",
                                      entityColumn = "playListId"
                                  )
                                  val videos: List<SavedVideo>) {
}