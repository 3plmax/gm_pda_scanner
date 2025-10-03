package com.shipitdone.scanner.manager;

import android.os.Build;
import android.util.Log;

/**
 * SUNMI: 商米科技 L2 手持扫码设备
 * ALPS: 智联天地的扫码 PDA, 型号 N2S000
 * SEUIC: 东大集成
 * UBX: 优博讯
 * 型号SG6900: 深圳市思感科技有限公司
 * 型号HT380K: 深圳市捷宝科技有限公司
 * 型号 NLS-MT6210，制造商 Newland
 * 型号 NLS-MT9210，制造商 Newland
 * 型号 GM-B9920P，制造商Hand-held Terminal
 */
public enum ScannerBrand {
    // Manufacturers with known aliases
    // The first name in the array is considered the primary/canonical name.
    UROVO(new String[]{"UROVO", "UBX"}),
    NEWLAND(new String[]{"NEWLAND", "NLS"}, "NLS-MT90", "NLS-MT66", "NLS-MT6210", "NLS-MT9210"),

    // Manufacturers identified by a single primary name
    IDATA("IDATA", "T1", "MC005"),
    NEWLAND("NEWLAND", "NLS-MT90", "NLS-MT66", "NLS-MT6210", "NLS-MT9210"),
    ALPS("ALPS"),
    SEUIC("SEUIC"),
    SIGAN("SIGAN", "SG6900"),
    SUNMI("SUNMI"),
    JEPOWER("JEPOWER", "HT380K"),
    HAND_HELD("HAND-HELD TERMINAL", "GM-B9920P"),

    // Other generic or uncategorized devices
    GENERIC_PDA("PDA", "PDA"),
    GENERIC_PDT90F("PDT-90F", "PDT-90F"),
    GENERIC_CT58("CT58", "CT58"),
    GENERIC_KP18("KP18", "KP18"),

    /**
     * Represents an unknown or unsupported device.
     */
    OTHER("OTHER");

    private final String canonicalName;
    private final String[] allNames; // Includes canonical name and all aliases
    private final String[] modelNames;

    /**
     * Constructor for brands with multiple possible manufacturer names (aliases).
     * @param manufacturerNames An array of names, where the first is the canonical name.
     * @param modelNames A varargs array of supported model names.
     */
    ScannerBrand(String[] manufacturerNames, String... modelNames) {
        if (manufacturerNames == null || manufacturerNames.length == 0) {
            throw new IllegalArgumentException("Manufacturer names cannot be null or empty.");
        }
        this.canonicalName = manufacturerNames[0];
        this.allNames = manufacturerNames;
        this.modelNames = modelNames;
    }

    /**
     * Convenience constructor for brands with a single manufacturer name.
     * @param manufacturerName The primary manufacturer name.
     * @param modelNames A varargs array of supported model names.
     */
    ScannerBrand(String manufacturerName, String... modelNames) {
        this(new String[]{manufacturerName}, modelNames);
    }

    /**
     * Gets the primary, or canonical, name for the brand.
     * @return The canonical manufacturer name as a String.
     */
    public String getCanonicalName() {
        return canonicalName;
    }

    /**
     * Gets all known names for the brand, including the canonical name and any aliases.
     * @return An array of all manufacturer names.
     */
    public String[] getAllNames() {
        return allNames;
    }

    /**
     * Gets the list of known models for the brand.
     * @return An array of model names.
     */
    public String[] getModelNames() {
        return modelNames;
    }

    /**
     * Determines the scanner brand based on the device's Build.MANUFACTURER and Build.MODEL.
     * <p>
     * This method first attempts to match the device model for higher accuracy. If no model
     * matches, it then falls back to matching against the manufacturer's name and all its known aliases.
     *
     * @return The identified {@link ScannerBrand}, or {@link ScannerBrand#OTHER} if no match is found.
     */
    public static ScannerBrand getCurrent() {
        final String model = Build.MODEL;
        final String manufacturer = Build.MANUFACTURER;

        Log.d("ScannerBrand", "Identifying device -> Model: " + model + ", Manufacturer: " + manufacturer);

        // 1. Check for a specific model match (most precise).
        for (ScannerBrand brand : values()) {
            for (String modelName : brand.getModelNames()) {
                if (modelName.equalsIgnoreCase(model)) {
                    Log.d("ScannerBrand", "Matched model '" + model + "' to brand " + brand.name());
                    return brand;
                }
            }
        }

        // 2. If no model matches, check by manufacturer name and its aliases.
        for (ScannerBrand brand : values()) {
            for (String brandName : brand.getAllNames()) {
                if (brandName.equalsIgnoreCase(manufacturer)) {
                    Log.d("ScannerBrand", "Matched manufacturer '" + manufacturer + "' to brand " + brand.name());
                    return brand;
                }
            }
        }

        Log.d("ScannerBrand", "No specific match found. Defaulting to OTHER.");
        return OTHER;
    }
}
