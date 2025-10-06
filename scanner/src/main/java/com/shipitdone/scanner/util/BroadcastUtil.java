package com.shipitdone.scanner.util;

import static android.content.Context.RECEIVER_EXPORTED;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

public class BroadcastUtil {
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public static void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter intentFilter) {
        if (context == null || receiver == null || intentFilter == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED);
        } else {
            context.registerReceiver(receiver, intentFilter);
        }
    }
}
