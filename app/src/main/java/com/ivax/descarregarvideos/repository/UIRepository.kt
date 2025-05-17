package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.helpers.IUIManager
import com.ivax.descarregarvideos.helpers.UIManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UIRepository @Inject constructor(uiManager: IUIManager) {
    var showPlaylistMenu =uiManager.showPlaylistMenu
}