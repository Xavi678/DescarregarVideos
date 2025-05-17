package com.ivax.descarregarvideos.ui.composables


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.ui.MainViewModel

@Composable
fun AddPlaylistMenu(mainViewModel : MainViewModel= viewModel()) {
    val playlists by mainViewModel.playlists.collectAsStateWithLifecycle(emptyList())
    val showPlaylistMenu by mainViewModel.showPlaylistMenu.collectAsStateWithLifecycle()
    var showCreatePlaylistMenu by remember { mutableStateOf(false) }
    if(showPlaylistMenu) {
        Dialog(onDismissRequest = {
            mainViewModel.dismissPlaylistMenu()
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                LazyColumn {
                    items(playlists) {
                        ListItem(it)
                    }
                }
                TextButton(onClick = {
                    showCreatePlaylistMenu=true
                }) {
                    Text("Crear Playlist")
                    Icon(
                        imageVector = Icons.Default.LibraryAdd,
                        contentDescription = "Create Playlist"
                    )
                }
            }
        }
        CreatePlaylistDialog(showCreatePlaylistMenu)
    }
}

@Composable
fun CreatePlaylistDialog(showCreatePlaylistMenu: Boolean) {

    if(showCreatePlaylistMenu) {
        var text by remember { mutableStateOf("") }
        Dialog(onDismissRequest = {

        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(value = text, onValueChange = {
                        text = it
                    }, label = {
                        Text("Nom")
                    }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = {

                        }) {
                            Text("Cancel·lar")
                        }
                        TextButton(onClick = {

                        }) {
                            Text("Ok")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ListItem(playlist: Playlist) {
    var checked by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = playlist.name.toString(),
            modifier = Modifier
                .padding(start = 8.dp)
                .wrapContentWidth(align = Alignment.Start)
        )
        Checkbox(checked = checked, onCheckedChange = {
            checked = it
        }, modifier = Modifier
            .padding(end = 8.dp)
            .wrapContentWidth(align = Alignment.End))
    }
}