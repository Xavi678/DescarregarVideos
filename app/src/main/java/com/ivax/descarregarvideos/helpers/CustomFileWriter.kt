package com.ivax.descarregarvideos.helpers

import android.content.Context
import java.util.Date
import javax.inject.Inject

class CustomFileWriter @Inject constructor(private val appContext: Context): FileWriter {
    override suspend fun write(bytes: ByteArray) {
        appContext.openFileOutput(
            "${Date().toString()}.mp4",
            Context.MODE_PRIVATE
        )
            .use {

                it?.write(bytes)
            }
    }

}