package com.ivax.descarregarvideos.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ivax.descarregarvideos.helpers.ApiClient
import com.ivax.descarregarvideos.helpers.CustomFileWriter
import com.ivax.descarregarvideos.helpers.FileWriter
import com.ivax.descarregarvideos.helpers.IApiClient
import com.ivax.descarregarvideos.helpers.IMediaHelper
import com.ivax.descarregarvideos.helpers.IUIManager
import com.ivax.descarregarvideos.helpers.MediaHelper
import com.ivax.descarregarvideos.helpers.UIManager
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
    fun provideUIManager() : IUIManager {
        return UIManager()
    }
    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, AppDatabase::class.java, "DescarregarVideosDB"
    ).addMigrations(MIGRATION_5_6).addMigrations(MIGRATION_9_10).addMigrations(MIGRATION_11_12).build()
    @Singleton
    @Provides
    fun provideVideoDao(db: AppDatabase) = db.videoDao()
    @Singleton
    @Provides
    fun provideApiClient() : IApiClient{
        return ApiClient()
    }
    @Singleton
    @Provides
    fun providePlaylistDao(db: AppDatabase) = db.playlistDao()
    @Singleton
    @Provides
    fun providePlaylistSavedVideoCrossRefDao(db: AppDatabase) = db.playlistSavedVideoCrossRefDao()
    @Singleton
    @Provides
    fun provideMediaHelper(@ApplicationContext context: Context) : IMediaHelper{
        return MediaHelper(context)
    }
    val MIGRATION_5_6 = object: Migration (5, 6) {
        override fun migrate (database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE playlist")
            database.execSQL("CREATE TABLE playlist(" +
                    "playListId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "name TEXT)")
        }
    }

    val MIGRATION_9_10 = object: Migration (9, 10) {
        override fun migrate (database: SupportSQLiteDatabase) {
            database.execSQL("DELETE FROM PlaylistSavedVideoCrossRef")
        }
    }
    val MIGRATION_11_12 = object: Migration (11, 12) {
        override fun migrate (database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE SavedVideo ADD author TEXT DEFAULT NULL")
        }
    }
}