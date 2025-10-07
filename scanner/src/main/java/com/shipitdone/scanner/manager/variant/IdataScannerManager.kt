package com.shipitdone.scanner.manager.variant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.KeyEvent
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener
import com.shipitdone.scanner.util.BroadcastUtil.registerReceiver

class IdataScannerManager : ScannerManager {
    private var mScanner: IdataScannerInterface? = null
    private var listener: ScanListener? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == RES_ACTION) {
                // int barocodelen = intent.getIntExtra("length", 0);
                // int type = intent.getIntExtra("type", 0);
                // String myType = String.format("%c", type);
                // 获取扫描结果
                val scanResult = intent.getStringExtra("value")
                Log.e("TAG", scanResult!!)
                if (!scanResult.isEmpty()) {
                    listener!!.onScannerResultChange(scanResult)
                }
            }
        }
    }

    override fun init(context: Context) {
        mScanner = IdataScannerInterface(context)
        mScanner!!.open() //打开扫描头上电   mScanner.close();//打开扫描头下电
        mScanner!!.enablePlayBeep(true) //是否允许蜂鸣反馈
        mScanner!!.enableFailurePlayBeep(false) //扫描失败蜂鸣反馈
        mScanner!!.enablePlayVibrate(true) //是否允许震动反馈
        // mScanner.enableAddKeyValue(1);/**附加无、回车、Teble、换行*/
        mScanner!!.timeOutSet(10) //设置扫描延时10秒
        mScanner!!.intervalSet(1000) //设置连续扫描间隔时间
        // mScanner.lightSet(false);//关闭右上角扫描指示灯
        mScanner!!.enablePower(true) //省电模式
        mScanner!!.setMaxMultireadCount(5) //设置一次最多解码5个

        // mScanner.addPrefix("AAA");//添加前缀
        // mScanner.addSuffix("BBB");//添加后缀
        // mScanner.interceptTrimleft(2); //截取条码左边字符
        // mScanner.interceptTrimright(3);//截取条码右边字符
        // mScanner.filterCharacter("R");//过滤特定字符
        mScanner!!.SetErrorBroadCast(true) //扫描错误换行

        // mScanner.resultScan();//恢复iScan默认设置

        // mScanner.lockScanKey();
        // 锁定设备的扫描按键,通过iScan定义扫描键扫描，用户也可以自定义按键。
        // mScanner.unlockScanKey();
        // 释放扫描按键的锁定，释放后iScan无法控制扫描按键，用户可自定义按键扫描。
        /**设置扫描结果的输出模式，参数为0和1：
         * 0为模拟输出（在光标停留的地方输出扫描结果）；
         * 1为广播输出（由应用程序编写广播接收者来获得扫描结果，并在指定的控件上显示扫描结果）
         * 这里采用接收扫描结果广播并在TextView中显示 */
        mScanner!!.setOutputMode(1)
        mScanner!!.setCharSetMode(4)
        listener!!.onScannerServiceConnected()
        registerReceiver(context)
    }

    private fun registerReceiver(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(RES_ACTION)
        registerReceiver(context, receiver, intentFilter)
    }

    override fun recycle(context: Context) {
        mScanner!!.resultScan()
        // mScanner.close();
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
        if (bool) {
            mScanner!!.scan_start()
        } else {
            mScanner!!.scan_stop()
        }
    }

    override fun continuousScan(context: Context, bool: Boolean) {
        mScanner!!.continceScan(bool)
    }

    companion object {
        private var instance: IdataScannerManager? = null
        private const val RES_ACTION = "android.intent.action.SCANRESULT"

        fun getInstance(): IdataScannerManager {
            if (instance == null) {
                synchronized(IdataScannerManager::class.java) {
                    if (instance == null) {
                        instance = IdataScannerManager()
                    }
                }
            }
            return instance!!
        }
    }
}
