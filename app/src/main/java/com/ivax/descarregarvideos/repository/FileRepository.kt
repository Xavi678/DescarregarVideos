package com.ivax.descarregarvideos.repository

import com.ivax.descarregarvideos.helpers.FileWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(private val fileWriter: FileWriter) {
    suspend fun saveFile(fileNme: String,bytes: ByteArray){
        fileWriter.write(fileNme,bytes)
    }
}