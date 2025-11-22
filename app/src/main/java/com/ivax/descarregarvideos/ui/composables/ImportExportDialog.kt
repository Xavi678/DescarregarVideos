package com.ivax.descarregarvideos.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivax.descarregarvideos.ui.MainViewModel

enum class ImportExportDialogUIState{
    EXPORT, IMPORT, NONE
}

@Composable
fun ImportExportDialog(onClose: (selectedOption: ImportExportDialogUIState) -> Unit/*,mainViewModel: MainViewModel= hiltViewModel()*/) {
    Dialog(onDismissRequest = fun() {
        onClose(ImportExportDialogUIState.NONE)
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                Row(Modifier
                    .clickable() {
                        onClose(ImportExportDialogUIState.EXPORT)
                    }
                    .padding(16.dp)
                    .fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowOutward,
                        contentDescription = "Export Icon",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        tint = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        text = "Exportar Dades",
                        color = MaterialTheme.colorScheme.surface
                    )
                }
                Row(Modifier
                    .clickable() {
                      onClose(ImportExportDialogUIState.IMPORT)
                    }
                    .padding(16.dp)
                    .fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDownward,
                        contentDescription = "Import Icon",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        tint = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        text = "Importar Dades",
                        color = MaterialTheme.colorScheme.surface
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .wrapContentWidth(align = Alignment.End)
                ) {
                    TextButton(onClick = {
                        onClose(ImportExportDialogUIState.NONE)
                    }, modifier = Modifier) {
                        Text("Cancel·lar", color = MaterialTheme.colorScheme.surface)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ImportExportDialogPreview() {

    ImportExportDialog(onClose = fun(option) {

    })
}