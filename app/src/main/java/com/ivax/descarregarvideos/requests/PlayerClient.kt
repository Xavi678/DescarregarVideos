package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayerClient(val clientName: String="ANDROID", val clientVersion:String="20.10.38",
                        //val deviceMake:String="Apple",
                        //val deviceModel:String="iPhone16,2",
                        val platform:String="MOBILE",
                        val osName:String="Android", val osVersion:String="11",
                        var visitorData:String="",
                        val hl:String="en", val gl: String="US",
                        val utcOffsetMinutes:String="0") {
}