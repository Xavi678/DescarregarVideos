package com.ivax.descarregarvideos.ui.edit.playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.general.viewmodels.ModalSheetBottomMenuViewModel
import com.ivax.descarregarvideos.ui.composables.ModalSheetBottomMenu
import com.ivax.descarregarvideos.ui.composables.PlayButton
import com.ivax.descarregarvideos.ui.composables.bounceClick
import java.io.FileInputStream

@Composable
fun EditPlaylistScreen(
    viewModel: EditPlaylistViewModel,
    modalSheetBottomMenuViewModel: ModalSheetBottomMenuViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Top(viewModel)
        Spacer(modifier = Modifier.width(8.dp))
        Playlists(viewModel)
    }
    val selectedVideoId by
    viewModel.bottomSheetParameter.collectAsStateWithLifecycle()
    val selectedPlaylistId by viewModel.selectedPlaylistId.collectAsStateWithLifecycle()
    if (selectedVideoId != null) {
        ModalSheetBottomMenu(selectedVideoId!!,selectedPlaylistId!!, modalSheetBottomMenuViewModel, onClose = fun() {
            viewModel.resetSelectedVideo()

        })
    }

}

@Composable
fun Top(viewModel: EditPlaylistViewModel) {

    val playlist by viewModel.playlist.collectAsState()
    Text(playlist?.name.toString())
    Spacer(modifier = Modifier.width(8.dp))
    PlayButton(onClickDelegate = {
        viewModel.playAll()
    })
}


@Composable
fun Playlists(viewModel: EditPlaylistViewModel) {
    var playlistIdWithPositions by remember { mutableStateOf<List<VideosWithPositionFoo>>(emptyList()) }
    val _playlistIdWithPositions by viewModel.playlistIdWithPositions.collectAsStateWithLifecycle()
    if (_playlistIdWithPositions != null) {
        playlistIdWithPositions = _playlistIdWithPositions!!
    }

    var draggingItem by remember { mutableStateOf<LazyListItemInfo?>(null) }
    Log.d("DescarregarVideos", "Drag remember " + draggingItem?.key)
    val rememberLazyListState = rememberLazyListState()
    var currentDelta by remember { mutableFloatStateOf(0f) }
    var index by remember { mutableStateOf<Int?>(null) }
    fun changePosition(i: Int, j: Int) {
        val tmp = playlistIdWithPositions.toMutableList().apply {

            val removed = removeAt(j)
            add(i, removed)
        }
        tmp.forEachIndexed {
                           index, videosWithPositionFoo ->
            videosWithPositionFoo.position=index
        }

        playlistIdWithPositions = tmp

        Log.d("DescarregarVideos", i.toString())
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(
                key1 = rememberLazyListState
            ) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        val found = rememberLazyListState.layoutInfo.visibleItemsInfo
                            .firstOrNull { item -> item.offset <= offset.y.toInt() && offset.y.toInt() <= (item.offset + item.size) }
                        found?.let {
                            /*Log.d("DescarregarVideos", "Drag Start Offset= " + it.offset)
                            Log.d(
                                "DescarregarVideos",
                                "Drag Start Offset+Size= " + (it.offset + it.size)
                            )
                            Log.d("DescarregarVideos", "Drag Start Y= " + offset.y.toInt())
*/
                            index = it.index
                            draggingItem = it
                        }
                    },
                    onDragEnd = {
                        currentDelta = 0f
                        draggingItem = null
                        index = null
                        viewModel.detectChanges(playlistIdWithPositions = playlistIdWithPositions)
                        Log.d("DescarregarVideos", "Drag End ")
                    },
                    onDragCancel = {
                        currentDelta = 0f
                        draggingItem = null
                        index = null
                        //Log.d("DescarregarVideos", "Drag Cancel ")
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        currentDelta += dragAmount.y.toInt()
                        //Log.d("DescarregarVideos", "Drag onDrag current Item " + draggingItem?.key)
                        if (draggingItem != null && index != null) {
                            //Log.d("DescarregarVideos", "Drag onDrag Not Null")
                            val dragItemOffset = draggingItem!!.offset
                            if (currentDelta < 0) {
                                Log.d("DescarregarVideos", "Drag Current delta $currentDelta")
                                val previousItem =
                                    rememberLazyListState.layoutInfo.visibleItemsInfo.getOrNull(
                                        index!! - 1
                                    )
                                if (previousItem != null) {
                                    val dragPreviousItemSize = previousItem.size
                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag dragItemOffset + Current delta ${dragItemOffset + currentDelta}"
                                    )
                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag previousItem.offset  + (dragPreviousItemSize/2) ${(previousItem.offset + (dragPreviousItemSize / 2))}"
                                    )
                                    if ((dragItemOffset + currentDelta) < (previousItem.offset + (dragPreviousItemSize / 2))) {
                                        changePosition(index!!, previousItem.index)
                                        draggingItem = previousItem
                                        index = previousItem.index
                                        currentDelta = 0f
                                        //currentDelta += draggingItem!!.offset - previousItem.offset
                                    }
                                }

                            } else {
                                if (rememberLazyListState.layoutInfo.totalItemsCount > index!! + 1) {
                                    val nextItem =
                                        rememberLazyListState.layoutInfo.visibleItemsInfo.getOrNull(
                                            index!! + 1
                                        )
                                    if (nextItem != null) {
                                        val dragNextItemSize = nextItem.size
                                        Log.d(
                                            "DescarregarVideos",
                                            "Drag (draggingItem!!.size+dragItemOffset) + Current delta ${(draggingItem!!.size + dragItemOffset) + currentDelta}"
                                        )
                                        Log.d(
                                            "DescarregarVideos",
                                            "Drag nextItem.offset  + (dragNextItemSize/2) ${(nextItem.offset + (dragNextItemSize / 2))}"
                                        )
                                        if ((draggingItem!!.size + dragItemOffset) + currentDelta > (nextItem.offset + (dragNextItemSize / 2))) {
                                            changePosition(index!!, nextItem.index)
                                            draggingItem = nextItem
                                            index = nextItem.index
                                            currentDelta = 0f
                                            //currentDelta += draggingItem!!.offset - nextItem.offset
                                        }
                                    }

                                }
                            }
                        }
                    }
                )
            }, state = rememberLazyListState
    ) {

        itemsIndexed(playlistIdWithPositions, key = { idx, item ->
            item.videoId
        }) { idx, item ->

            val modifier = if (idx == index) {
                //Log.d("DescarregarVideos", "Modifier Drag Index= " + index)
                Modifier
                    .zIndex(1f)
                    .graphicsLayer {
                        translationY = currentDelta
                    }
            } else {
                Modifier
            }
            ListItem(
                item,
                idx,
                rememberLazyListState,
                viewModel,
                modifier
            )
        }

    }
}

@Composable
fun ListItem(
    videosWithPositionFoo: VideosWithPositionFoo,

    index: Int,
    state: LazyListState,
    viewModel: EditPlaylistViewModel,
    modifier: Modifier

) {

    var bmp: Bitmap
    var fileInStream = FileInputStream(videosWithPositionFoo.imgUrl)
    fileInStream.use {
        bmp = BitmapFactory.decodeStream(it)
    }

    fileInStream.close()
    Column(
        modifier = modifier

    ) {


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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = videosWithPositionFoo.title
                )
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.download_rounded_base),
                        contentDescription = "Download Icon"
                    )
                    Text(
                        text = videosWithPositionFoo.downloadDateFormatted.toString(),
                        fontSize = 11.sp,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

            }
            IconButton(onClick = {
                viewModel.setBottomSheetVideoId(videosWithPositionFoo.videoId)
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
}