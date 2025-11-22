package com.ivax.descarregarvideos.ui.composables

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivax.descarregarvideos.ui.MainViewModel
import com.ivax.descarregarvideos.ui.theme.CustomInputColors
import androidx.core.net.toUri
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.*
import kotlinx.serialization.json.*



@Composable
fun SearchComposable(
    onClickInvoker: (text: String) -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var text by rememberSaveable { mutableStateOf("") }
    var showExportImport by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val getContent = ActivityResultContracts.OpenDocument()

    //getContent.createIntent(context, "applicatyion/json")

    val getContentLaunch = rememberLauncherForActivityResult(getContent) { res ->
        mainViewModel.importData(res)
    }
    val createDocument = CreateDocument("applicatyion/json")
    val createDocumentLaunch = rememberLauncherForActivityResult(createDocument) { res ->
        mainViewModel.exportData(res)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(8.dp)
                .weight(1f)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder = {
                    Text(text = "Search...")
                },
                leadingIcon = {
                    IconButton(onClick = {
                        onClickInvoker(text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Icon",
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable(
                                enabled = true,
                                onClick = {
                                    text = ""
                                },
                                indication = ripple(),
                                interactionSource = remember { MutableInteractionSource() })
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CustomInputColors()
            )
        }
        IconButton(
            onClick = {
                showExportImport = true
            }, modifier = Modifier
                .weight(0.1f)
                .align(alignment = Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ImportExport, contentDescription = "Import Export Icon",
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
    if (showExportImport) {
        ImportExportDialog(onClose = fun(option) {
            showExportImport = false
            when (option) {
                ImportExportDialogUIState.EXPORT -> {
                    createDocumentLaunch.launch(
                        "${
                            LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"))
                        }_exported_data.json"
                    )

                }

                ImportExportDialogUIState.IMPORT -> {

                    getContentLaunch.launch(arrayOf("application/json"))
                }

                else -> {}
            }
        })
    }
}


@Preview
@Composable
fun SearchComposablePreview() {
    SearchComposable(onClickInvoker = fun(text: String) {

    })
}