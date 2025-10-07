package com.shipitdone.scanner.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build

object BroadcastUtil {
    @JvmStatic
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun registerReceiver(
        context: Context?,
        receiver: BroadcastReceiver?,
        intentFilter: IntentFilter?
    ) {
        if (context == null || receiver == null || intentFilter == null) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, intentFilter)
        }
    }
}
