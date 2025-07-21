package com.ivax.descarregarvideos.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.RouteLabel
import kotlinx.serialization.Serializable

@Serializable
open class Route(val label: String,val icon: Int?=null,var hasBackButton: Boolean=false
                 ) {
    open fun navigate(navController: NavController){
        navController.navigate(this)
    }

    @Serializable
    object Playlists : Route("Playlists",R.drawable.collection_fill )

    @Serializable
    object Search : Route("Search",R.drawable.ic_menu_search)
    /*{
        override fun navigate(navController: NavController) {
            navController.navigate(this)
            {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }
    }*/

    @Serializable
    object SavedAudio : Route("Saved Audios",R.drawable.download_rounded_base)

    @Serializable
    data class EditPlaylist(val playlistId: Int,){
        companion object{
            fun get() : Route{
               return Route("Edit Playlist", hasBackButton = true,)
            }
        }
    }
}
