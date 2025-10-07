package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil

class ZebraScannerManager : ScannerManager {


    private var listener: ScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val code = intent.getStringExtra(RESULT_PARAMETER)
            if (code != null && !code.isEmpty()) {
                listener!!.onScannerResultChange(code)
            }
        }
    }

    override fun init(context: Context) {
        registerReceiver(context)
        listener!!.onScannerServiceConnected()
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
        if (enable) {
            val i = Intent()
            i.action = "com.symbol.datawedge.api.ACTION"
            i.putExtra("com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN", "RESUME_PLUGIN")
            context.sendBroadcast(i)
        } else {
            val i = Intent()
            i.action = "com.symbol.datawedge.api.ACTION"
            i.putExtra("com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN", "SUSPEND_PLUGIN")
            context.sendBroadcast(i)
        }
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        BroadcastUtil.registerReceiver(context, receiver, intentFilter)
    }

    companion object {
        private var instance: ZebraScannerManager? = null

        const val ACTION_DATA_RECEIVED = "com.shipitdone.pickpack.ACTION_BARCODE_SCANNED"
        const val RESULT_PARAMETER = "com.symbol.datawedge.data_string"

        fun getInstance(): ZebraScannerManager {
            if (instance == null) {
                synchronized(ZebraScannerManager::class.java) {
                    if (instance == null) {
                        instance = ZebraScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}