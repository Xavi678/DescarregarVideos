package com.ivax.descarregarvideos.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedVideo(@PrimaryKey val videoId: String, val title: String, val imgUrl: String?,
                      val duration: String, val viewCount: String, var videoUrl: String?=null) {
}