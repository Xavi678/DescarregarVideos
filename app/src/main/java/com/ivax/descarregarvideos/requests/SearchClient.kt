package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchClient(
    val clientName: String = "WEB",
    val clientVersion: String = "2.20250307.01.00",
    val deviceMake: String = "",
    val deviceModel: String = "",
    val platform: String = "DESKTOP",
    val osName: String = "Windows",
    val osVersion: String = "10.03",
    val visitorData: String = "CgtDLVlyd0FVYUlPcyjg3Ly-BjIiCgJFUxIcEhgSFhMLFBUWFwwYGRobHB0eHw4PIBAREiEgJw%3D%3D",
    val hl: String = "en",
    val gl: String = "US",
    val utcOffsetMinutes: String = "0",
    val browserName: String = "Firefox",
    val browserVersion: String = "136.0",
    val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0,gzip(gfe)"
) {
}