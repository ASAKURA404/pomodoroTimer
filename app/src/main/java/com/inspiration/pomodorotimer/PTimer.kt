package com.inspiration.pomodorotimer

class PTimer(val posN: Int = 25 * 60, val negN: Int = 5 * 60) {
    var nowValue = posN

    var inWorkProgress = true
    fun minute() : String = (nowValue / 60).toString() + "分" + (nowValue % 60) + "秒"
    fun status() : String = if(inWorkProgress) "仕事中" else "休憩中"

    fun count() {
        nowValue--
    }

    fun reset() {
        inWorkProgress = !inWorkProgress
        nowValue = if (inWorkProgress) posN else negN
    }
}