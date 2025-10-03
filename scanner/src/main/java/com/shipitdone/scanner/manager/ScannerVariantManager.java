package com.shipitdone.scanner.manager;

import static com.shipitdone.scanner.manager.ScannerBrand.getCurrent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.shipitdone.scanner.manager.variant.HandHeldScannerManager;
import com.shipitdone.scanner.manager.variant.Ct58ScannerManager;
import com.shipitdone.scanner.manager.variant.JepowerScannerManager;
import com.shipitdone.scanner.manager.variant.Kp18ScannerManager;
import com.shipitdone.scanner.manager.variant.PdaScannerManager;
import com.shipitdone.scanner.manager.variant.Pdt90fScannerManager;
import com.shipitdone.scanner.manager.variant.SiganScannerManager;
import com.shipitdone.scanner.manager.variant.AlpsScannerManager;
import com.shipitdone.scanner.manager.variant.IdataScannerManager;
import com.shipitdone.scanner.manager.variant.NewlandScannerManager;
import com.shipitdone.scanner.manager.variant.OtherScannerManager;
import com.shipitdone.scanner.manager.variant.SeuicScannerManager;
import com.shipitdone.scanner.manager.variant.SunmiScannerManager;
import com.shipitdone.scanner.manager.variant.UrovoScannerManager;

public class ScannerVariantManager<T extends ScannerManager> {
    private static ScannerVariantManager<ScannerManager> instance;
    private T scannerManager;

    public ScannerVariantManager(Context context, @NonNull ScanListener listener) {
        ScannerBrand scannerBrand = getCurrent();
        Log.i("Scanner","[ScannerBrand] ="+scannerBrand);
        switch (scannerBrand) {
            case UROVO:
                scannerManager = (T) UrovoScannerManager.getInstance(context);
                break;
            case SUNMI:
                scannerManager = (T) SunmiScannerManager.getInstance(context);
                break;
            case ALPS:
                scannerManager = (T) AlpsScannerManager.getInstance(context);
                break;
            case SEUIC:
                scannerManager = (T) SeuicScannerManager.getInstance(context);
                break;
            case NEWLAND:
                scannerManager = (T) NewlandScannerManager.getInstance(context);
                break;
            case IDATA:
                scannerManager = (T) IdataScannerManager.getInstance(context);
                break;
            case SIGAN:
                scannerManager = (T) SiganScannerManager.getInstance(context);
                break;
            case JEPOWER:
                scannerManager = (T) JepowerScannerManager.getInstance(context);
                break;
            case HAND_HELD:
                scannerManager = (T) HandHeldScannerManager.getInstance(context);
                break;
            case GENERIC_PDA:
                scannerManager = (T) PdaScannerManager.getInstance(context);
                break;
            case GENERIC_PDT90F:
                scannerManager = (T) Pdt90fScannerManager.getInstance(context);
                break;
            case GENERIC_CT58:
                scannerManager = (T) Ct58ScannerManager.getInstance(context);
                break;
            case GENERIC_KP18:
                scannerManager = (T) Kp18ScannerManager.getInstance(context);
                break;
            default:
                scannerManager = (T) new OtherScannerManager(context);
                break;
        }

        Log.i("Scanner","[scannerManager] ="+scannerManager);
        scannerManager.setScannerListener(listener);
        scannerManager.init();
    }

    public void recycle() {
        scannerManager.recycle();
    }

    public void setScannerListener(@NonNull ScanListener listener) {
        scannerManager.setScannerListener(listener);
    }

    public void sendKeyEvent(KeyEvent key) {
        scannerManager.sendKeyEvent(key);
    }

    public int getScannerModel() {
        return scannerManager.getScannerModel();
    }

    public void scannerEnable(Boolean enable) {
        scannerManager.scannerEnable(enable);
    }

    public void setScanMode(String mode) {
        scannerManager.setScanMode(mode);
    }

    public void setDataTransferType(String type) {
        scannerManager.setDataTransferType(type);
    }

    public void singleScan(Boolean bool) {
        scannerManager.singleScan(bool);
    }

    public void continuousScan(Boolean bool) {
        scannerManager.continuousScan(bool);
    }

    public interface ScanListener {
        void onScannerResultChange(String result);

        void onScannerServiceConnected();

        void onScannerServiceDisconnected();

        void onScannerInitFail();
    }
}
