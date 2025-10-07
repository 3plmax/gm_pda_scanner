package com.shipitdone.scanner.manager

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import com.shipitdone.scanner.manager.variant.AlpsScannerManager
import com.shipitdone.scanner.manager.variant.Ct58ScannerManager
import com.shipitdone.scanner.manager.variant.HandHeldScannerManager
import com.shipitdone.scanner.manager.variant.IdataScannerManager
import com.shipitdone.scanner.manager.variant.JepowerScannerManager
import com.shipitdone.scanner.manager.variant.Kp18ScannerManager
import com.shipitdone.scanner.manager.variant.NewlandScannerManager
import com.shipitdone.scanner.manager.variant.OtherScannerManager
import com.shipitdone.scanner.manager.variant.PdaScannerManager
import com.shipitdone.scanner.manager.variant.Pdt90fScannerManager
import com.shipitdone.scanner.manager.variant.SeuicScannerManager
import com.shipitdone.scanner.manager.variant.SiganScannerManager
import com.shipitdone.scanner.manager.variant.SunmiScannerManager
import com.shipitdone.scanner.manager.variant.UrovoScannerManager
import com.shipitdone.scanner.manager.variant.ZebraScannerManager.Companion.getInstance

class ScannerVariantManager<T : ScannerManager?>(context: Context, listener: ScanListener) {
    private var scannerManager: T? = null

    init {
        val scannerBrand = ScannerBrand.current
        Log.i("Scanner", "[ScannerBrand] =$scannerBrand")
        when (scannerBrand) {
            ScannerBrand.UROVO -> scannerManager = UrovoScannerManager.getInstance() as T?
            ScannerBrand.SUNMI -> scannerManager = SunmiScannerManager.getInstance() as T?
            ScannerBrand.ALPS -> scannerManager = AlpsScannerManager.getInstance() as T?
            ScannerBrand.SEUIC -> scannerManager = SeuicScannerManager.getInstance() as T?
            ScannerBrand.NEWLAND -> scannerManager = NewlandScannerManager.getInstance() as T?
            ScannerBrand.IDATA -> scannerManager = IdataScannerManager.getInstance() as T?
            ScannerBrand.SIGAN -> scannerManager = SiganScannerManager.getInstance() as T?
            ScannerBrand.JEPOWER -> scannerManager = JepowerScannerManager.getInstance() as T?
            ScannerBrand.HAND_HELD -> scannerManager = HandHeldScannerManager.getInstance() as T?
            ScannerBrand.ZEBRA -> scannerManager = getInstance() as T

            ScannerBrand.GENERIC_PDA -> scannerManager = PdaScannerManager.getInstance() as T?
            ScannerBrand.GENERIC_PDT90F -> scannerManager = Pdt90fScannerManager.getInstance() as T?
            ScannerBrand.GENERIC_CT58 -> scannerManager = Ct58ScannerManager.getInstance() as T?
            ScannerBrand.GENERIC_KP18 -> scannerManager = Kp18ScannerManager.getInstance() as T?

            else -> scannerManager = OtherScannerManager() as T
        }

        Log.i("Scanner", "[scannerManager] =$scannerManager")
        scannerManager!!.setScannerListener(listener)
        scannerManager!!.init(context)
    }

    fun recycle(context: Context) {
        scannerManager!!.recycle(context)
    }

    fun setScannerListener(listener: ScanListener) {
        scannerManager!!.setScannerListener(listener)
    }

    fun sendKeyEvent(key: KeyEvent?) {
        scannerManager!!.sendKeyEvent(key)
    }

    val scannerModel: Int
        get() = scannerManager!!.getScannerModel()

    fun scannerEnable(context: Context, enable: Boolean) {
        scannerManager!!.scannerEnable(context, enable)
    }

    fun setScanMode(mode: String?) {
        scannerManager!!.setScanMode(mode)
    }

    fun setDataTransferType(type: String?) {
        scannerManager!!.setDataTransferType(type)
    }

    fun singleScan(context: Context, bool: Boolean) {
        scannerManager!!.singleScan(context, bool)
    }

    fun continuousScan(context: Context, bool: Boolean) {
        scannerManager!!.continuousScan(context, bool)
    }

    interface ScanListener {
        fun onScannerResultChange(result: String?)

        fun onScannerServiceConnected()

        fun onScannerServiceDisconnected()

        fun onScannerInitFail()
    }
}
