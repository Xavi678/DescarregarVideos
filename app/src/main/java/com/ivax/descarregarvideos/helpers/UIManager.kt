package com.ivax.descarregarvideos.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class UIManager : IUIManager {
    override val showPlaylistMenu=MutableStateFlow(false)
    override val selectedVideoId=MutableStateFlow<String?>(null)


}