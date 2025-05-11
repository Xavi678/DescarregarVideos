package com.ivax.descarregarvideos.ui.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.entities.Playlist
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.ui.composables.PlayButton
import com.ivax.descarregarvideos.ui.preview.providers.PlaylistWithOrderedVideosFooPreviewParameterProvider
import com.ivax.descarregarvideos.ui.theme.MainAppTheme
import java.io.FileInputStream


@Composable
fun PlaylistScreen(
    playlistsViewModel: PlaylistsViewModel = viewModel(),
    function: (Int) -> Unit
){
    MainAppTheme {
        Column {
            SearchContentWrapper()
            ColumnPlaylists(function = function)
        }
    }
}

@Composable
fun SearchContentWrapper(playlistsViewModel: PlaylistsViewModel = viewModel()){
    SearchComposable(onClickInvoker = fun (text: String) {
        playlistsViewModel.filterPlaylist(text)
    })
}

@Composable
fun ColumnPlaylists(playlistsViewModel: PlaylistsViewModel = viewModel(), function: (Int) -> Unit){
    val playlists by playlistsViewModel.playlists.collectAsStateWithLifecycle(listOf<Playlist>())
    val orderedPlaylist by playlistsViewModel.orderedPlaylist.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orderedPlaylist) {
            Item(it,function)
        }
    }
}

@Preview
@Composable
fun ItemPreview(@PreviewParameter(PlaylistWithOrderedVideosFooPreviewParameterProvider::class) playlistWithOrderedVideosFoo: PlaylistWithOrderedVideosFoo){
    //Item(playlistWithOrderedVideosFoo)
}
@Composable
fun Item(playlistWithOrderedVideosFoo: PlaylistWithOrderedVideosFoo, function: (Int) -> Unit){

    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp).clickable(
        onClick = {

            function(playlistWithOrderedVideosFoo.playlist.playListId)
        },
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    )) {
        Box(
            Modifier
                .width(86.dp)
                .padding(top = 8.dp, start = 8.dp)
        ) {
        val firstVideo=playlistWithOrderedVideosFoo.orderedVideos.firstOrNull()
        if(firstVideo!=null){
            var bmp: Bitmap
            var fileInStream = FileInputStream(firstVideo.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()

            Image(bitmap = bmp.asImageBitmap(), contentDescription = "First Video Thumbnail")
        }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .weight(1f)) {
            Text(playlistWithOrderedVideosFoo.playlist.name.toString())
            PlayButton(onClickDelegate = fun (){

            })

        }

    }
}