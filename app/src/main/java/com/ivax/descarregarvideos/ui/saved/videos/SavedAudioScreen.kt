package com.ivax.descarregarvideos.ui.saved.videos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.ui.composables.bounceClick
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.general.viewmodels.ModalSheetBottomMenuViewModel
import com.ivax.descarregarvideos.ui.composables.AddPlaylistMenu
import com.ivax.descarregarvideos.ui.composables.ModalSheetBottomMenu
import com.ivax.descarregarvideos.ui.composables.ShowBottomDialogVideoMenu
import java.io.FileInputStream

@Composable
fun SearchAudioScreen(savedVideosViewModel: SavedVideosViewModel = viewModel(),
                      modalSheetBottomMenuViewModel: ModalSheetBottomMenuViewModel= hiltViewModel()) {
    Column {
        SearchContentWrapper()
        AllVideos()
    }
    val selectedVideoId by
    savedVideosViewModel.bottomSheetParameter.collectAsStateWithLifecycle()
    if(selectedVideoId!=null) {
        ModalSheetBottomMenu(selectedVideoId!!,null,modalSheetBottomMenuViewModel){
            savedVideosViewModel.resetSelectedVideo()
        }
    }
}

@Composable
fun AllVideos(
    savedVideosViewModel: SavedVideosViewModel = viewModel()
) {
    val savedVideos by savedVideosViewModel.allSavedVideos.collectAsStateWithLifecycle(listOf<SavedVideo>())
    LazyColumn(Modifier.fillMaxSize()) {
        items(savedVideos) {
            ListItem(it)
        }
    }
}

@Composable
fun SearchContentWrapper(savedVideosViewModel: SavedVideosViewModel = viewModel()) {
    SearchComposable(onClickInvoker = fun(text: String) {
        savedVideosViewModel.filterSavedVideos(text)
    })
}


@Composable
fun ListItem(
    data: SavedVideo,
    modifier: Modifier = Modifier,
    savedVideosViewModel: SavedVideosViewModel = viewModel()
) {

    var bmp: Bitmap
    var fileInStream = FileInputStream(data.imgUrl)
    fileInStream.use {
        bmp = BitmapFactory.decodeStream(it)
    }

    fileInStream.close()


    Row(modifier) {

        Box(
            Modifier
                .width(86.dp)
                .padding(top = 8.dp, start = 8.dp)
        ) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(bmp).build(),
                contentDescription = null,)
            Image(
                painter = painterResource(id = R.drawable.play_button_rect_mod),
                contentDescription = null,
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .bounceClick()
                    .clickable(
                        enabled = true,
                        onClick = {
                            savedVideosViewModel.addSingleItemMedia(data)
                            savedVideosViewModel.setSavedVideo(data)
                            savedVideosViewModel.play()
                            savedVideosViewModel.setMediaVisibility(true)
                        },

                        )
            )
            Text(
                text = data.duration,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .background(Color.Black),
                fontSize =  12.sp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = data.title,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 16.sp,
            )
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.download_rounded_base),
                    contentDescription = "Download Icon",
                    tint = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier=Modifier.width(8.dp))
                Text(
                    text = data.downloadDateFormatted,
                    fontSize = 14.sp,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.surface
                )
            }

        }
        IconButton(onClick = {
            savedVideosViewModel.setBottomSheetVisibility(true)
            savedVideosViewModel.setBottomSheetVideoId(data.videoId)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.three_dots),
                contentDescription = null,
                tint=MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .animateContentSize()
                    .align(alignment = Alignment.CenterVertically)
            )
        }

    }
}

