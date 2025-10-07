package com.shipitdone.scanner.manager

import android.os.Build
import android.util.Log
import kotlin.arrayOf

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
enum class ScannerBrand(manufacturerNames: Array<String>, modelNames: Array<String> = arrayOf()) {
    // Manufacturers with known aliases
    // The first name in the array is considered the primary/canonical name.
    UROVO(arrayOf<String>("UROVO", "UBX"), arrayOf<String>("AS01")),
    NEWLAND(arrayOf<String>("NEWLAND", "NLS"), arrayOf<String>("NLS-MT90", "NLS-MT66", "NLS-MT6210", "NLS-MT9210")),

    // Manufacturers identified by a single primary name
    IDATA("IDATA", arrayOf<String>("T1", "MC005")),
    ALPS("ALPS"),
    SEUIC("SEUIC"),
    SIGAN("SIGAN", arrayOf<String>("SG6900")),
    SUNMI("SUNMI"),
    JEPOWER("JEPOWER", arrayOf<String>("HT380K")),
    HAND_HELD("HAND-HELD TERMINAL", arrayOf<String>("GM-B9920P")),
    ZEBRA("ZEBRA"),

    // Other generic or uncategorized devices
    GENERIC_PDA("PDA", arrayOf<String>("PDA")),
    GENERIC_PDT90F("PDT-90F", arrayOf<String>("PDT-90F")),
    GENERIC_CT58("CT58", arrayOf<String>("CT58")),
    GENERIC_KP18("KP18", arrayOf<String>("KP18")),

    /**
     * Represents an unknown or unsupported device.
     */
    OTHER("OTHER");

    /**
     * Gets the primary, or canonical, name for the brand.
     * @return The canonical manufacturer name as a String.
     */
    val canonicalName: String?

    /**
     * Gets all known names for the brand, including the canonical name and any aliases.
     * @return An array of all manufacturer names.
     */
    val allNames: Array<String> // Includes canonical name and all aliases

    /**
     * Gets the list of known models for the brand.
     * @return An array of model names.
     */
    val modelNames: Array<String>?

    /**
     * Constructor for brands with multiple possible manufacturer names (aliases).
     * @param manufacturerNames An array of names, where the first is the canonical name.
     * @param modelNames An array of supported model names.
     */
    init {
        require(!manufacturerNames.isEmpty()) { "Manufacturer names cannot be null or empty." }
        this.canonicalName = manufacturerNames[0]
        this.allNames = manufacturerNames
        this.modelNames = modelNames
    }

    /**
     * Convenience constructor for brands with a single manufacturer name.
     * @param manufacturerName The primary manufacturer name.
     * @param modelNames An array of supported model names.
     */
    constructor(manufacturerName: String?, modelNames: Array<String> = arrayOf()) : this(
        arrayOf<String>(
            manufacturerName!!
        ), modelNames
    )

    companion object {
        val current: ScannerBrand
            /**
             * Determines the scanner brand based on the device's Build.MANUFACTURER and Build.MODEL.
             *
             *
             * This method first attempts to match the device model for higher accuracy. If no model
             * matches, it then falls back to matching against the manufacturer's name and all its known aliases.
             *
             * @return The identified [ScannerBrand], or [ScannerBrand.OTHER] if no match is found.
             */
            get() {
                val model = Build.MODEL
                val manufacturer = Build.MANUFACTURER

                Log.d(
                    "ScannerBrand",
                    "Identifying device -> Model: $model, Manufacturer: $manufacturer"
                )

                // 1. Check for a specific model match (most precise).
                for (brand in ScannerBrand.entries) {
                    for (modelName in brand.modelNames!!) {
                        if (modelName.equals(model, ignoreCase = true)) {
                            Log.d(
                                "ScannerBrand",
                                "Matched model '" + model + "' to brand " + brand.name
                            )
                            return brand
                        }
                    }
                }

                // 2. If no model matches, check by manufacturer name and its aliases.
                for (brand in ScannerBrand.entries) {
                    for (brandName in brand.allNames) {
                        if (brandName.equals(manufacturer, ignoreCase = true)) {
                            Log.d(
                                "ScannerBrand",
                                "Matched manufacturer '" + manufacturer + "' to brand " + brand.name
                            )
                            return brand
                        }
                    }
                }

                Log.d("ScannerBrand", "No specific match found. Defaulting to OTHER.")
                return OTHER
            }
    }
}
