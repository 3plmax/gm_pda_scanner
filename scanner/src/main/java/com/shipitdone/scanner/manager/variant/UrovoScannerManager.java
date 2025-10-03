package com.shipitdone.scanner.manager.variant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;

public class UrovoScannerManager implements ScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context activity;
    private static UrovoScannerManager instance;
    public static final String ACTION_DATA_CODE_RECEIVED = "android.intent.ACTION_DECODE_DATA";
    private ScannerVariantManager.ScanListener listener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String code = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG);
                    if (code != null && !code.isEmpty()) {
                        listener.onScannerResultChange(code);
                    }
                }
            });
        }
    };
    private ScanManager mScanManager;

    private UrovoScannerManager(Context activity) {
        this.activity = activity;
    }

    public static UrovoScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (UrovoScannerManager.class) {
                if (instance == null) {
                    instance = new UrovoScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScanManager = new ScanManager();
        boolean b = mScanManager.openScanner();
        if (b) {
            listener.onScannerServiceConnected();
        } else {
            listener.onScannerInitFail();
        }
        registerReceiver();
    }

    @Override
    public void recycle() {
        mScanManager.closeScanner();
        activity.unregisterReceiver(receiver);
        this.listener = null;
    }

    @Override
    public void setScannerListener(@NonNull ScannerVariantManager.ScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {

    }

    @Override
    public int getScannerModel() {
        return 0;
    }

    @Override
    public void scannerEnable(boolean enable) {

    }

    @Override
    public void setScanMode(String mode) {

    }

    @Override
    public void setDataTransferType(String type) {

    }

    @Override
    public void singleScan(boolean bool) {
        if (bool) {
            mScanManager.startDecode();
        } else {
            mScanManager.stopDecode();
        }
    }

    @Override
    public void continuousScan(boolean bool) {

    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        activity.registerReceiver(receiver, intentFilter);
    }
}
