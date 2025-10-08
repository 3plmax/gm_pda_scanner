package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.Toast
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class OtherScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val result = intent.getStringExtra(RESULT_PARAMETER)
                    if (!TextUtils.isEmpty(result)) {
                        listener!!.onScannerResultChange(result)
                    }
                }
            })
        }
    }

    override fun init(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        listener!!.onScannerServiceConnected()
                    }
                }, 800)
            }
        })
    }

    override fun recycle(context: Context) {
        context.unregisterReceiver(receiver)
    }

    override fun setScannerListener(listener: ScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {
    }

    override fun getScannerModel(): Int {
        return 0
    }

    override fun scannerEnable(context: Context, enable: Boolean) {
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
        if (bool) {
            context.sendBroadcast(Intent(ACTION_START))
        } else {
            context.sendBroadcast(Intent(ACTION_STOP))
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    companion object {
        const val ACTION_START = "com.shipitdone.scanner.START"
        const val ACTION_STOP = "com.shipitdone.scanner.STOP"
        const val ACTION_DATA_RECEIVED = "com.shipitdone.scanner.data"
        const val RESULT_PARAMETER: String = "string"
    }
}
