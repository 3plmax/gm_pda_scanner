package com.shipitdone.scanner.manager.variant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.util.LogUtil;
import com.shipitdone.scanner.manager.ScannerVariantManager;

public class Kp18ScannerManager implements ScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private static Kp18ScannerManager instance;
    private ScannerVariantManager.ScanListener listener;

    private static String ACTION_DATA_CODE_RECEIVED = "com.kte.scan.result";
    public static String SCAN_RESULT = "code";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String result = intent.getStringExtra(SCAN_RESULT);
                    LogUtil.printLog("[RECV] data=" + result);
                    if (!TextUtils.isEmpty(result)) {
                        listener.onScannerResultChange(result);
                    }
                }
            });
        }
    };

    private Kp18ScannerManager(Context activity) {
        this.mContext = activity;
    }

    public static Kp18ScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (Kp18ScannerManager.class) {
                if (instance == null) {
                    instance = new Kp18ScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        mContext.registerReceiver(receiver, intentFilter);

        listener.onScannerServiceConnected();
    }

    @Override
    public void recycle() {
        mContext.unregisterReceiver(receiver);
    }

    @Override
    public void setScannerListener(@NonNull ScannerVariantManager.ScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {
        LogUtil.printLog("[sendKeyEvent] value=" + key);
    }

    @Override
    public int getScannerModel() {
        LogUtil.printLog("[getScannerModel] value=" + 0);
        return 0;
    }

    @Override
    public void scannerEnable(boolean enable) {
        LogUtil.printLog("[scannerEnable] value=" + enable);
    }

    @Override
    public void setScanMode(String mode) {
        LogUtil.printLog("[setScanMode] value=" + mode);
    }

    @Override
    public void setDataTransferType(String type) {
        LogUtil.printLog("[setDataTransferType] value=" + type);
    }

    @Override
    public void singleScan(boolean bool) {
        LogUtil.printLog("[singleScan] value=" + bool);
    }

    @Override
    public void continuousScan(boolean bool) {
        LogUtil.printLog("[continuousScan] value=" + bool);
    }
}
