package com.ivax.descarregarvideos.ui.edit.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ivax.descarregarvideos.ui.composables.PlayButton
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo

@Composable
fun EditPlaylistScreen(playlistId: Int,viewModel: EditPlaylistViewModel) {
    Column {
        Top(playlistId,viewModel)
        Playlists(viewModel)
    }

}
@Composable
fun Top(playlistId: Int,viewModel: EditPlaylistViewModel){
    viewModel.getPlaylist(playlistId)

    val playlist by viewModel.playlist.collectAsState()
    Text(playlist?.name.toString())
    PlayButton()
}

@Composable
fun Playlists(viewModel: EditPlaylistViewModel){
    val playlistIdWithPositions by viewModel.playlistIdWithPositions.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if(playlistIdWithPositions!=null) {
            items(playlistIdWithPositions!!) {
                ListItem(it)
            }
        }
    }
}
@Composable
fun ListItem(videosWithPositionFoo: VideosWithPositionFoo){
    Text(videosWithPositionFoo.title)
}