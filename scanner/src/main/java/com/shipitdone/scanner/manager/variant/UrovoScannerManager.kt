package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class UrovoScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val code = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG)
                    if (code != null && !code.isEmpty()) {
                        listener!!.onScannerResultChange(code)
                    }
                }
            })
        }
    }
    private var mScanManager: ScanManager? = null

    override fun init(context: Context) {
        try {
            mScanManager = ScanManager()
            if (mScanManager!!.openScanner()) {
                if (mScanManager!!.switchOutputMode(0)) {
                    listener!!.onScannerServiceConnected()
                } else {
                    listener!!.onScannerInitFail()
                }
            } else {
                listener!!.onScannerInitFail()
            }
            registerReceiver(context)
        } catch (e: Exception) {
            listener!!.onScannerInitFail()
        }
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
    }

    override fun recycle(context: Context) {
        mScanManager!!.closeScanner()
        context.unregisterReceiver(receiver)
        this.listener = null
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
        if (enable) {
            mScanManager!!.unlockTrigger()
        } else {
            mScanManager!!.lockTrigger()
        }
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
        if (bool) {
            mScanManager!!.startDecode()
        } else {
            mScanManager!!.stopDecode()
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    companion object {
        private var instance: UrovoScannerManager? = null

        const val ACTION_DATA_RECEIVED: String = "android.intent.ACTION_DECODE_DATA"

        fun getInstance(): UrovoScannerManager {
            if (instance == null) {
                synchronized(UrovoScannerManager::class.java) {
                    if (instance == null) {
                        instance = UrovoScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
