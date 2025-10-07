package com.shipitdone.scanner.manager.variant

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.util.Log
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver
import com.sunmi.scanner.IScanInterface

class SunmiScannerManager : ScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var serviceIntent: Intent? = null
    private var listener: ScanListener? = null
    private var singleScanFlag = false

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            listener?.onScannerServiceConnected()
            scanInterface = IScanInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            if (listener != null) {
                listener!!.onScannerServiceDisconnected()
            }
            scanInterface = null
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post(object : Runnable {
                override fun run() {
                    val code = intent.getStringExtra(RESULT_PARAMETER)
                    if (code != null && !code.isEmpty()) {
                        if (listener != null) {
                            listener!!.onScannerResultChange(code)
                        }
                        if (singleScanFlag) {
                            singleScanFlag = false
                            try {
                                scanInterface!!.stop()
                            } catch (e: RemoteException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
        }
    }

    override fun init(context: Context) {
        bindService(context)
        registerReceiver(context)
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_RECEIVED)
        registerReceiver(context, receiver, intentFilter)
    }

    private fun bindService(context: Context) {
        serviceIntent = Intent()
        serviceIntent!!.setPackage("com.sunmi.scanner")
        serviceIntent!!.action = "com.sunmi.scanner.IScanInterface"
        context.bindService(serviceIntent!!, conn, Service.BIND_AUTO_CREATE)
    }

    override fun recycle(context: Context) {
        context.stopService(serviceIntent)
        context.unregisterReceiver(receiver)
        this.listener = null
    }

    override fun setScannerListener(listener: ScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {
        try {
            scanInterface!!.sendKeyEvent(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取扫描头类型
     *
     * @return int 100 -> NONE 101 -> P2Lite 102 -> L2-newLane 103 -> L2 -zabra
     */
    override fun getScannerModel(): Int {
        try {
            return scanInterface!!.getScannerModel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    override fun scannerEnable(context: Context, enable: Boolean) {
        Log.d("pda", "此设备不支持该方法")
    }

    override fun setScanMode(mode: String?) {
    }

    override fun setDataTransferType(type: String?) {
    }

    override fun singleScan(context: Context, bool: Boolean) {
        try {
            if (bool) {
                scanInterface!!.scan()
                singleScanFlag = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    override fun continuousScan(context: Context, bool: Boolean) {
        try {
            if (bool) {
                scanInterface!!.scan()
            } else {
                scanInterface!!.stop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var scanInterface: IScanInterface? = null
        private var instance: SunmiScannerManager? = null

        const val ACTION_DATA_RECEIVED: String = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED"
        const val RESULT_PARAMETER = "data"

        fun getInstance(): SunmiScannerManager {
            if (instance == null) {
                synchronized(SunmiScannerManager::class.java) {
                    if (instance == null) {
                        instance = SunmiScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
