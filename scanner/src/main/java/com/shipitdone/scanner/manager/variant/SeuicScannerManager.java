package com.shipitdone.scanner.manager.variant;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.view.KeyEvent;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;
import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;

public class SeuicScannerManager implements ScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context activity;
    private static SeuicScannerManager instance;
    private Scanner mScanner;
    private ScannerVariantManager.ScanListener listener;

    private DecodeInfoCallBack mDecodeInfoCallBack = new DecodeInfoCallBack() {
        @Override
        public void onDecodeComplete(final DecodeInfo decodeInfo) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String s = decodeInfo.barcode;
                    if (s != null) {
                        listener.onScannerResultChange(s);
                    }
                }
            });
        }
    };

    private SeuicScannerManager(Context activity) {
        this.activity = activity;
    }

    public static SeuicScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SeuicScannerManager.class) {
                if (instance == null) {
                    instance = new SeuicScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScanner = ScannerFactory.getScanner(activity);
        boolean isOpen = mScanner.open();
        if (isOpen) {
            listener.onScannerServiceConnected();
        } else {
            listener.onScannerInitFail();
        }
        mScanner.setDecodeInfoCallBack(mDecodeInfoCallBack);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret1 = ScannerKey.open();
                if (ret1 > -1) {
                    while (true) {
                        int ret = ScannerKey.getKeyEvent();
                        if (ret > -1) {
                            switch (ret) {
                                case ScannerKey.KEY_DOWN:
                                    mScanner.startScan();
                                    break;
                                case ScannerKey.KEY_UP:
                                    mScanner.stopScan();
                                    break;
                            }
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void recycle() {
        mScanner.close();
        ScannerKey.close();
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
        if (enable) {
            mScanner.enable();
        } else {
            mScanner.disable();
        }
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
            mScanner.startScan();
        } else {
            mScanner.stopScan();
        }
    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
