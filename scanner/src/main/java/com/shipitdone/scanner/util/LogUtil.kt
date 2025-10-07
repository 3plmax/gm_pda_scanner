package com.shipitdone.scanner.util

import android.util.Log

object LogUtil {
    var openLog: Boolean = false

    @JvmStatic
    fun printLog(log: String) {
        if (openLog) {
            Log.i("Scanner_TAG", log)
        }
    }
}
