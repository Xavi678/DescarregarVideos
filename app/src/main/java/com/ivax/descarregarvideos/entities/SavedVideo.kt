package com.ivax.descarregarvideos.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivax.descarregarvideos.classes.LocalDateTimeSerializer
import com.ivax.descarregarvideos.classes.getCustomDateTimePattern
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.Date
import java.time.format.DateTimeFormatter

@Entity
@Serializable
data class SavedVideo(@PrimaryKey val videoId: String, val title: String, val imgUrl: String?,
                      val duration: String, val viewCount: String, var videoUrl: String?=null,
                      var playListId: Int?=null,
                      @ColumnInfo(defaultValue = "2025-04-27T15:00:00" )
                      @Serializable(with = LocalDateTimeSerializer::class)
                      var downloadDate : LocalDateTime = LocalDateTime.now(),
                      @ColumnInfo(defaultValue = "NULL" )
                      var author: String?=null) {

    val downloadDateFormatted : String get()=downloadDate.format(getCustomDateTimePattern())
}