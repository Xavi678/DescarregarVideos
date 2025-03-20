package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.helpers.FileWriter
import javax.inject.Inject

class FileRepository @Inject constructor(private val fileWriter: FileWriter) {
    suspend fun saveFile(bytes: ByteArray){
        fileWriter.write(bytes)
    }
}