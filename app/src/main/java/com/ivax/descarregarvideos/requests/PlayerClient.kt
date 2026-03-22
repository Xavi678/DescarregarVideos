package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayerClient(val clientName: String="ANDROID_VR", val clientVersion:String="1.60.19",
                        //val deviceMake:String="Apple",
                        //val deviceModel:String="iPhone16,2",
                        val platform:String="MOBILE",
                        val osName:String="Android", val osVersion:String="12L",
                        var visitorData:String="",
                        val hl:String="en", val gl: String="US",
                        val utcOffsetMinutes:String="0") {
}