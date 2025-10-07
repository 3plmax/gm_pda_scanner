package com.shipitdone.scanner.manager

import android.view.KeyEvent
import android.content.Context
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener

interface ScannerManager {
    fun init(context: Context)

    fun recycle(context: Context)

    fun setScannerListener(listener: ScanListener)

    fun sendKeyEvent(key: KeyEvent?)

    fun scannerEnable(context: Context, enable: Boolean)

    fun setScanMode(mode: String?)

    fun setDataTransferType(type: String?)

    fun singleScan(context: Context, bool: Boolean)

    fun continuousScan(context: Context, bool: Boolean)

    fun getScannerModel(): Int
}
