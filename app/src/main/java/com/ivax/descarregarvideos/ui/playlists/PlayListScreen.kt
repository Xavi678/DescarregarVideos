package com.ivax.descarregarvideos.ui.playlists

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.custom.composables.SearchComposable
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel


@Composable
fun PlaylistScreen(playlistsViewModel: PlaylistsViewModel = viewModel()){
    Column {
        SearchContentWrapper()
    }
}

@Composable
fun SearchContentWrapper(playlistsViewModel: PlaylistsViewModel = viewModel()){
    SearchComposable(onClickInvoker = fun (text: String) {
        playlistsViewModel.filterPlaylist(text)
        //savedVideosViewModel.filterSavedVideos(text)
    })
}