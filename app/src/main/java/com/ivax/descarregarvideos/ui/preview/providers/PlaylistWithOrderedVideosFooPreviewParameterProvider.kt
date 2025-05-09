package com.ivax.descarregarvideos.ui.preview.providers


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivax.descarregarvideos.classes.OrderedVideos
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.entities.Playlist

class PlaylistWithOrderedVideosFooPreviewParameterProvider :
    PreviewParameterProvider<PlaylistWithOrderedVideosFoo> {
        override val values=sequenceOf(
            PlaylistWithOrderedVideosFoo(playlist = Playlist(0,"nova"),
                orderedVideos = listOf(OrderedVideos(position = 0, videoId = "wqeqw",
                    title = "Bring Me The Horizon - Drown", duration = "4:56",
                    videoUrl = "/data/user/0/com.ivax.descarregarvideos/files/videos/TkV5709EG5M.mp4",
                    imgUrl = "/data/user/0/com.ivax.descarregarvideos/files/fotos/TkV5709EG5M_thumbnail.bmp",
                    viewCount = "324324")))
        )
}