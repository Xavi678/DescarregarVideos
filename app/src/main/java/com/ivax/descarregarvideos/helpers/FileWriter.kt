package com.ivax.descarregarvideos.helpers

interface FileWriter {
    suspend fun write(bytes: ByteArray)
}