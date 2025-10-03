package com.shipitdone.scanner.manager.variant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import java.io.IOException;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;

public class SiganScannerManager implements ScannerManager {
    private Context activity;
    private static SiganScannerManager instance;
    private ScannerVariantManager.ScanListener listener;
    private ScanThread scanThread;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == ScanThread.SCAN) {
                String data = msg.getData().getString("data");
                if (data != null && !data.isEmpty()) {
                    listener.onScannerResultChange(data);
                }
            }
        }
    };

    private SiganScannerManager(Context activity) {
        this.activity = activity;
    }

    public static SiganScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SiganScannerManager.class) {
                if (instance == null) {
                    instance = new SiganScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        try {
            scanThread = new ScanThread(mHandler);
            listener.onScannerServiceConnected();
        } catch (IOException e) {
            listener.onScannerInitFail();
            return;
        }
        scanThread.start();
    }

    @Override
    public void recycle() {
        scanThread.close();
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
        scanThread.scan();
    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
