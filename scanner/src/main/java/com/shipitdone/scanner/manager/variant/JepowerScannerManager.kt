package com.shipitdone.scanner.manager.variant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;
import com.shipitdone.scanner.util.BroadcastUtil;

public class JepowerScannerManager implements ScannerManager {
    private static JepowerScannerManager instance;
    private Context activity;
    public static final String ACTION_DATA_CODE_RECEIVED = "com.android.server.scannerservice.broadcast";
    private static final String DATA = "scannerdata";
    private ScannerVariantManager.ScanListener listener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            if (code != null && !code.isEmpty()) {
                listener.onScannerResultChange(code);
            }
        }
    };

    private JepowerScannerManager(Context activity) {
        this.activity = activity;
    }

    public static JepowerScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (JepowerScannerManager.class) {
                if (instance == null) {
                    instance = new JepowerScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        registerReceiver();
        listener.onScannerServiceConnected();
    }

    @Override
    public void recycle() {

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

    }

    @Override
    public void continuousScan(boolean bool) {

    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        BroadcastUtil.registerReceiver(activity, receiver, intentFilter);
    }
}
