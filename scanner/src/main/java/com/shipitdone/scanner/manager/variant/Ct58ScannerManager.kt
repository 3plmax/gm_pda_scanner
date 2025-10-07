package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver
import com.shipitdone.scanner.util.LogUtil.printLog

class Ct58ScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: ScanListener? = null

    var mScanManager: ScanManager = ScanManager()

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
        //设置扫描模式
        mScanManager.switchOutputMode(0)
        //设置扫描参数
        val params = mScanManager.getParameterString(
            intArrayOf(
                PropertyID.WEDGE_INTENT_ACTION_NAME,
                PropertyID.WEDGE_INTENT_DATA_STRING_TAG
            )
        )
        if (params != null && params.size == 2) {
            ACTION_DATA_RECEIVED = params[0]
            RESULT_PARAMETER = params[1]
        }

        if (TextUtils.isEmpty(ACTION_DATA_RECEIVED)) {
            ACTION_DATA_RECEIVED = ScanManager.ACTION_DECODE
        }
        if (TextUtils.isEmpty(RESULT_PARAMETER)) {
            RESULT_PARAMETER = ScanManager.BARCODE_STRING_TAG
        }

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
            mScanManager.startDecode()
        } else {
            mScanManager.stopDecode()
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
        printLog("[continuousScan] value=$bool")
    }

    companion object {
        private var instance: Ct58ScannerManager? = null

        var ACTION_DATA_RECEIVED: String? = ScanManager.ACTION_DECODE
        var RESULT_PARAMETER: String? = ScanManager.BARCODE_STRING_TAG

        fun getInstance(): Ct58ScannerManager {
            if (instance == null) {
                synchronized(Ct58ScannerManager::class.java) {
                    if (instance == null) {
                        instance = Ct58ScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
