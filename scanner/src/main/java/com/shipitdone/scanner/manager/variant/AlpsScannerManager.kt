package com.shipitdone.scanner.manager.variant

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.zltd.industry.ScannerManager
import com.zltd.industry.ScannerManager.IScannerStatusListener
import java.io.UnsupportedEncodingException

class AlpsScannerManager: com.shipitdone.scanner.manager.ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var mScannerManager: ScannerManager? = null
    private var listener: ScanListener? = null

    private val mIScannerStatusListener: IScannerStatusListener = object : IScannerStatusListener {
        override fun onScannerStatusChanage(i: Int) {
        }

        override fun onScannerResultChanage(bytes: ByteArray?) {
            handler.post(object : Runnable {
                override fun run() {
                    var s: String? = null
                    try {
                        s = String(bytes!!, charset("UTF-8"))
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    if (s != null && listener != null) {
                        listener!!.onScannerResultChange(s)
                    }
                }
            })
        }
    }

    override fun init(context: Context) {
        mScannerManager = ScannerManager.getInstance()
        mScannerManager!!.scannerEnable(true)
        mScannerManager!!.scanMode = ScannerManager.SCAN_SINGLE_MODE
        mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_API
        mScannerManager!!.addScannerStatusListener(mIScannerStatusListener)
    }

    override fun recycle(context: Context) {
        if (mScannerManager != null) {
            mScannerManager!!.removeScannerStatusListener(mIScannerStatusListener)
        }
    }

    override fun setScannerListener(listener: ScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {
    }

    override fun getScannerModel(): Int {
        return -1
    }

    override fun scannerEnable(context: Context, enable: Boolean) {
        mScannerManager!!.scannerEnable(enable)
    }

    override fun setScanMode(mode: String?) {
        when (mode) {
            "single" -> mScannerManager!!.scanMode = ScannerManager.SCAN_SINGLE_MODE
            "continuous" -> mScannerManager!!.scanMode = ScannerManager.SCAN_CONTINUOUS_MODE
            "keyHold" -> mScannerManager!!.scanMode = ScannerManager.SCAN_KEY_HOLD_MODE
            else -> {}
        }
    }

    override fun setDataTransferType(type: String?) {
        when (type) {
            "api" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_API
            "editText" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_EDITTEXT
            "key" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_KEY
            else -> {}
        }
    }

    override fun singleScan(context: Context, bool: Boolean) {
        if (bool) {
            mScannerManager!!.startKeyHoldScan()
        } else {
            mScannerManager!!.stopKeyHoldScan()
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
        if (bool) {
            mScannerManager!!.startContinuousScan()
        } else {
            mScannerManager!!.stopContinuousScan()
        }
    }

    companion object {
        private var instance: AlpsScannerManager? = null
        fun getInstance(): AlpsScannerManager {
            if (instance == null) {
                synchronized(AlpsScannerManager::class.java) {
                    if (instance == null) {
                        instance = AlpsScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
