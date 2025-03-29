package com.ivax.descarregarvideos.classes

import java.util.TimerTask

class CustomTimerTask(private val callback: ()->Unit) : TimerTask() {
    override fun run() {
        callback()
    }
}