package com.ivax.descarregarvideos.ui.composables


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.ui.MainViewModel


@Composable
fun AddPlaylistMenu(mainViewModel: MainViewModel = hiltViewModel()) {
    val playlists by mainViewModel.playlists.collectAsStateWithLifecycle(emptyList())
    val showPlaylistMenu by mainViewModel.showPlaylistMenu.collectAsStateWithLifecycle()
    val showCreatePlaylistMenu by mainViewModel.showCreatePlaylistMenu.collectAsStateWithLifecycle()
    val videoId by mainViewModel.videoId.collectAsStateWithLifecycle()
    if (showPlaylistMenu && videoId!=null) {
        Dialog(onDismissRequest = {
            mainViewModel.dismissPlaylistMenu()
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(200.dp, 350.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TextButton(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                            .align(alignment = Alignment.CenterHorizontally)
                            .wrapContentHeight(align = Alignment.Bottom), onClick = {
                            mainViewModel.showCreatePlaylistMenu()
                        }) {
                        Icon(
                            imageVector = Icons.Default.LibraryAdd,
                            contentDescription = "Create Playlist"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crear Playlist")
                    }
                    HorizontalDivider(modifier = Modifier.height(4.dp))
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(playlists) {
                            ListItem(it,videoId!!)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = {
                            mainViewModel.saveChanges()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Check,
                            contentDescription = "Check")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }

                }
            }
        }
        CreatePlaylistDialog(showCreatePlaylistMenu)
    }
}

@Composable
fun CreatePlaylistDialog(
    showCreatePlaylistMenu: Boolean,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    var isError by remember { mutableStateOf(false) }
    if (showCreatePlaylistMenu) {
        var playlistName by remember { mutableStateOf("") }
        Dialog(onDismissRequest = {

        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(value = playlistName, onValueChange = {
                        playlistName = it
                    }, isError = isError, label = {
                        Text("Nom")
                    }, modifier = Modifier.fillMaxWidth())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .wrapContentWidth(align = Alignment.End)
                    ) {
                        TextButton(onClick = {
                            mainViewModel.dismissCreatePlaylistMenu()
                        }, modifier = Modifier) {
                            Text("Cancel·lar")
                        }
                        TextButton(onClick = {
                            if (playlistName.isBlank()) {
                                isError = true
                            } else {
                                mainViewModel.createPlaylist(playlistName)
                                isError = false
                            }

                        }, modifier = Modifier) {
                            Text("Ok")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ListItem(playlist: PlaylistWithSavedVideos,videoId: String,mainViewModel: MainViewModel= hiltViewModel()) {

    var checked by remember { mutableStateOf(playlist.videos.firstOrNull { it.videoId==videoId }!=null) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = playlist.playlist.name.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .wrapContentWidth(align = Alignment.Start)
                .align(alignment = Alignment.CenterVertically)
        )
        Checkbox(
            checked = checked, onCheckedChange = {
                checked = it
                mainViewModel.addChange(playlist.playlist.playListId,videoId,it)
            }, modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .wrapContentWidth(align = Alignment.End)
                .align(alignment = Alignment.CenterVertically)
        )
    }
}