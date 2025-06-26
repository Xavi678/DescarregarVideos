package com.ivax.descarregarvideos

import android.content.res.Configuration
import android.graphics.Color.WHITE
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ivax.descarregarvideos.databinding.ActivityMainBinding
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ivax.descarregarvideos.general.viewmodels.ModalSheetBottomMenuViewModel
import com.ivax.descarregarvideos.ui.composables.MusicPlayer
import com.ivax.descarregarvideos.ui.edit.playlist.EditPlaylistScreen
import com.ivax.descarregarvideos.ui.edit.playlist.EditPlaylistViewModel
import com.ivax.descarregarvideos.ui.routes.Route
import com.ivax.descarregarvideos.ui.routes.Route.Playlists
import com.ivax.descarregarvideos.ui.playlists.PlaylistScreen
import com.ivax.descarregarvideos.ui.playlists.PlaylistsViewModel
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel
import com.ivax.descarregarvideos.ui.saved.videos.SearchAudioScreen
import com.ivax.descarregarvideos.ui.search.SearchScreen
import com.ivax.descarregarvideos.ui.search.SearchViewModel
import com.ivax.descarregarvideos.ui.theme.MainAppTheme


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var player: MediaController
    private lateinit var btnSkipBackward: ImageButton
    private lateinit var btnSkipForward: ImageButton
    private lateinit var btnMinimizePlayer: ImageButton
    private lateinit var btnDestroyPlayer: ImageButton

    private lateinit var tbxPlaylistName: TextView


    private val mediaViewModel: MediaViewModel by lazy {
        ViewModelProvider(this).get(MediaViewModel::class.java)
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onStart() {
        /*binding.appBarMain.composeViewMain.setContent {
            MainWrapper()

        }
        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                val mediaController = controllerFuture.get()
                mediaViewModel.setMediaController(mediaController)
                player = mediaViewModel.getMediaPlayer()
                binding.appBarMain.playerView.player = player
                player.repeatMode = Player.REPEAT_MODE_ALL
                if (player.isPlaying) {
                    val mediaItem = player.currentMediaItem
                    if (mediaItem != null) {
                        mediaViewModel.isMediaPlayerMaximized.postValue(true)
                        setMetadata(mediaItem)
                    }

                }
                //val defaultTimeBar=binding.appBarMain.root.findViewById<DefaultTimeBar>(R.id.defaultTimeBar)
                //defaultTimeBar.
                player.addListener(object : Player.Listener {
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
                        hasNextAndPreviousMedia()
                        super.onIsPlayingChanged(isPlaying)
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        @MediaItemTransitionReason reason: Int
                    ) {
                        if (mediaItem != null) {
                            setMetadata(mediaItem)

                        }
                    }


                })
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            }
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())

*/
        super.onStart()
    }

    override fun onStop() {

        //MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        StatusBraConfig()
        setContent {
            MainAppTheme {
                MainWrapper()

            }
        }
    }

    fun StatusBraConfig(){
        val color=WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(android.view.WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)

                // Adjust padding to avoid overlap
                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = color
        }
    }

    @Composable
    fun HomeNavHost(
        modifier: Modifier,
        navController: NavHostController,
        startDestination: Any,
        navigateTo: (route: String) -> Unit
    ) {
        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {

            composable<Route.Search> {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(viewModel)
            }
            composable<Playlists> {
                val viewModel = hiltViewModel<PlaylistsViewModel>()
                PlaylistScreen(viewModel, fun(playlistId: Int) {
                    navController.navigate(Route.EditPlaylist(playlistId))
                })
            }
            composable<Route.SavedAudio> {
                val viewModel = hiltViewModel<SavedVideosViewModel>()
                val modalSheetBottomMenuViewModel = hiltViewModel<ModalSheetBottomMenuViewModel>()
                SearchAudioScreen(viewModel, modalSheetBottomMenuViewModel)
            }

            composable<Route.EditPlaylist> {
                val arg = it.toRoute<Route.EditPlaylist>()
                val viewModel = hiltViewModel<EditPlaylistViewModel>()
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "playlistId",
                    arg.playlistId
                )
                EditPlaylistScreen(viewModel){
                    navController.navigate(Route.Playlists) {
                        restoreState = false

                    }
                }
            }

        }
    }

    @Composable
    fun MainWrapper() {
        val context = LocalContext.current

        val navController = rememberNavController()
        MainScaffold {
            navController.navigate(it)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScaffold(
        navigateTo: (route: String) -> Unit
    ) {
        val navController = rememberNavController()
        var showMusicPLayer by remember { mutableStateOf(true) }
        var heightMusicPLayerOffset by remember { mutableFloatStateOf(0f) }
        var heightMusicPlayer by remember { mutableFloatStateOf(0f) }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    //Log.d("DescarregarVideos","Offset Scroll ${delta} source: ${source} Height Offset: ${heightMusicPLayerOffset}")
                    val newOffset = heightMusicPLayerOffset + delta

                    heightMusicPLayerOffset= newOffset.coerceIn(-heightMusicPlayer,0f)

                    Log.d("DescarregarVideos","Scroll heightMusicPLayerOffset ${heightMusicPLayerOffset}")
                    //heightOffset = newOffset.coerceIn(-heightOffset, 0f)
                    showMusicPLayer = newOffset >= 0f
                    //showMusicPLayer = delta >= 0
                    return Offset.Zero
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    Log.d("DescarregarVideos","Offset Scroll Finished Available: " +
                            "${available.y} Consumed: ${consumed.y} source: ${source}")
                    return super.onPostScroll(consumed, available, source)
                }
            }
        }

        val currentBackState by navController.currentBackStackEntryAsState()
        val ruta = currentBackState.getRoute()


        Scaffold(modifier = Modifier.nestedScroll(nestedScrollConnection),
            //contentWindowInsets = WindowInsets.safeContent,
            containerColor =MaterialTheme.colorScheme.secondary ,
            contentColor =MaterialTheme.colorScheme.secondary,
            /*topBar = {

                Row {
                    if (ruta?.hasBackButton == true) {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.Playlists) {
                                    restoreState = false

                                }
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow Back",
                                tint =  MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }

                /*TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Row {
                            if (ruta?.hasBackButton == true) {
                                IconButton(onClick = {
                                    navController.navigate(Route.Playlists){
                                        restoreState=false

                                    }
                                }, modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Arrow Back"
                                    )
                                }
                            }
                            Text(text = ruta?.label.toString(),modifier = Modifier.align(alignment = Alignment.CenterVertically))
                        }
                    }
                )*/
            },*/
            bottomBar = {

                NavBar(navController)
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HomeNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    navController = navController,
                    startDestination = Route.Search,
                    navigateTo = navigateTo
                )

                MusicPlayer(shouldShow = showMusicPLayer){
                    heightMusicPlayer=it
                }

            }
        }
    }

    @Composable
    fun NavBar(navController: NavController) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val navItems = listOf(
            Route.Search,
            Route.SavedAudio,
            Route.Playlists
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            navItems.forEach {
                NavigationBarItem(
                    selected = currentBackStackEntry?.destination?.hasRoute(it::class) == true,
                    onClick = {
                        navController.navigate(it)
                        {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(it.icon!!),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(it.label)
                    },
                    colors = NavigationBarItemColors(
                        selectedIndicatorColor = MaterialTheme.colorScheme.surface,
                        disabledIconColor = Color.LightGray,
                        disabledTextColor = Color.LightGray,
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.surface,
                        unselectedIconColor = MaterialTheme.colorScheme.surface,
                        unselectedTextColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            /*NavigationBarItem(
                selected = false, onClick = {
                    navController.navigate(Route.SavedAudio) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.download_rounded_base),
                        contentDescription = null,
                    )
                },
                label = {
                    Text("Saved Audio")
                }
            )
            NavigationBarItem(
                selected = false, onClick = {
                    navController.navigate(Route.Playlists) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.collection_fill),
                        contentDescription = null,
                    )
                },
                label = {
                    Text("Playlists")
                }
            )*/


        }
    }
}

private fun NavBackStackEntry?.getRoute(): Route? {
    return this?.let {
        when {
            destination.hasRoute<Route.Search>() -> Route.Search
            destination.hasRoute<Route.SavedAudio>() -> Route.SavedAudio
            destination.hasRoute<Route.Playlists>() -> Route.Playlists
            destination.hasRoute<Route.EditPlaylist>() -> Route.EditPlaylist.Companion.get()
            else -> null
        }
    }
}
