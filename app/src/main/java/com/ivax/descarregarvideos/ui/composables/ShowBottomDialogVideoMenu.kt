package com.ivax.descarregarvideos.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.ui.MainViewModel
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomDialogVideoMenu(
    playlistId: Int?,
    onClose: (deleteVideo: Boolean, deleteVideoFromPlaylist: Boolean) -> Unit,
    onShowPlayListMenu: () -> Unit
) {


    ModalBottomSheet(
        onDismissRequest = {
            onClose(false, false)
        }, containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            onClose(true, false)
                        },
                        indication = ripple(color = MaterialTheme.colorScheme.surface),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .align(alignment = Alignment.CenterHorizontally)) {

                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface
                )

                Text(
                    text = "Borrar Audio", color =MaterialTheme.colorScheme.surface,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
            if (playlistId != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            enabled = true,
                            onClick = {

                                onClose(false, true)
                            },
                            indication = ripple(color = MaterialTheme.colorScheme.surface),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .align(alignment = Alignment.CenterHorizontally)) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface
                    )

                    Text(
                        text = "Borrar de la Playlist", color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        enabled = true,
                        onClick = {

                            onShowPlayListMenu()
                        },
                        indication = ripple(color = MaterialTheme.colorScheme.surface),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .align(alignment = Alignment.CenterHorizontally)) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface
                )

                Text(
                    text = "Afegir a la Playlist", color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

        }

    }

}