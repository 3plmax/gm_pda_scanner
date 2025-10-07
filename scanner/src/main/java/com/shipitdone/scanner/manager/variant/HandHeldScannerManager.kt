package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver
import com.shipitdone.scanner.util.LogUtil.printLog

class HandHeldScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val result = intent.getStringExtra(RESULT_PARAMETER)
                    printLog("[RECV] data=$result")
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
        listener!!.onScannerServiceConnected()
    }

    override fun recycle(context: Context) {
        context.unregisterReceiver(receiver)
    }

    override fun setScannerListener(listener: ScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {
        printLog("[sendKeyEvent] value=$key")
    }

    override fun getScannerModel(): Int {
        printLog("[getScannerModel] value=" + 0)
        return 0
    }

    override fun scannerEnable(context: Context, enable: Boolean) {
        printLog("[scannerEnable] value=$enable")
    }

    override fun setScanMode(mode: String?) {
        printLog("[setScanMode] value=$mode")
    }

    override fun setDataTransferType(type: String?) {
        printLog("[setDataTransferType] value=$type")
    }

    override fun singleScan(context: Context, bool: Boolean) {
        printLog("[singleScan] value=$bool")
        if (bool) {
            context.sendBroadcast(Intent("com.uc.scanner.trigger.START"))
        } else {
            context.sendBroadcast(Intent("com.uc.scanner.trigger.STOP"))
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
        printLog("[continuousScan] value=$bool")
    }

    companion object {
        private var instance: HandHeldScannerManager? = null

        const val ACTION_DATA_RECEIVED = "com.uc.scanner.result"
        const val RESULT_PARAMETER: String = "string"

        fun getInstance(): HandHeldScannerManager {
            if (instance == null) {
                synchronized(HandHeldScannerManager::class.java) {
                    if (instance == null) {
                        instance = HandHeldScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
