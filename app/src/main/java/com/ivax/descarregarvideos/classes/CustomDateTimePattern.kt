package com.ivax.descarregarvideos.classes

import java.time.format.DateTimeFormatter

fun getCustomDateTimePattern(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
}