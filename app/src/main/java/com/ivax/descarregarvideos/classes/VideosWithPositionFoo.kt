package com.ivax.descarregarvideos.classes

import androidx.room.Embedded
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class VideosWithPositionFoo(val playListId: Int, val videoId: String, var position: Int,
                                 val title: String, val imgUrl : String?,
                                 val duration: String, val viewCount: String,val videoUrl: String?,
                                 var downloadDate: LocalDateTime) {

    val downloadDateFormatted : String get()=downloadDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}