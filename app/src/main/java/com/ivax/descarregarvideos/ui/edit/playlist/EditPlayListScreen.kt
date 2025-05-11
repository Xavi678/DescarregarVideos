package com.ivax.descarregarvideos.ui.edit.playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.ui.composables.PlayButton
import com.ivax.descarregarvideos.ui.composables.bounceClick
import java.io.FileInputStream

@Composable
fun EditPlaylistScreen(
    playlistId: Int,
    viewModel: EditPlaylistViewModel
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Top(playlistId,viewModel)
        Spacer(modifier = Modifier.width(8.dp))
        Playlists(viewModel)
    }

}
@Composable
fun Top(playlistId: Int,viewModel: EditPlaylistViewModel){
    viewModel.getPlaylist(playlistId)

    val playlist by viewModel.playlist.collectAsState()
    Text(playlist?.name.toString())
    Spacer(modifier = Modifier.width(8.dp))
    PlayButton(onClickDelegate = {

    })
}

@Composable
fun Playlists(viewModel: EditPlaylistViewModel){
    val playlistIdWithPositions by viewModel.playlistIdWithPositions.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if(playlistIdWithPositions!=null) {
            items(playlistIdWithPositions!!) {
                ListItem(it,viewModel)
            }
        }
    }
}
@Composable
fun ListItem(videosWithPositionFoo: VideosWithPositionFoo,viewModel: EditPlaylistViewModel){
    var bmp: Bitmap
    var fileInStream = FileInputStream(videosWithPositionFoo.imgUrl)
    fileInStream.use {
        bmp = BitmapFactory.decodeStream(it)
    }

    fileInStream.close()


    Row {

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
                            /*viewModel.addSingleItemMedia(data)
                            viewModel.setSavedVideo(data)
                            viewModel.play()
                            viewModel.setMediaVisibility(true)*/
                        },

                        )
            )
            Text(
                text = videosWithPositionFoo.duration,
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
                text = videosWithPositionFoo.title
            )
            Row {
                Icon(painter = painterResource(id = R.drawable.download_rounded_base), contentDescription = "Download Icon")
                Text(text = videosWithPositionFoo.downloadDateFormatted.toString(),
                    fontSize = 11.sp,
                    modifier= Modifier.align(alignment = Alignment.CenterVertically))
            }

        }
        IconButton(onClick = {
            /*savedVideosViewModel.setBottomSheetVisibility(true)
            savedVideosViewModel.setBottomSheetVideoId(data.videoId)*/
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