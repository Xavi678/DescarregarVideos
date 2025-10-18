package com.ivax.descarregarvideos.ui.edit.playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.ivax.descarregarvideos.ui.composables.PlayShuffle
import com.ivax.descarregarvideos.ui.composables.bounceClick
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.FileInputStream

@Composable
fun EditPlaylistScreen(
    viewModel: EditPlaylistViewModel,
    modalSheetBottomMenuViewModel: ModalSheetBottomMenuViewModel = hiltViewModel(),
    backClick: ()->Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        BackButton(backClick)
        Top(viewModel)
        Spacer(modifier = Modifier.height(8.dp))
        Playlists(viewModel)
    }
    val selectedVideoId by
    viewModel.bottomSheetParameter.collectAsStateWithLifecycle()
    val selectedPlaylistId by viewModel.selectedPlaylistId.collectAsStateWithLifecycle()
    if (selectedVideoId != null) {
        ModalSheetBottomMenu(selectedVideoId!!,selectedPlaylistId!!, modalSheetBottomMenuViewModel, onClose = fun() {
            viewModel.resetSelectedVideo()

        }, updated = fun(){
            viewModel.refresh()
        })
    }

}

@Composable
fun BackButton(backClick: () -> Unit) {
    IconButton(
        onClick = {
            backClick()
        },
        interactionSource = remember { MutableInteractionSource() },
        //modifier = Modifier.align(alignment = Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Arrow Back",
            tint =  MaterialTheme.colorScheme.surface
        )
    }
}
@Composable
fun Top(viewModel: EditPlaylistViewModel) {

    val playlist by viewModel.playlist.collectAsState()
    Text(playlist?.name.toString(), color = MaterialTheme.colorScheme.surface, fontSize = 22.sp,
        fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    Row {

        PlayButton(onClickDelegate = {
            viewModel.playAll()
        })

        Spacer(modifier = Modifier.width(4.dp))

        PlayShuffle(onClickDelegate = fun() {
            viewModel.shuffle()
        })
    }
}


@Composable
fun Playlists(viewModel: EditPlaylistViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var scrollJob by remember { mutableStateOf<Job?>(null) }

    var playlistIdWithPositions by remember { mutableStateOf<List<VideosWithPositionFoo>>(emptyList()) }
    val _playlistIdWithPositions by viewModel.playlistIdWithPositions.collectAsStateWithLifecycle()
    Log.d("DescarregarVideosBorrats", "Valors Actuals ${playlistIdWithPositions.size} ${_playlistIdWithPositions?.size}")
    //playlistIdWithPositions = _playlistIdWithPositions!!
    //LaunchedEffect(Unit) {

    if (_playlistIdWithPositions != null && playlistIdWithPositions.size!=_playlistIdWithPositions!!.size) {
        playlistIdWithPositions = _playlistIdWithPositions!!
    }
    //}
    Log.d("DescarregarVideos","Changed: ${playlistIdWithPositions.count()}")

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
               Unit
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
                        Log.d("DescarregarVideos","Drag Amount ${currentDelta}")
                        //Log.d("DescarregarVideos", "Drag onDrag current Item " + draggingItem?.key)
                        if (draggingItem != null && index != null) {
                            //Log.d("DescarregarVideos", "Drag onDrag Not Null")
                            val dragItemOffset = draggingItem!!.offset
                            val startOffset = dragItemOffset + dragAmount.y
                            val endOffset = (dragItemOffset+draggingItem!!.size) + dragAmount.y
                            if (currentDelta < 0) {
                                val previousItem =
                                    rememberLazyListState.layoutInfo.visibleItemsInfo.getOrNull(
                                        index!! - 1
                                    )


                                if (previousItem != null) {
                                    val dragPreviousItemSize = previousItem.size

                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag draggingItem!!.offset " +
                                                "${draggingItem!!.offset}"
                                    )
                                    Log.d("DescarregarVideos", "Drag Current delta $currentDelta")
                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag draggingItem!!.size " +
                                                "${draggingItem!!.size}"
                                    )
                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag ((draggingItem!!.offset +draggingItem!!.size) + currentDelta) " +
                                                "${((draggingItem!!.offset +draggingItem!!.size) + currentDelta)}"
                                    )
                                    Log.d("DescarregarVideos","Drag previous Item offset ${previousItem.offset}")
                                    Log.d(
                                        "DescarregarVideos",
                                        "Drag (previousItem.offset + (dragPreviousItemSize /2)) " +
                                                "${dragPreviousItemSize}"
                                    )

                                    if (((draggingItem!!.offset +(draggingItem!!.size/2)) + currentDelta) < (previousItem.offset + (dragPreviousItemSize /2))) {
                                        changePosition(index!!, previousItem.index)
                                        draggingItem = previousItem
                                        index = previousItem.index
                                        currentDelta = 0f
                                        if(index==0){
                                            scrollJob=coroutineScope.launch {
                                                rememberLazyListState.scrollToItem(0,0)
                                            }
                                        }
                                    }
                                }

                            } else {
                                if (rememberLazyListState.layoutInfo.totalItemsCount > index!! + 1) {

                                    val firstVisible=rememberLazyListState.firstVisibleItemIndex
                                    Log.d("DescarregarVideos","First Visible ${firstVisible}")
                                    val nextItem =
                                        rememberLazyListState.layoutInfo.visibleItemsInfo.getOrNull(
                                            (index!! + 1)-firstVisible
                                        )
                                    /*val scroll=(endOffset - rememberLazyListState.layoutInfo.viewportStartOffset).takeIf { diff ->
                                        Log.d("DescarregarVideos","Scroll ViewPort Diff: ${diff}")
                                        diff < 0
                                    }*/
                                    if(endOffset>rememberLazyListState.layoutInfo.viewportEndOffset){
                                       val scroll= endOffset-rememberLazyListState.layoutInfo.viewportEndOffset
                                        Log.d("DescarregarVideos","ScrollJob Active: ${scrollJob?.isActive}")
                                        if (scrollJob?.isActive != true){
                                            scrollJob = coroutineScope.launch {

                                                rememberLazyListState.scrollBy(scroll)

                                            }
                                        }
                                        Log.d("DescarregarVideos","Scroll ViewPort: ${scroll} End Offset VP:" +
                                                " ${rememberLazyListState.layoutInfo.viewportEndOffset}" +
                                                " Start Offset VP: ${rememberLazyListState.layoutInfo.viewportStartOffset}")
                                    }

                                    if (nextItem != null) {
                                        val dragNextItemSize = nextItem.size
                                        Log.d(
                                            "DescarregarVideos",
                                            "Drag (draggingItem!!.size+dragItemOffset) + Current delta ${(draggingItem!!.size + dragItemOffset) + currentDelta}"
                                        )
                                        Log.d(
                                            "DescarregarVideos",
                                            "Drag nextItem.offset  + (dragNextItemSize/2) ${(nextItem.offset + (dragNextItemSize/2))}"
                                        )
                                        if ((draggingItem!!.offset +(draggingItem!!.size/2)) + currentDelta > (nextItem.offset + (dragNextItemSize /2))) {
                                            changePosition(index!!, nextItem.index)
                                            draggingItem = nextItem
                                            index = nextItem.index
                                            currentDelta = 0f
                                            if(index==1){
                                                scrollJob=coroutineScope.launch {
                                                    rememberLazyListState.scrollToItem(0,0)
                                                }
                                            }
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
                    .zIndex(100f).
                    background(MaterialTheme.colorScheme.primary)
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
                            onClick = {},

                            )
                )
                Text(
                    text = videosWithPositionFoo.duration,
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
                    text = videosWithPositionFoo.title,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 16.sp
                )
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.download_rounded_base),
                        contentDescription = "Download Icon",
                        tint = MaterialTheme.colorScheme.surface
                    )
                    Spacer(modifier=Modifier.width(8.dp))
                    Text(
                        text = videosWithPositionFoo.downloadDateFormatted,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically,
                            )
                    )
                }

            }
            IconButton(onClick = {
                viewModel.setBottomSheetVideoId(videosWithPositionFoo.videoId)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.three_dots),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .animateContentSize()
                        .align(alignment = Alignment.CenterVertically)
                )
            }

        }
        //HorizontalDivider(Modifier.padding(4.dp), color = Color.LightGray)
    }
}