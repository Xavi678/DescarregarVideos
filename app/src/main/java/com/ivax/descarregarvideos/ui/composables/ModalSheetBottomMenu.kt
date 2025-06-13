package com.ivax.descarregarvideos.ui.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.general.viewmodels.ModalSheetBottomMenuViewModel

@Composable
fun ModalSheetBottomMenu(selectedVideoId: String,modalSheetBottomMenuViewModel : ModalSheetBottomMenuViewModel= hiltViewModel(),onClose: ()->Unit) {

        ShowBottomDialogVideoMenu(selectedVideoId, onClose = fun(deleteVideo: Boolean) {
            if (deleteVideo) {
                modalSheetBottomMenuViewModel.deleteVideo(selectedVideoId)
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
        },modalSheetBottomMenuViewModel)
    }
}