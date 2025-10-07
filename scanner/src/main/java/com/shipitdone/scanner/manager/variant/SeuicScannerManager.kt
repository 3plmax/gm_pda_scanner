package com.shipitdone.scanner.manager.variant

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.seuic.scanner.DecodeInfo
import com.seuic.scanner.DecodeInfoCallBack
import com.seuic.scanner.Scanner
import com.seuic.scanner.ScannerFactory
import com.seuic.scanner.ScannerKey
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener

class SeuicScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var mScanner: Scanner? = null
    private var listener: ScanListener? = null

    private val mDecodeInfoCallBack: DecodeInfoCallBack = object : DecodeInfoCallBack {
        override fun onDecodeComplete(decodeInfo: DecodeInfo) {
            handler.post(object : Runnable {
                override fun run() {
                    val s = decodeInfo.barcode
                    if (s != null) {
                        listener!!.onScannerResultChange(s)
                    }
                }
            })
        }
    }

    override fun init(context: Context) {
        mScanner = ScannerFactory.getScanner(context)
        val isOpen = mScanner!!.open()
        if (isOpen) {
            listener!!.onScannerServiceConnected()
        } else {
            listener!!.onScannerInitFail()
        }
        mScanner!!.setDecodeInfoCallBack(mDecodeInfoCallBack)
        Thread(object : Runnable {
            override fun run() {
                val ret1 = ScannerKey.open()
                if (ret1 > -1) {
                    while (true) {
                        val ret = ScannerKey.getKeyEvent()
                        if (ret > -1) {
                            when (ret) {
                                ScannerKey.KEY_DOWN -> mScanner!!.startScan()
                                ScannerKey.KEY_UP -> mScanner!!.stopScan()
                            }
                        }
                    }
                }
            }
        }).start()
    }

    override fun recycle(context: Context) {
        mScanner!!.close()
        ScannerKey.close()
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
            mScanner!!.enable()
        } else {
            mScanner!!.disable()
        }
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
        if (bool) {
            mScanner!!.startScan()
        } else {
            mScanner!!.stopScan()
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    companion object {
        private var instance: SeuicScannerManager? = null
        fun getInstance(): SeuicScannerManager {
            if (instance == null) {
                synchronized(SeuicScannerManager::class.java) {
                    if (instance == null) {
                        instance = SeuicScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
