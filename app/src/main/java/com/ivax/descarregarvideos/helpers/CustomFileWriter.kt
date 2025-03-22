package com.ivax.descarregarvideos.helpers

import android.content.Context
import java.util.Date
import javax.inject.Inject

class CustomFileWriter @Inject constructor(private val appContext: Context): FileWriter {
    override suspend fun write(fileNme: String,bytes: ByteArray) {
        var out=appContext.openFileOutput(
            "videos/${fileNme}.mp4",
            Context.MODE_PRIVATE
        )
            .use {

                it?.write(bytes)
            }

    }

}