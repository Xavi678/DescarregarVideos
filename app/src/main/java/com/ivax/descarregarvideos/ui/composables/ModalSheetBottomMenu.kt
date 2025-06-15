package com.ivax.descarregarvideos.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.general.viewmodels.ModalSheetBottomMenuViewModel

@Composable
fun ModalSheetBottomMenu(
    selectedVideoId: String,
    playlistId: Int?,
    modalSheetBottomMenuViewModel: ModalSheetBottomMenuViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    /*val closeMenu by modalSheetBottomMenuViewModel.closeMenu.collectAsStateWithLifecycle()

    if (closeMenu) {
        onClose()
    }*/

    ShowBottomDialogVideoMenu(playlistId, onClose = fun(deleteVideo: Boolean,deleteVideoFromPlaylist: Boolean) {
        if (deleteVideo) {
            modalSheetBottomMenuViewModel.deleteVideo(selectedVideoId)
        }
        if(deleteVideoFromPlaylist){
            modalSheetBottomMenuViewModel.deleteVideoFromPlaylist(selectedVideoId,playlistId!!)
        }
        modalSheetBottomMenuViewModel.setBottomSheetVisibility(false)
        onClose()
    }, onShowPlayListMenu = fun() {
        modalSheetBottomMenuViewModel.showPlaylistMenu(true)
        modalSheetBottomMenuViewModel.setSelectedVideoId(selectedVideoId)

    })

    if (modalSheetBottomMenuViewModel.showPlaylistMenu.collectAsStateWithLifecycle().value) {

        AddPlaylistMenu(selectedVideoId, onClose = fun() {
            modalSheetBottomMenuViewModel.setBottomSheetVisibility(false)
            modalSheetBottomMenuViewModel.showPlaylistMenu(false)
            onClose()
        }, modalSheetBottomMenuViewModel)
    }
}