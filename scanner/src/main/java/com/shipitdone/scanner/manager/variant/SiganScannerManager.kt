package com.shipitdone.scanner.manager.variant

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import java.io.IOException

class SiganScannerManager : ScannerManager {
    private var listener: ScanListener? = null
    private var scanThread: ScanThread? = null

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == ScanThread.SCAN) {
                val data = msg.data.getString("data")
                if (data != null && !data.isEmpty()) {
                    listener!!.onScannerResultChange(data)
                }
            }
        }
    }

    override fun init(context: Context) {
        try {
            scanThread = ScanThread(mHandler)
            listener!!.onScannerServiceConnected()
        } catch (_: IOException) {
            listener!!.onScannerInitFail()
            return
        }
        scanThread!!.start()
    }

    override fun recycle(context: Context) {
        scanThread!!.close()
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
        scanThread!!.scan()
    }

    override fun continuousScan(context: Context, bool: Boolean) {
    }

    companion object {
        private var instance: SiganScannerManager? = null
        fun getInstance(): SiganScannerManager {
            if (instance == null) {
                synchronized(SiganScannerManager::class.java) {
                    if (instance == null) {
                        instance = SiganScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
