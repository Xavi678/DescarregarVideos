package com.ivax.descarregarvideos.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["playListId", "videoId"],
   /* foreignKeys = [ForeignKey(entity = Playlist::class, parentColumns = ["id"],
        childColumns = ["playListId"],
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)]*/
)
data class PlaylistSavedVideoCrossRef(
    val playListId: Int,
    val videoId: String,
    var position: Int=0
){

}