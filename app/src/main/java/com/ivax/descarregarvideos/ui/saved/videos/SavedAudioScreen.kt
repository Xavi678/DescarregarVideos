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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.ui.composables.bounceClick
import com.ivax.descarregarvideos.entities.SavedVideo
import java.io.FileInputStream

@Composable
fun SearchAudioScreen(savedVideosViewModel: SavedVideosViewModel = viewModel()){
    Column {
        SearchContentWrapper()
        AllVideos()
    }
    ShowBottomDialog()
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
fun SearchContentWrapper(savedVideosViewModel: SavedVideosViewModel = viewModel()){
    SearchComposable(onClickInvoker = fun (text: String) {
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
                .background(
                    Color.Blue
                )
        ) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
            )

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
                color = Color.White,
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .background(Color.Black)
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .weight(1f)) {
            Text(
                text = data.title
            )
            Row {
                Icon(painter = painterResource(id = R.drawable.download_rounded_base), contentDescription = "Download Icon")
                Text(text = data.downloadDateFormatted,
                    fontSize = 11.sp,
                    modifier= Modifier.align(alignment = Alignment.CenterVertically))
            }

        }
        IconButton(onClick = {
            savedVideosViewModel.setBottomSheetVisibility(true)
            savedVideosViewModel.setBottomSheetVideoId(data.videoId)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.three_dots),
                contentDescription = null,

                modifier = Modifier
                    .animateContentSize()
                    .align(alignment = Alignment.CenterVertically)
            )
        }

    }
    HorizontalDivider(Modifier.padding(4.dp), color = Color.LightGray)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomDialog(savedVideosViewModel: SavedVideosViewModel = viewModel()) {

    if (savedVideosViewModel.isBottomSheetVisible.collectAsStateWithLifecycle().value) {
        val videoId =
            savedVideosViewModel.bottomSheetParameter.collectAsStateWithLifecycle().value!!
        ModalBottomSheet(
            onDismissRequest = {
                savedVideosViewModel.setBottomSheetVisibility(false)
            }, containerColor = Color(29, 27, 32, 255)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            savedVideosViewModel.deleteVideo(videoId)
                            savedVideosViewModel.setBottomSheetVisibility(false)
                        },
                        indication = ripple(color = Color.Magenta),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .align(alignment = Alignment.CenterHorizontally)) {

                Image(
                    painter = painterResource(id = R.drawable.remove_trash),
                    contentDescription = null
                )

                Text(
                    text = "Delete Audio", color = Color.White,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

        }
    }

}