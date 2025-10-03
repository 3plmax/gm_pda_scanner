package com.shipitdone.scanner.manager.variant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;

public class NewlandScannerManager implements ScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private static NewlandScannerManager instance;
    private ScannerVariantManager.ScanListener listener;
    private static final String ACTION_DATA_CODE_RECEIVED = "nlscan.action.SCANNER_RESULT";
    private static final String SCAN_ACTION = "nlscan.action.SCANNER_TRIG";
    public static final String SETTING_ACTION = "ACTION_BAR_SCANCFG";
    public static final String RESULT1 = "SCAN_BARCODE1";
    public static final String RESULT2 = "SCAN_BARCODE2";
    public static final String POWER_TAG = "EXTRA_SCAN_POWER";
    public static final String SCAN_MODE_TAG = "EXTRA_SCAN_MODE";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String code1 = intent.getStringExtra(RESULT1);
                    String code2 = intent.getStringExtra(RESULT2);
                    if (code1 != null && !code1.isEmpty()) {
                        listener.onScannerResultChange(code1);
                    } else if (code2 != null && !code2.isEmpty()) {
                        listener.onScannerResultChange(code2);
                    }
                }
            });
        }
    };

    private NewlandScannerManager(Context activity) {
        this.mContext = activity;
    }

    public static NewlandScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (NewlandScannerManager.class) {
                if (instance == null) {
                    instance = new NewlandScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        Intent intent = new Intent(SETTING_ACTION);
        intent.putExtra(POWER_TAG, 1);
        intent.putExtra(SCAN_MODE_TAG, 3);
        mContext.sendBroadcast(intent);
        registerReceiver();
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
        Intent intent = new Intent(SCAN_ACTION);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void continuousScan(boolean bool) {

    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        mContext.registerReceiver(receiver, intentFilter);
    }
}
