package com.ivax.descarregarvideos.di.module

import android.content.Context
import com.ivax.descarregarvideos.helpers.CustomFileWriter
import com.ivax.descarregarvideos.helpers.FileWriter
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {

    fun provideFileWriter(@ApplicationContext context: Context): FileWriter {
        return CustomFileWriter(context)
    }
}