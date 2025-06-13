package com.ivax.descarregarvideos.ui.routes

import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.RouteLabel
import kotlinx.serialization.Serializable

@Serializable
open class Route(val label: String,val icon: Int?=null,var hasBackButton: Boolean=false) {
    @Serializable
    object Playlists : Route("Playlists",R.drawable.collection_fill )

    @Serializable
    object Search : Route("Search",R.drawable.ic_menu_search)

    @Serializable
    object SavedAudio : Route("Saved Audios",R.drawable.download_rounded_base)

    @Serializable
    data class EditPlaylist(val playlistId: Int){
        companion object{
            fun get() : Route{
               return Route("Edit Playlist", hasBackButton = true)
            }
        }
    }
}
