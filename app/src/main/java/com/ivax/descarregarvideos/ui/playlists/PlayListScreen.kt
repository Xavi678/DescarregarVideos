package com.ivax.descarregarvideos.ui.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.custom.composables.SearchComposable
import com.ivax.descarregarvideos.entities.Playlist
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import java.io.FileInputStream


@Composable
fun PlaylistScreen(playlistsViewModel: PlaylistsViewModel = viewModel()){
    Column {
        SearchContentWrapper()
        ColumnPlaylists()
    }
}

@Composable
fun SearchContentWrapper(playlistsViewModel: PlaylistsViewModel = viewModel()){
    SearchComposable(onClickInvoker = fun (text: String) {
        playlistsViewModel.filterPlaylist(text)
    })
}

@Composable
fun ColumnPlaylists(playlistsViewModel: PlaylistsViewModel = viewModel()){
    val playlists by playlistsViewModel.playlists.collectAsStateWithLifecycle(listOf<Playlist>())
    val orderedPlaylist by playlistsViewModel.orderedPlaylist.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orderedPlaylist) {
            Item(it)
        }
    }
}

@Composable
fun Item(playlist: PlaylistWithOrderedVideosFoo){



    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        val firstVideo=playlist.orderedVideos.firstOrNull()
        if(firstVideo!=null){
            var bmp: Bitmap
            var fileInStream = FileInputStream(firstVideo.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()

            Image(bitmap = bmp.asImageBitmap(), contentDescription = "First Video Thumbnail")
        }

        Text(playlist.playlist.name.toString())

    }
}