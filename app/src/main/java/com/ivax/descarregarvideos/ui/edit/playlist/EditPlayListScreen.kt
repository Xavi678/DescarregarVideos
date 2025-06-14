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
    if (selectedVideoId != null) {
        ModalSheetBottomMenu(selectedVideoId!!, modalSheetBottomMenuViewModel, onClose = fun() {
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

    })
}


@Composable
fun Playlists(viewModel: EditPlaylistViewModel) {
    var playlistIdWithPositions by remember { mutableStateOf<List<Pair<VideosWithPositionFoo,Float>>>(emptyList()) }
    LaunchedEffect(viewModel.playlistIdWithPositions.collectAsStateWithLifecycle()) {
        val playlistIdWithPositions_ = viewModel.playlistIdWithPositions.value
        if (playlistIdWithPositions_ != null) {
            playlistIdWithPositions = playlistIdWithPositions_.map { it.to(0f) }
        }
    }
    var draggingItem by remember { mutableStateOf<LazyListItemInfo?>(null) }
    Log.d("DescarregarVideos", "Drag remember " + draggingItem?.key)
    val rememberLazyListState = rememberLazyListState()
    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState) {

        itemsIndexed(playlistIdWithPositions, key = { index, item ->
            item.first.videoId
        }) { index, item ->
            ListItem(
                item.first,
                getCurrentDelta =  fun(): Float{
                   return item.second
                },
                setCurrentDelta= fun(delta: Float){
                        //item.second=delta
                }
                ,
                index,
                rememberLazyListState,
                viewModel,
                setDraggingItem = fun(currentDraggingitem: LazyListItemInfo) {
                    draggingItem = currentDraggingitem
                },
                onDragEnd = fun(){
                    viewModel.detectChanges(playlistIdWithPositions)
                },
                getCurrentDraggingItem = fun(): LazyListItemInfo? { return draggingItem }
            ) { i, j ->
                playlistIdWithPositions =
                    playlistIdWithPositions.toMutableList().apply { add(i, removeAt(j)) }
            }
        }

    }
}

@Composable
fun ListItem(
    videosWithPositionFoo: VideosWithPositionFoo,
    getCurrentDelta: ()->Float,
    setCurrentDelta:(delta: Float)->Unit,
    index: Int,
    state: LazyListState,
    viewModel: EditPlaylistViewModel,
    getCurrentDraggingItem: () -> LazyListItemInfo?,
    setDraggingItem: (draggingItem: LazyListItemInfo) -> Unit,
    onDragEnd: ()->Unit,
    changePosition: (i: Int, j: Int) -> Unit

) {
    //var currentDelta by remember { mutableFloatStateOf(0f) }
    /*val offset by animateIntOffsetAsState(
        targetValue = if (isMoving) {
            Log.d("DescarregarVideos", "${currentDelta}")
            IntOffset(0, currentDelta)
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )*/
    var bmp: Bitmap
    var fileInStream = FileInputStream(videosWithPositionFoo.imgUrl)
    fileInStream.use {
        bmp = BitmapFactory.decodeStream(it)
    }

    fileInStream.close()
    Column(
        modifier = Modifier
            .graphicsLayer {
                translationY=getCurrentDelta()
            }
            .pointerInput(
                key1 = state
            ) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        //isMoving = true
                        val item = state.layoutInfo.visibleItemsInfo[index]
                        setDraggingItem(item)

                        Log.d("DescarregarVideos", "Drag Start " + item.key)
                    },
                    onDragEnd = {
                        setCurrentDelta(0f)
                        onDragEnd()
                        Log.d("DescarregarVideos", "Drag End ")
                    },
                    onDragCancel = {
                        setCurrentDelta(0f)
                        Log.d("DescarregarVideos", "Drag Cancel ")
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        var currentDelta=getCurrentDelta()
                        currentDelta+=dragAmount.y.toInt()
                        setCurrentDelta( currentDelta)
                        val draggingItem = getCurrentDraggingItem()
                        Log.d("DescarregarVideos","Drag onDrag current Item "+draggingItem?.key)
                        if (draggingItem != null) {
                            Log.d("DescarregarVideos", "Drag onDrag Not Null")
                            val dragItemOffset = draggingItem.offset
                            if (currentDelta < 0) {
                                val previousItem =
                                    state.layoutInfo.visibleItemsInfo.getOrNull(index - 1)
                                if (previousItem != null) {
                                    val dragPreviousItemSize = previousItem.size
                                    Log.d(
                                        "DescarregarVideos",
                                        "Offset previous; " + (dragItemOffset - currentDelta).toString()
                                    )
                                    Log.d(
                                        "DescarregarVideos",
                                        "Width previous; " + (dragPreviousItemSize / 2).toString()
                                    )
                                    if (dragItemOffset + currentDelta < (dragPreviousItemSize / 2)) {
                                        changePosition(index, previousItem.index)
                                        setDraggingItem(state.layoutInfo.visibleItemsInfo[previousItem.index])
                                        //change.consume()
                                    }
                                }

                            } else {
                                if (state.layoutInfo.totalItemsCount > index + 1) {
                                    val nextItem =
                                        state.layoutInfo.visibleItemsInfo.getOrNull(index + 1)
                                    if (nextItem != null) {
                                        val dragNextItemSize = nextItem.size
                                        if (dragItemOffset + currentDelta > (dragNextItemSize / 2)) {
                                            changePosition(index, nextItem.index)
                                            setDraggingItem(state.layoutInfo.visibleItemsInfo[nextItem.index])

                                        }
                                    }

                                }
                            }
                        }
                    }
                )
            }) {


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