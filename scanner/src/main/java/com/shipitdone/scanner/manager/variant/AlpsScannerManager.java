package com.shipitdone.scanner.manager.variant;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.ScannerManager;
import com.shipitdone.scanner.manager.ScannerVariantManager;

import java.io.UnsupportedEncodingException;

public class AlpsScannerManager implements ScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context activity;
    private static AlpsScannerManager instance;
    private com.zltd.industry.ScannerManager mScannerManager;
    private ScannerVariantManager.ScanListener listener;

    private com.zltd.industry.ScannerManager.IScannerStatusListener mIScannerStatusListener = new com.zltd.industry.ScannerManager.IScannerStatusListener() {
        @Override
        public void onScannerStatusChanage(int i) {

        }

        @Override
        public void onScannerResultChanage(final byte[] bytes) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String s = null;
                    try {
                        s = new String(bytes, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (s != null && listener != null) {
                        listener.onScannerResultChange(s);
                    }
                }
            });
        }
    };

    private AlpsScannerManager(Context activity) {
        this.activity = activity;
    }

    public static AlpsScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (AlpsScannerManager.class) {
                if (instance == null) {
                    instance = new AlpsScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScannerManager = com.zltd.industry.ScannerManager.getInstance();
        mScannerManager.scannerEnable(true);
        mScannerManager.setScanMode(com.zltd.industry.ScannerManager.SCAN_SINGLE_MODE);
        mScannerManager.setDataTransferType(com.zltd.industry.ScannerManager.TRANSFER_BY_API);
        mScannerManager.addScannerStatusListener(mIScannerStatusListener);
    }

    @Override
    public void recycle() {
        if (mScannerManager != null) {
            mScannerManager.removeScannerStatusListener(mIScannerStatusListener);
        }
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
        return -1;
    }

    @Override
    public void scannerEnable(boolean enable) {
        mScannerManager.scannerEnable(enable);
    }

    @Override
    public void setScanMode(String mode) {
        switch (mode) {
            case "single":
                mScannerManager.setScanMode(com.zltd.industry.ScannerManager.SCAN_SINGLE_MODE);
                break;
            case "continuous":
                mScannerManager.setScanMode(com.zltd.industry.ScannerManager.SCAN_CONTINUOUS_MODE);
                break;
            case "keyHold":
                mScannerManager.setScanMode(com.zltd.industry.ScannerManager.SCAN_KEY_HOLD_MODE);
                break;
            default:
                break;
        }

    }

    @Override
    public void setDataTransferType(String type) {
        switch (type) {
            case "api":
                mScannerManager.setDataTransferType(com.zltd.industry.ScannerManager.TRANSFER_BY_API);
                break;
            case "editText":
                mScannerManager.setDataTransferType(com.zltd.industry.ScannerManager.TRANSFER_BY_EDITTEXT);
                break;
            case "key":
                mScannerManager.setDataTransferType(com.zltd.industry.ScannerManager.TRANSFER_BY_KEY);
                break;
            default:
                break;
        }

    }

    @Override
    public void singleScan(boolean bool) {
        if (bool) {
            mScannerManager.startKeyHoldScan();
        } else {
            mScannerManager.stopKeyHoldScan();
        }
    }

    @Override
    public void continuousScan(boolean bool) {
        if (bool) {
            mScannerManager.startContinuousScan();
        } else {
            mScannerManager.stopContinuousScan();
        }
    }
}
