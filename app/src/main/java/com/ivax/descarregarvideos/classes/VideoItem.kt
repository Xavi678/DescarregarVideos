package com.ivax.descarregarvideos.classes

import android.graphics.Bitmap
import android.net.Uri

data class VideoItem(val videoId: String, val title: String, val imgUrl: Bitmap?, val viewCount: String,
                     val duration:String,
                     var videoUrl: String?=null) {
}