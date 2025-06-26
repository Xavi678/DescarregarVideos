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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.source.ExternalLoader.LoadRequest
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.DownloadState
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.ui.composables.SearchComposable
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import com.ivax.descarregarvideos.ui.composables.AddPlaylistMenu
import com.ivax.descarregarvideos.ui.composables.FormatsDialog
import java.io.File

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel()){
    Column {
        SearchComposable(onClickInvoker = fun(text: String) {
            searchViewModel.SearchVideos(text)
        })
        /*Button(onClick = {
            searchViewModel.loadMoreVideos()
        }) {
            Text("Load More Videos", color = MaterialTheme.colorScheme.surface)
        }*/
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
            color = MaterialTheme.colorScheme.surface,
            strokeWidth = 8.dp,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
            trackColor = MaterialTheme.colorScheme.secondary,

        )
    }
}



@Composable
fun Item(video: VideoItem,searchViewModel: SearchViewModel = viewModel()) {

        Row{

            Box(
                Modifier
                    .width(86.dp)
                    .padding(top = 8.dp, start = 8.dp)
            ) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(video.imgUrl).build(),
                    contentDescription = null,)
                Text(
                    text = video.duration,
                    color = Color.White,
                    fontSize = 12.sp,
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
                    fontSize = 16.sp,
                    color=MaterialTheme.colorScheme.surface
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
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Text(
                    text = video.viewCount,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.surface
                )


            }

            IconButton(onClick = {
                searchViewModel.getAudioUrlsResponse(
                    video,
                    callback = fun(formats: List<AdaptiveFormats>, ) {
                        searchViewModel.setFormats(video,formats)
                    })
            }) {
                Log.d("DescarregarVideos","${video.title} State: ${video.videoDownloaded}")
                Icon(

                    painter = painterResource(
                        id = when
                                     (video.videoDownloaded) {

                            DownloadState.NotDownloaded
                                -> R.drawable.download_button

                            DownloadState.Downloading ->
                                R.drawable.downloading

                            else -> R.drawable.finished_downloading
                        }
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .animateContentSize()
                        .align(alignment = Alignment.CenterVertically)
                )
            }

        }
    //}
}
@Composable
fun SearchVideos(
    searchViewModel: SearchViewModel = viewModel()
) {
    val videos by searchViewModel.videos.collectAsStateWithLifecycle()
    val isLoading by searchViewModel.isLoading.collectAsStateWithLifecycle()
    val currentVideo by searchViewModel.currentVideo.collectAsStateWithLifecycle()
    val formats by searchViewModel.formats.collectAsStateWithLifecycle()
    val context=LocalContext.current
    var reload by remember { mutableStateOf(false) }



    var offset by remember { mutableFloatStateOf(0f) }
    val listState = rememberLazyListState()
    /*val loadMoreVideos: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(loadMoreVideos) {
        if(loadMoreVideos && !isLoading && !reload){
            reload=true
            searchViewModel.loadMoreVideos()
        }
    }*/
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
                    val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        //.firstOrNull { item -> item.offset <= offset.toInt() && offset.toInt() <= (item.offset + item.size) }
                    Log.d("DescarregarVideos","${last?.offset}")
                    if (last?.index==listState.layoutInfo.totalItemsCount-1 && !isLoading && !reload) {
                        searchViewModel.loadMoreVideos()
                        reload=true
                    }
                    delta

                },
            )
    ) {
        Log.d("DescarregarVideos","Total Videos: ${videos.count()}")
        items(videos
            ,key = {it.videoId}) {

            Item(it)
        }
    }

    if(formats.isNotEmpty() && currentVideo!=null) {
        FormatsDialog(formats, onClose = fun(selectedFormat: AdaptiveFormats?) {
            if (selectedFormat != null) {
                searchViewModel.setDownloading(currentVideo!!)
                searchViewModel.downloadVideo(selectedFormat, currentVideo!!, finished = fun(success: Boolean) {
                    val handler=Handler(Looper.getMainLooper()!!)
                    if(success) {
                        searchViewModel.setDownloaded(currentVideo!!)

                        handler.post {
                            Toast.makeText(
                                context, "Video \"${currentVideo!!.title}\" Descarregat Correctament",
                                Toast.LENGTH_LONG
                            ).show()
                            searchViewModel.resetDialog()
                        }

                    }else {
                        searchViewModel.setNotDownloaded(currentVideo!!)
                        handler.post {
                            Toast.makeText(
                                context, "Video \"${currentVideo!!.title}\" Error: No s'ha pogut Descarregat Correctament",
                                Toast.LENGTH_LONG
                            ).show()
                            searchViewModel.resetDialog()
                        }

                    }

                })
            }
            searchViewModel.resetFormats()
        })
    }

}