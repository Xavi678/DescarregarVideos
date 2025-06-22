package com.ivax.descarregarvideos.classes

class MathExtensions {
    companion object {
        fun toTime(seconds: Float): String {
            return toTime(seconds.toLong())
        }

        fun toTime(seconds: Long): String {
            if (seconds < 60) {

                return "0:${padSeconds(seconds)}"
            } else {
                var f = seconds / 60F
                var fInt=f.toInt()
                var x = f - fInt
                if (x == 0F) {
                    return "${fInt}:00"
                } else {
                    var r=fInt*60
                    var dif=seconds-(fInt*60)
                    return "${fInt}:${padSeconds(dif)}"
                }

            }
        }
        private fun padSeconds(seconds: Long):String{
            return seconds.toString().padStart(2,'0')
        }
    }
}