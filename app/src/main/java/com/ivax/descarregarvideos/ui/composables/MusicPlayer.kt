package com.ivax.descarregarvideos.ui.composables

import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.SurfaceType
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import com.ivax.descarregarvideos.services.PlaybackService
import kotlinx.coroutines.flow.update
import java.io.FileInputStream

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayer(modifier: Modifier,mediaViewModel: MediaViewModel= hiltViewModel()) {
    val context= LocalContext.current
    var player by remember { mutableStateOf<MediaController?>(null) }
    LaunchedEffect(Unit) {
        lateinit var controllerFuture: ListenableFuture<MediaController>
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                val mediaController = controllerFuture.get()
                mediaViewModel.setMediaController(mediaController)
                player = mediaViewModel.getMediaPlayer()
                player!!.repeatMode = Player.REPEAT_MODE_ALL
                if (player!!.isPlaying) {
                    val mediaItem = player!!.currentMediaItem
                    if (mediaItem != null) {
                        mediaViewModel.isMediaPlayerMaximized.postValue(true)
                        //setMetadata(mediaItem)
                    }

                }
                //val defaultTimeBar=binding.appBarMain.root.findViewById<DefaultTimeBar>(R.id.defaultTimeBar)
                //defaultTimeBar.

                player!!.addListener(object : Player.Listener {
                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int
                    ) {
                        Log.d("DescarregarVideos", "Old: ${oldPosition.positionMs}")
                        Log.d("DescarregarVideos", "New: ${newPosition.positionMs}")
                        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        Log.d("DescarregarVideos", "Playback Changed")
                        super.onPlaybackStateChanged(playbackState)
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        Log.d("DescarregarVideos", "Timeline Chnaged")
                        super.onTimelineChanged(timeline, reason)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        //hasNextAndPreviousMedia()
                        super.onIsPlayingChanged(isPlaying)
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        @Player.MediaItemTransitionReason reason: Int
                    ) {
                        if (mediaItem != null) {
                            //setMetadata(mediaItem)
                            val title = mediaItem.mediaMetadata.title
                            val uri = mediaItem.mediaMetadata.artworkUri
                            val playlistName = mediaItem.mediaMetadata.albumTitle
                            var bmp: Bitmap
                            var fileInStream = FileInputStream(uri.toString())
                            fileInStream.use {
                                bmp = BitmapFactory.decodeStream(it)
                            }
                            fileInStream.close()

                            mediaViewModel.setMetaData(playlistName?.toString(),bmp,title.toString())
                            /*mediaViewModel.playlistName.update {
                                playlistName?.toString()
                            }
                            mediaViewModel.thumbnail.update {
                                bmp
                            }
                            mediaViewModel.title.update {
                                title.toString()
                            }*/
                        }
                    }


                })
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            }
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())
        fun setMetadata(mediaItem: MediaItem) {
            //hasNextAndPreviousMedia()

        }
    }

    val ready by mediaViewModel.isMediaControllerReady.collectAsStateWithLifecycle()
    if(ready) {
        var showControls by remember { mutableStateOf(true) }
        val presentationState = rememberPresentationState(player!!)

        val metaDataUi by mediaViewModel.mediaStateUi.collectAsStateWithLifecycle()

        Box(modifier = modifier.background(Color.Black).clip(RoundedCornerShape(12.dp)).shadow(1.dp)) {

                Row(Modifier.align(alignment = Alignment.TopStart).zIndex(9999f).padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
                    if(metaDataUi!=null) {

                        Image(
                            bitmap = metaDataUi!!.artwork.asImageBitmap(),
                            contentDescription = "Thumbnail Video",
                            modifier=Modifier.width(86.dp)
                        )
                        Column {
                            Text(
                                text = metaDataUi!!.title.toString(),
                                color = Color.White,
                                modifier = Modifier.basicMarquee(),
                                fontSize = 18.sp
                            )
                            if(metaDataUi!!.playlistName!=null) {
                                Text(
                                    text = metaDataUi!!.playlistName.toString(),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                AndroidView(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).align(alignment = Alignment.BottomCenter).padding(16.dp),
                    factory = {

                        PlayerView(context).apply {
                            controllerAutoShow = false
                            this.showController()
                            this.player = player!!
                            useController = true
                            this.setShowSubtitleButton(false)
                            this.controllerHideOnTouch = false
                            this.controllerShowTimeoutMs = 0
                        }
                    }
                )

        }
    }
        /*Box(modifier) {
            PlayerSurface(
                player = player!!,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                modifier = Modifier
                    .resizeWithContentScale(
                        ContentScale.Fit,
                        presentationState.videoSizeDp
                    )
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null,
                    ) {
                        showControls = !showControls
                    },
            )


            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefaultTimeBar(context).apply {
                    setPosition(200L)
                }

            }
        }*/

    }
