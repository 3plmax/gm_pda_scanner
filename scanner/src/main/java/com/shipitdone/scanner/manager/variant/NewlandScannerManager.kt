package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class NewlandScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val code1 = intent.getStringExtra(RESULT_PARAMETER_1)
                    val code2 = intent.getStringExtra(RESULT_PARAMETER_2)
                    if (code1 != null && !code1.isEmpty()) {
                        listener!!.onScannerResultChange(code1)
                    } else if (code2 != null && !code2.isEmpty()) {
                        listener!!.onScannerResultChange(code2)
                    }
                }
            })
        }
    }

    override fun init(context: Context) {
        val intent = Intent(SETTING_ACTION)
        intent.putExtra(POWER_TAG, 1)
        intent.putExtra(SCAN_MODE_TAG, 3)
        context.sendBroadcast(intent)
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
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
        val intent = Intent(SCAN_ACTION)
        context.sendBroadcast(intent)
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
    }

    companion object {
        private var instance: NewlandScannerManager? = null

        const val ACTION_DATA_RECEIVED = "nlscan.action.SCANNER_RESULT"
        const val SCAN_ACTION = "nlscan.action.SCANNER_TRIG"
        const val SETTING_ACTION: String = "ACTION_BAR_SCANCFG"
        const val RESULT_PARAMETER_1: String = "SCAN_BARCODE1"
        const val RESULT_PARAMETER_2: String = "SCAN_BARCODE2"
        const val POWER_TAG: String = "EXTRA_SCAN_POWER"
        const val SCAN_MODE_TAG: String = "EXTRA_SCAN_MODE"

        fun getInstance(): NewlandScannerManager {
            if (instance == null) {
                synchronized(NewlandScannerManager::class.java) {
                    if (instance == null) {
                        instance = NewlandScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
