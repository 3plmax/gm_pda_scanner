package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class PdaScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val broadCode = intent.getByteArrayExtra(RESULT_PARAMETER)
                    val code = String(broadCode!!)
                    if (!code.isEmpty()) {
                        listener!!.onScannerResultChange(code)
                    }
                }
            })
        }
    }
    private var mScanDevice: ScanDevice? = null

    override fun init(context: Context) {
        mScanDevice = ScanDevice()
        mScanDevice!!.openScan()
        mScanDevice!!.outScanMode = 0
        registerReceiver(context)
        listener!!.onScannerServiceConnected()
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
        mScanDevice!!.startScan()
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
    }

    companion object {
        private var instance: PdaScannerManager? = null

        const val ACTION_DATA_RECEIVED = "scan.rcv.message"
        const val RESULT_PARAMETER = "barcode"

        fun getInstance(): PdaScannerManager {
            if (instance == null) {
                synchronized(UrovoScannerManager::class.java) {
                    if (instance == null) {
                        instance = PdaScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
