package com.ivax.descarregarvideos.ui.composables

import android.content.ComponentName
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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

                        }
                    }


                })
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            }
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }
    val ready by mediaViewModel.isMediaControllerReady.collectAsStateWithLifecycle()
    if(ready) {
        var showControls by remember { mutableStateOf(true) }
        val presentationState = rememberPresentationState(player!!)
        AndroidView(
            modifier = modifier,
            factory = {

                PlayerView(context).apply {
                    this.player = player!!
                    useController = true
                    controllerAutoShow = true
                }
            }
        )
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
