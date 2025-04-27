package com.ivax.descarregarvideos.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
import java.time.format.DateTimeFormatter

@Entity
data class SavedVideo(@PrimaryKey val videoId: String, val title: String, val imgUrl: String?,
                      val duration: String, val viewCount: String, var videoUrl: String?=null,
                      var playListId: Int?=null,
                      @ColumnInfo(defaultValue = "2025-04-27T15:00:00" )
                      var downloadDate : LocalDateTime = LocalDateTime.now()) {
}