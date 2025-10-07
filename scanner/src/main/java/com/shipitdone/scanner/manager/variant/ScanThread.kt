package com.shipitdone.scanner.manager.variant

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.pda.serialport.SerialPort
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ScanThread(private val handler: Handler) : Thread() {
    private val mSerialPort: SerialPort?
    private val `is`: InputStream?
    private val os: OutputStream?

    /* serial port parameter */
    private val port = 0
    private val baudRate = 9600
    private val flags = 0

    /**
     * if throw exception, serial port initialize fail.
     *
     * @throws SecurityException
     * @throws IOException
     */
    init {
        mSerialPort = SerialPort(port, baudRate, flags)
        mSerialPort.scaner_poweron()
        `is` = mSerialPort.inputStream
        os = mSerialPort.outputStream
        try {
            sleep(500)
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        /** clear useless data  */
        val temp = ByteArray(128)
        `is`.read(temp)
    }

    override fun run() {
        try {
            var size = 0
            val buffer = ByteArray(2048)
            var available = 0
            while (!isInterrupted) {
                available = `is`!!.available()
                if (available > 0) {
                    size = `is`.read(buffer)
                    if (size > 0) {
                        sendMessage(buffer, size, SCAN)
                    }
                }
            }
        } catch (e: IOException) {
            // 返回错误信息
            e.printStackTrace()
        }
        super.run()
    }

    private fun sendMessage(data: ByteArray?, dataLen: Int, mode: Int) {
        try {
            val dataStr = String(data!!, 0, dataLen)
            val bundle = Bundle()
            bundle.putString("data", dataStr)
            val msg = Message()
            msg.what = mode
            msg.data = bundle
            handler.sendMessage(msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scan() {
        if (mSerialPort!!.scaner_trig_stat() == true) {
            mSerialPort.scaner_trigoff()
            try {
                sleep(50)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        mSerialPort.scaner_trigon()
    }

    fun close() {
        if (mSerialPort != null) {
            mSerialPort.scaner_poweroff()
            try {
                `is`?.close()
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mSerialPort.close(port)
        }
    }

    companion object {
        var SCAN: Int = 1001 // message receive mode
    }
}
