package com.ivax.descarregarvideos.di.module

import android.content.Context
import androidx.room.Room
import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.helpers.CustomFileWriter
import com.ivax.descarregarvideos.helpers.FileWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {
    @Singleton
    @Provides
    fun provideFileWriter(@ApplicationContext context: Context): FileWriter {
        return CustomFileWriter(context)
    }
    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, AppDatabase::class.java, "DescarregarVideosDB"
    ).build()
    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.videoDao()
}