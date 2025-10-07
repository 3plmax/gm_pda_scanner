package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class JepowerScannerManager : ScannerManager {
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

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
    }

    override fun recycle(context: Context) {
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
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    companion object {
        private var instance: JepowerScannerManager? = null

        val ACTION_DATA_RECEIVED: String? = ScanManager.ACTION_DECODE
        val RESULT_PARAMETER: String? = ScanManager.BARCODE_STRING_TAG

        fun getInstance(): JepowerScannerManager {
            if (instance == null) {
                synchronized(JepowerScannerManager::class.java) {
                    if (instance == null) {
                        instance = JepowerScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
