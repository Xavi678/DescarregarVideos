package com.ivax.descarregarvideos.ui.playlists

import android.content.ClipDescription
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.entities.Playlist
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.ui.composables.ModalSheetBottomMenu
import com.ivax.descarregarvideos.ui.composables.PlayButton
import com.ivax.descarregarvideos.ui.composables.PlayShuffle
import com.ivax.descarregarvideos.ui.preview.providers.PlaylistWithOrderedVideosFooPreviewParameterProvider
import com.ivax.descarregarvideos.ui.theme.MainAppTheme
import java.io.FileInputStream


@Composable
fun PlaylistScreen(
    playlistsViewModel: PlaylistsViewModel = viewModel(),
    function: (Int) -> Unit
) {
    MainAppTheme {
        Column {
            SearchContentWrapper()
            ColumnPlaylists(function = function)
        }
        PlaylistBottomDialog()
    }

}

@Composable
fun SearchContentWrapper(playlistsViewModel: PlaylistsViewModel = viewModel()) {
    SearchComposable(onClickInvoker = fun(text: String) {
        playlistsViewModel.filterPlaylist(text)
    })
}

@Composable
fun ColumnPlaylists(playlistsViewModel: PlaylistsViewModel = viewModel(), function: (Int) -> Unit) {
    val playlists by playlistsViewModel.playlists.collectAsStateWithLifecycle(listOf<Playlist>())
    val orderedPlaylist by playlistsViewModel.orderedPlaylist.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(orderedPlaylist) { item ->
            Item(item, function)
        }
    }
}

@Preview
@Composable
fun ItemPreview(@PreviewParameter(PlaylistWithOrderedVideosFooPreviewParameterProvider::class) playlistWithOrderedVideosFoo: PlaylistWithOrderedVideosFoo) {
    //Item(playlistWithOrderedVideosFoo)
}

@Composable
fun Item(
    playlistWithOrderedVideosFoo: PlaylistWithOrderedVideosFoo,
    function: (Int) -> Unit,
    playlistsViewModel: PlaylistsViewModel = viewModel()
) {
    val context = LocalContext.current
    Box() {


        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)

                .clickable(
                    onClick = {

                        function(playlistWithOrderedVideosFoo.playlist.playListId)
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = MaterialTheme.colorScheme.surface)
                )
        ) {

            Box(
                Modifier
                    .width(86.dp)
                    .padding(top = 8.dp, start = 8.dp)
            ) {
                val firstVideo = playlistWithOrderedVideosFoo.orderedVideos.firstOrNull()
                if (firstVideo != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(firstVideo.imgUrl).build(),
                        contentDescription = "First Video Thumbnail"
                    )
                }
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .background(Color.Black)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.playlist),
                        contentDescription = "Collection Icon",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Text(
                        text = "${playlistWithOrderedVideosFoo.orderedVideos.count()}",
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 11.sp,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    playlistWithOrderedVideosFoo.playlist.name.toString(),
                    color = MaterialTheme.colorScheme.surface
                )
                Row{
                    PlayButton(onClickDelegate = fun() {
                        playlistsViewModel.playAll(playlistWithOrderedVideosFoo)
                    })
                    Spacer(modifier = Modifier.width(4.dp))
                    PlayShuffle(onClickDelegate = fun(){
                        playlistsViewModel.shuffle(playlistWithOrderedVideosFoo)
                    })
                }


            }
            IconButton(onClick = {
                playlistsViewModel.setSelectedPlaylist(playlistId = playlistWithOrderedVideosFoo.playlist.playListId)
                playlistsViewModel.setBottomSheetVisibility(true)
            }) {
                Icon(
                    painter = painterResource(R.drawable.three_dots),
                    contentDescription = "Menu Icon",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .animateContentSize()
                        .align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomDialog(playlistsViewModel: PlaylistsViewModel = viewModel()) {
    val playlistId by playlistsViewModel.selectedPlaylist.collectAsStateWithLifecycle()
    if (playlistsViewModel.isBottomSheetVisible.collectAsStateWithLifecycle().value && playlistId != null) {
        ModalBottomSheet(
            onDismissRequest = {
                playlistsViewModel.setBottomSheetVisibility(false)
            }, containerColor = Color(29, 27, 32, 255)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                playlistsViewModel.deletePlaylist(playlistId!!)
                                playlistsViewModel.setBottomSheetVisibility(false)
                            },
                            indication = ripple(color = MaterialTheme.colorScheme.primary),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .align(alignment = Alignment.CenterHorizontally)) {

                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Text(
                        text = "Delete Playlist", color = Color.White,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
            }
        }
    }

}