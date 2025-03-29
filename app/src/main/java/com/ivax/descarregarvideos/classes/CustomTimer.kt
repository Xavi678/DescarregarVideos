package com.ivax.descarregarvideos.classes

import java.util.Timer

class CustomTimer(private val callback: ()-> Unit) {

    private var timerTask: CustomTimerTask?=null

    fun schedule() {
        if(timerTask!=null){
            cancel()
        }
        timerTask=CustomTimerTask(callback)
        Timer().schedule(timerTask, 0,1000L)
    }

    fun cancel() {
        try {
            timerTask?.cancel()
        } catch (e: Exception) {

        }
    }
}