package com.ivax.descarregarvideos.helpers

interface FileWriter {
    suspend fun write(fileNme: String,bytes: ByteArray): String
}