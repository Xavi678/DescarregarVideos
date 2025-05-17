package com.ivax.descarregarvideos.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IUIManager {
    val showPlaylistMenu : MutableStateFlow<Boolean>
}