package com.ivax.descarregarvideos.ui.search

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import com.ivax.descarregarvideos.ui.composables.FormatsDialog
import java.io.File

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()){
    Column {
        SearchComposable(onClickInvoker = fun(text: String) {
            searchViewModel.SearchVideos(text)
        })
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Loading()
            SearchVideos()
        }

    }
}

@Composable
fun Loading(searchViewModel: SearchViewModel = viewModel()) {
    val isLoading by searchViewModel.isLoading.collectAsStateWithLifecycle()
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

enum class DownloadState { NotDownloaded, Downloaded, Downloading }

@Composable
fun Item(video: VideoItem,searchViewModel: SearchViewModel = viewModel()) {
    var downloadState by remember { mutableStateOf(if (video.videoDownloaded) DownloadState.Downloaded else DownloadState.NotDownloaded) }
    val formats by searchViewModel.formats.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var savedVideo by remember { mutableStateOf<SavedVideo?>(null) }
    LaunchedEffect(Unit) {
        val imgPath = "${video.videoId}_thumbnail.bmp"
        var dir = File("${context.filesDir}/fotos")
        var d = dir.mkdir()
        var f = File("${dir}/${imgPath}")

        if (f.exists()) {
            f.delete()
        }
        f.createNewFile()
        f.outputStream().use {
            video.imgUrl?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        val saveVideo = SavedVideo(
            video.videoId,
            video.title,
            "${dir}/${imgPath}",
            video.duration,
            video.viewCount,
            author = video.author
        )
        savedVideo=saveVideo
    }
    if (savedVideo!=null) {
        FormatsDialog(formats, onClose = fun(selectedFormat: AdaptiveFormats?) {
            if (selectedFormat != null) {

                searchViewModel.downloadVideo(selectedFormat, savedVideo!!, finished = fun() {

                    downloadState = DownloadState.Downloaded
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context, "Video ${savedVideo!!.videoId} Descarregat Correctament",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        })
        Row() {

            Box(
                Modifier
                    .width(86.dp)
                    .padding(top = 8.dp, start = 8.dp)
                    .background(
                        Color.Blue
                    )
            ) {
                Image(
                    bitmap = video.imgUrl!!.asImageBitmap(),
                    contentDescription = null,
                )
                Text(
                    text = video.duration,
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomStart)
                        .background(Color.Black)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = video.title,
                    fontSize = 16.sp
                )
                Row {
                    if (video.channelThumbnail != null) {
                        Image(
                            bitmap = video.channelThumbnail!!.asImageBitmap(),
                            contentDescription = "Channel Video Thumbnail"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = video.author.toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Text(
                    text = video.viewCount,
                    fontSize = 11.sp,
                    color = Color.LightGray
                )


            }

            IconButton(onClick = {
                downloadState = DownloadState.Downloading
                searchViewModel.getAudioUrlsResponse(
                    savedVideo!!,
                    callback = fun(formats: List<AdaptiveFormats>, ) {
                        searchViewModel.setFormats(formats)
                    })
            }) {
                Icon(
                    painter = painterResource(
                        id = when
                                     (downloadState) {
                            DownloadState.NotDownloaded
                                -> R.drawable.download_button

                            DownloadState.Downloading ->
                                R.drawable.downloading

                            else -> R.drawable.finished_downloading
                        }
                    ),
                    contentDescription = null,

                    modifier = Modifier
                        .animateContentSize()
                        .align(alignment = Alignment.CenterVertically)
                )
            }

        }
        HorizontalDivider(Modifier.padding(4.dp), color = Color.LightGray)
    }
}
@Composable
fun SearchVideos(
    searchViewModel: SearchViewModel = viewModel()
) {
    val videos by searchViewModel.videos.collectAsStateWithLifecycle()
    val isLoading by searchViewModel.isLoading.collectAsStateWithLifecycle()







    var offset by remember { mutableFloatStateOf(0f) }
    var listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    offset += delta

                    Log.d("DescarregarVideos", "Offset: ${offset} Delta: ${delta}")
                    Log.d(
                        "DescarregarVideos",
                        "Can Scroll Forward: ${listState.canScrollForward} " +
                                "Last Scroll Forward: ${listState.lastScrolledForward}"
                    )
                    if (!listState.canScrollForward && !isLoading) {
                        searchViewModel.loadMoreVideos()
                    }
                    delta

                },
            )
    ) {
        Log.d("DescarregarVideos","Total Videos: ${videos.count()}")
        items(videos,key = {it.videoId}) {

            Item(it)
        }
    }

}