package com.shipitdone.scanner_demo

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shipitdone.scanner.manager.ScannerManager
import com.shipitdone.scanner.manager.ScannerVariantManager
import com.shipitdone.scanner.manager.ScannerVariantManager.ScanListener

class MainActivity : AppCompatActivity() {
    private var mScannerManager: ScannerVariantManager<*>? = null
    private var mTextView: TextView? = null
    private var start: View? = null
    private var stop: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextView = findViewById<TextView>(R.id.result)
        start = findViewById<View>(R.id.start)
        stop = findViewById<View>(R.id.stop)
        mTextView!!.text = "制造商：${Build.MANUFACTURER}\n型号：${Build.MODEL}"
        val clearText = findViewById<Button>(R.id.clear)
        clearText.setOnClickListener(View.OnClickListener { view: View? -> mTextView!!.text = "扫描结果" })
        initScanner()
        initListener()
    }

    private fun initListener() {
        start!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mScannerManager!!.singleScan(this@MainActivity, true)
            }
        })
        stop!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mScannerManager!!.singleScan(this@MainActivity, false)
            }
        })
    }

    private fun initScanner() {
        mScannerManager = ScannerVariantManager<ScannerManager>(this, object : ScanListener {
            override fun onScannerResultChange(result: String?) {
                val s = mTextView!!.text.toString() + "\n" + "扫描结果:"
                mTextView!!.text = "$s$result"
            }

            override fun onScannerServiceConnected() {
                Toast.makeText(this@MainActivity, "扫描头初始化成功", Toast.LENGTH_SHORT).show()
                val s = mTextView!!.text.toString() + "\n"
                mTextView!!.text = "${s}扫描头初始化成功"
            }

            override fun onScannerServiceDisconnected() {
            }

            override fun onScannerInitFail() {
                Toast.makeText(this@MainActivity, "无法获取扫描头，请重试！", Toast.LENGTH_SHORT)
                    .show()
                val s = mTextView!!.text.toString() + "\n"
                mTextView!!.text = "${s}无法获取扫描头"
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mScannerManager != null) {
            mScannerManager!!.recycle(this)
        }
    }
}
