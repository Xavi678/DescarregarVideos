package com.ivax.descarregarvideos

import android.app.Application
import com.ivax.descarregarvideos.datasource.VideoLocalSource
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.HiltAndroidApp
import database.AppDatabase

@HiltAndroidApp
class MyApplication : Application() {
    //private val applicationScope = CoroutineScope(SupervisorJob())

    private val dataBase by lazy { AppDatabase.getDatabase(this) }
    private val videoLocalSource by lazy { VideoLocalSource(dataBase.videoDao()) }
    val videoRepository by lazy { VideoRepository(videoLocalSource) }
}