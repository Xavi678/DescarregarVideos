package com.ivax.descarregarvideos.ui.composables

import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C.TIME_UNSET
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_GET_CURRENT_MEDIA_ITEM
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.compose.state.rememberNextButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPresentationState
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.ivax.descarregarvideos.classes.MathExtensions
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import com.ivax.descarregarvideos.services.PlaybackService
import kotlinx.coroutines.Runnable
import java.io.FileInputStream


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(mediaViewModel: MediaViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<MediaController?>(null) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var sliderTotalDuration by remember { mutableFloatStateOf(0f) }
    var isSliderChanging by remember { mutableStateOf(false) }
    LifecycleStartEffect(Unit) {
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


                //val defaultTimeBar=binding.appBarMain.root.findViewById<DefaultTimeBar>(R.id.defaultTimeBar)
                //defaultTimeBar.
                player!!.addListener(object : Player.Listener {
                    init {
                        val handler = Handler(Looper.myLooper()!!)
                        val runnable = object : Runnable {
                            override fun run() {
                                updateSlider()
                                handler.postDelayed(this, 1000)
                            }
                        }
                        handler.post(runnable)
                    }

                    fun updateSlider() {
                        if (!isSliderChanging) {

                            val commands = player?.availableCommands
                            if (commands?.contains(COMMAND_GET_CURRENT_MEDIA_ITEM) == true) {
                                val durationMs = player!!.duration

                                if (durationMs != TIME_UNSET) {
                                    val durationParsed = durationMs / 1000f
                                    val currentPosition = player!!.currentPosition
                                    val currentPositionParsed = currentPosition / 1000f
                                    sliderTotalDuration = durationParsed
                                    sliderPosition = currentPositionParsed
                                }
                            }
                        }
                    }

                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int
                    ) {
                        Log.d("DescarregarVideos", "Old: ${oldPosition.positionMs}")
                        Log.d("DescarregarVideos", "New: ${newPosition.positionMs}")
                        updateSlider()
                        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        Log.d("DescarregarVideos", "Playback Changed")
                        updateSlider()
                        super.onPlaybackStateChanged(playbackState)
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        Log.d("DescarregarVideos", "Timeline Chnaged")
                        updateSlider()
                        super.onTimelineChanged(timeline, reason)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        //hasNextAndPreviousMedia()
                        mediaViewModel.setPlaying(isPlaying)
                        super.onIsPlayingChanged(isPlaying)
                    }

                    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                        updateSlider()
                        super.onPlayWhenReadyChanged(playWhenReady, reason)
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        @Player.MediaItemTransitionReason reason: Int
                    ) {
                        updateSlider()
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

                            mediaViewModel.setMetaData(
                                playlistName?.toString(),
                                bmp,
                                title.toString()
                            )
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
        onStopOrDispose {
            player?.release()
            player = null
        }
    }

    val ready by mediaViewModel.isMediaControllerReady.collectAsStateWithLifecycle()
    if (ready) {
        var showControls by remember { mutableStateOf(true) }
        val presentationState = rememberPresentationState(player!!)

        val metaDataUi by mediaViewModel.mediaStateUi.collectAsStateWithLifecycle()
        val isMediaVisible by mediaViewModel.isMediaPlayerVisible.collectAsStateWithLifecycle()
        val isMediaPlayerMaximized by mediaViewModel.isMediaPlayerMaximized.collectAsStateWithLifecycle()
        val isPlaying = rememberPlayPauseButtonState(player!!)
        val nextButton = rememberNextButtonState(player!!)
        val previousButton = rememberPreviousButtonState(player!!)


        val modifierBox = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White),
                enabled = !isMediaPlayerMaximized
            ) {
                mediaViewModel.maximize()
            }
            .height(if (isMediaPlayerMaximized) 200.dp else 100.dp)

            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 0.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
        if (isMediaVisible) {
            Box(
                modifier = modifierBox
            ) {

                Row(
                    Modifier
                        .align(alignment = Alignment.TopStart)
                        .zIndex(100f)
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    if (metaDataUi != null) {

                        Image(
                            bitmap = metaDataUi!!.artwork.asImageBitmap(),
                            contentDescription = "Thumbnail Video",
                            modifier = Modifier
                                .width(86.dp)
                                .align(alignment = Alignment.CenterVertically)

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            if (isMediaPlayerMaximized) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Minimize/Maximize Player Button",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .zIndex(101f)
                                        .align(alignment = Alignment.End)
                                        .clickable(
                                            interactionSource =
                                                remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            mediaViewModel.minimize()
                                        }
                                )
                            }
                            Text(
                                text = metaDataUi!!.title.toString(),
                                color = Color.White,
                                modifier = Modifier.basicMarquee(),
                                fontSize = 18.sp
                            )
                            if (metaDataUi!!.playlistName != null) {
                                Text(
                                    text = metaDataUi!!.playlistName.toString(),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                Row(modifier = Modifier.align(alignment = Alignment.Center)) {
                    IconButton(onClick = {
                        player?.seekToPreviousMediaItem()
                    }, enabled = previousButton.isEnabled) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Audio Icon", tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        if (!isPlaying.showPlay) {
                            player?.stop()
                        } else {

                            player?.play()
                        }
                    }) {
                        Icon(
                            imageVector = if (!isPlaying.showPlay) Icons.Default.Pause else Icons.Default.PlayCircle,
                            contentDescription = "Play/Pause Button", tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        player?.seekToNextMediaItem()
                    }, enabled = nextButton.isEnabled) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Audio Icon", tint = Color.White
                        )
                    }

                }
                val modifierTimeBar = if (isMediaPlayerMaximized) {
                    Modifier.padding(12.dp)
                } else {
                    Modifier.padding(start = 12.dp, end = 12.dp, bottom = 0.dp)
                }

                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .then(modifierTimeBar)

                ) {
                    if (isMediaPlayerMaximized) {
                        Row(
                            modifier = Modifier
                                .align(alignment = Alignment.End)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = MathExtensions.toTime(sliderPosition),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "/", color = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = MathExtensions.toTime(sliderTotalDuration),
                                color = Color.White
                            )
                        }
                    }
                    Slider(
                        modifier = Modifier, value = sliderPosition, onValueChangeFinished = {
                        player?.seekTo((sliderPosition.toLong() * 1000))
                        isSliderChanging = false

                    }, onValueChange = {
                        isSliderChanging = true
                        sliderPosition = it


                    },
                        valueRange = 0f..sliderTotalDuration,
                        colors = SliderColors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            activeTickColor = Color.White,
                            inactiveTrackColor = Color.LightGray,
                            inactiveTickColor = Color.LightGray,
                            disabledThumbColor = Color.LightGray,
                            disabledActiveTrackColor = Color.LightGray,
                            disabledActiveTickColor = Color.LightGray,
                            disabledInactiveTrackColor = Color.LightGray,
                            disabledInactiveTickColor = Color.LightGray
                        )
                        , thumb = {
                            Box(Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .background(Color.White, CircleShape)
                            ) {

                            }
                        }
                        , track = { sliderState ->

                            val fraction by remember {
                                derivedStateOf {
                                    (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                                }
                            }

                            Box(Modifier
                                .fillMaxWidth()
                                .background(Color.White)) {
                                Box(
                                    Modifier
                                        .fillMaxWidth(fraction)
                                        .align(Alignment.CenterStart)
                                        .height(2.dp)
                                        .padding(end = 16.dp)
                                        .background(Color.Yellow, CircleShape)
                                )
                                Box(
                                    Modifier
                                        .fillMaxWidth(1f - fraction)
                                        .align(Alignment.CenterEnd)
                                        .height(1.dp)
                                        .padding(start = 16.dp)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        })

                }

            }
        }
    }

}
