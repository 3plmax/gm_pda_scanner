package com.shipitdone.scanner.manager.variant

import android.content.Context
import android.content.Intent

class IdataScannerInterface(
    private val mContext: Context?
) {
    fun ShowUI() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_SHOWISCANUI)
            mContext.sendBroadcast(intent)
        }
    }

    //	2.打开扫描头电源
    fun open() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_BARCODE_ENABLESCANNER_ACTION)
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, true)
            mContext.sendBroadcast(intent)
        }
    }

    //2.关闭扫描头电源
    fun close() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_BARCODE_ENABLESCANNER_ACTION)
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, false)
            mContext.sendBroadcast(intent)
        }
    }

    // 3. 触发扫描头，扫描头出光
    /*
	 * 此函数和 scan_stop 配合使用可以在程序中软件触发扫描头。当扫描头处于空闲状
 态时,调用 scan_start 可以触发扫描头出光扫描。扫描完毕或超时后,必须调用
scan_start 恢复扫描头状态。
	 *
	 * */
    fun scan_start() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_BARCODE_STARTSCAN_ACTION)
            mContext.sendBroadcast(intent)
        }
    }

    //4.停止扫描头解码，扫描头灭光
    /**
     * 此函数和 scan_stop 配合使用可以在程序中软件触发扫描头。当应用程序调用
     * scan_start 触发扫描头出光扫描后, 必须调用 scan_stop 恢复扫描头状态。
     *
     */
    fun scan_stop() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_BARCODE_STOPSCAN_ACTION)
            mContext.sendBroadcast(intent)
        }
    }

    /**锁定设备的扫描按键，锁定后，只能通过iScan定义的扫描按键控制扫描，用户无法自定义按键。
     */
    fun lockScanKey() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_LOCK_SCAN_ACTION)
            mContext.sendBroadcast(intent)
        }
    }

    /******
     * 解除对扫描按键的锁定。解除后iScan无法控制扫描键，用户可自定义按键。
     */
    fun unlockScanKey() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_UNLOCK_SCAN_ACTION)
            mContext.sendBroadcast(intent)
        }
    }

    /**KEY_CHARSET_ACTION   广播设置编码格式
     * 0  <item>ASCII</item>
     * 1  <item>GB2312</item>
     * 2  <item>GBK</item>
     * 3  <item>GB18030</item>
     * 4  <item>UTF-8</item> */
    fun setCharSetMode(mode: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_CHARSET_ACTION)
            intent.putExtra(KEY_CHARSET_ACTION, mode)
            mContext.sendBroadcast(intent)
        }
    }


    //设置一次最多解码数量
    fun setMaxMultireadCount(mode: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_SETMAXMULTIREADCOUNT_ACTION)
            intent.putExtra(KEY_SETMAXMULTIREADCOUNT_ACTION, mode)
            mContext.sendBroadcast(intent)
        }
    }


    /**扫描头的输出模式
     * mode 0:扫描结果直接发送到焦点编辑框内
     * mode 1:扫描结果以广播模式发送，应用程序需要注册action为“android.intent.action.SCANRESULT”的广播接收器，在广播机的 onReceive(Context context, Intent arg1) 方法中,通过如下语句
     * String  barocode=arg1.getStringExtra("value");
     * int barocodelen=arg1.getIntExtra("length",0);
     * 分别获得 条码值,条码长度,条码类型
     */
    fun setOutputMode(mode: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_OUTPUT_ACTION)
            intent.putExtra(KEY_OUTPUT_ACTION, mode)
            mContext.sendBroadcast(intent)
        }
    }

    /**8 是否播放声音 */
    fun enablePlayBeep(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_BEEP_ACTION)
            intent.putExtra(KEY_BEEP_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    /**扫描失败是否播放声音 */
    fun enableFailurePlayBeep(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_FAILUREBEEP_ACTION)
            intent.putExtra(KEY_FAILUREBEEP_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }


    /**9 是否震动 */
    fun enablePlayVibrate(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_VIBRATE_ACTION)
            intent.putExtra(KEY_VIBRATE_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    /**KEY_POWER_ACTION   省电模式
     * true 开启，该模式下屏幕锁屏后将无法扫描
     * false 关闭，该模式相反 */
    fun enablePower(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_POWER_ACTION)
            intent.putExtra(KEY_POWER_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    /**  附加回车、换行等
     * 0 <item>无</item>
     * 1 <item>附加回车键</item>
     * 2 <item>附加TAB键</item>
     * 3 <item>附加换行符</item> */
    fun enableAddKeyValue(value: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_TERMINATOR_ACTION)
            intent.putExtra(KEY_TERMINATOR_ACTION, value)
            mContext.sendBroadcast(intent)
        }
    }

    /** */ //KEY_PREFIX_ACTION 添加前缀
    fun addPrefix(text: String?) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_PREFIX_ACTION)
            intent.putExtra(KEY_PREFIX_ACTION, text)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_SUFFIX_ACTION添加后缀
    fun addSuffix(text: String?) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_SUFFIX_ACTION)
            intent.putExtra(KEY_SUFFIX_ACTION, text)
            mContext.sendBroadcast(intent)
        }
    }

    //截取左字符KEY_TRIMLEFT_ACTION
    fun interceptTrimleft(num: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_TRIMLEFT_ACTION)
            intent.putExtra(KEY_TRIMLEFT_ACTION, num)
            mContext.sendBroadcast(intent)
        }
    }

    //截取右字符KEY_TRIMRIGHT_ACTION
    fun interceptTrimright(num: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_TRIMRIGHT_ACTION)
            intent.putExtra(KEY_TRIMRIGHT_ACTION, num)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_LIGHT_ACTION 右侧Led灯光控制
    fun lightSet(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_LIGHT_ACTION)
            intent.putExtra(KEY_LIGHT_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_TIMEOUT_ACTION 设置超时时间
    fun timeOutSet(value: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_TIMEOUT_ACTION)
            intent.putExtra(KEY_TIMEOUT_ACTION, value)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_FILTERCHARACTER_ACTION  //过滤特定字符
    fun filterCharacter(text: String?) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_FILTERCHARACTER_ACTION)
            intent.putExtra(KEY_FILTERCHARACTER_ACTION, text)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_CONTINUCESCAN_ACTION  是否连扫
    fun continceScan(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_CONTINUCESCAN_ACTION)
            intent.putExtra(KEY_CONTINUCESCAN_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_INTERVALTIME_ACTION  连续扫描间隔时间
    fun intervalSet(value: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_INTERVALTIME_ACTION)
            intent.putExtra(KEY_INTERVALTIME_ACTION, value)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_FAILUREBROADCAST_ACTION 扫描失败广播
    fun SetErrorBroadCast(enable: Boolean) {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_FAILUREBROADCAST_ACTION)
            intent.putExtra(KEY_FAILUREBROADCAST_ACTION, enable)
            mContext.sendBroadcast(intent)
        }
    }

    //KEY_RESET_ACTION  恢复默认设置
    fun resultScan() {
        if (mContext != null) {
            val intent: Intent = Intent(KEY_RESET_ACTION)
            mContext.sendBroadcast(intent)
        }
    }

    //SCANKEY_CONFIG_ACTION   扫描按键配置
    //KEYCODE 按键名称   value有两个0，1  0表示扫描，1不做任何操作
    fun scanKeySet(keycode: Int, value: Int) {
        if (mContext != null) {
            val intent: Intent = Intent(SCANKEY_CONFIG_ACTION)
            intent.putExtra("KEYCODE", keycode)
            intent.putExtra("value", value)
            mContext.sendBroadcast(intent)
        }
    }

    companion object {
        /********************************************扫描接口定义常量 */ //打开与关闭扫描头
        const val KEY_BARCODE_ENABLESCANNER_ACTION: String = "android.intent.action.BARCODESCAN"

        //开始扫描
        const val KEY_BARCODE_STARTSCAN_ACTION: String = "android.intent.action.BARCODESTARTSCAN"

        //停止扫描
        const val KEY_BARCODE_STOPSCAN_ACTION: String = "android.intent.action.BARCODESTOPSCAN"

        //锁定扫描键
        const val KEY_LOCK_SCAN_ACTION: String = "android.intent.action.BARCODELOCKSCANKEY"

        //解锁扫描键
        const val KEY_UNLOCK_SCAN_ACTION: String = "android.intent.action.BARCODEUNLOCKSCANKEY"

        //声音android.intent.action.BEEP
        const val KEY_BEEP_ACTION: String = "android.intent.action.BEEP"

        //扫描失败提示音
        const val KEY_FAILUREBEEP_ACTION: String = "android.intent.action.FAILUREBEEP"

        //震动android.intent.action.VIBRATE
        const val KEY_VIBRATE_ACTION: String = "android.intent.action.VIBRATE"

        //是否广播模式
        const val KEY_OUTPUT_ACTION: String = "android.intent.action.BARCODEOUTPUT"

        //广播设置编码格式
        const val KEY_CHARSET_ACTION: String = "android.intent.actionCHARSET"

        //省电模式
        const val KEY_POWER_ACTION: String = "android.intent.action.POWER"

        //附加内容
        const val KEY_TERMINATOR_ACTION: String = "android.intent.TERMINATOR"

        //通知栏图标显示android.intent.action.SHOWNOTICEICON
        const val KEY_SHOWNOTICEICON_ACTION: String = "android.intent.action.SHOWNOTICEICON"

        //APP图标显示android.intent.action.SHOWAPPICON
        const val KEY_SHOWICON_ACTION: String = "android.intent.action.SHOWAPPICON"

        //打开扫描设置界面
        const val KEY_SHOWISCANUI: String = "com.android.auto.iscan.show_setting_ui"

        //添加前缀
        const val KEY_PREFIX_ACTION: String = "android.intent.action.PREFIX"

        //后缀
        const val KEY_SUFFIX_ACTION: String = "android.intent.action.SUFFIX"

        //截取左字符
        const val KEY_TRIMLEFT_ACTION: String = "android.intent.action.TRIMLEFT"

        //截取右字符
        const val KEY_TRIMRIGHT_ACTION: String = "android.intent.action.TRIMRIGHT"

        //右上侧Led灯光控制
        const val KEY_LIGHT_ACTION: String = "android.intent.action.LIGHT"

        //设置超时时间
        const val KEY_TIMEOUT_ACTION: String = "android.intent.action.TIMEOUT"

        //过滤特定字符
        const val KEY_FILTERCHARACTER_ACTION: String = "android.intent.action.FILTERCHARACTER"

        //连扫
        const val KEY_CONTINUCESCAN_ACTION: String = "android.intent.action.BARCODECONTINUCESCAN"

        //连续扫描间隔时间
        const val KEY_INTERVALTIME_ACTION: String = "android.intent.action.INTERVALTIME"

        //是否删除编辑框内容
        const val KEY_DELELCTED_ACTION: String = "android.intent.action.DELELCTED"

        //恢复默认设置
        const val KEY_RESET_ACTION: String = "android.intent.action.RESET"

        //扫描按键配置
        const val SCANKEY_CONFIG_ACTION: String = "android.intent.action.scankeyConfig"

        //扫描失败广播
        const val KEY_FAILUREBROADCAST_ACTION: String = "android.intent.action.FAILUREBROADCAST"

        //设置解码数量
        const val KEY_SETMAXMULTIREADCOUNT_ACTION: String =
            "android.intent.action.MAXMULTIREADCOUNT"

        /** */
        /********************************************系统接口定义常量 */
        const val SET_STATUSBAR_EXPAND: String = "com.android.set.statusbar_expand"
        const val SET_USB_DEBUG: String = "com.android.set.usb_debug"
        const val SET_INSTALL_PACKAGE: String = "com.android.set.install.package"
        const val SET_SCREEN_LOCK: String = "com.android.set.screen_lock"
        const val SET_CFG_WAKEUP_ANYKEY: String = "com.android.set.cfg.wakeup.anykey"
        const val SET_UNINSTALL_PACKAGE: String = "com.android.set.uninstall.package"
        const val SET_SYSTEM_TIME: String = "com.android.set.system.time"
        const val SET_KEYBOARD_CHANGE: String = "com.android.disable.keyboard.change"
        const val SET_INSTALL_PACKAGE_WITH_SILENCE: String =
            "com.android.set.install.packege.with.silence"
        const val SET_INSTALL_PACKAGE_EXTRA_APK_PATH: String =
            "com.android.set.install.packege.extra.apk.path"
        const val SET_INSTALL_PACKAGE_EXTRA_TIPS_FORMAT: String =
            "com.android.set.install.packege.extra.tips.format"
        const val SET_SIMULATION_KEYBOARD: String = "com.android.simulation.keyboard"
        const val SET_SIMULATION_KEYBOARD_STRING: String = "com.android.simulation.keyboard.string"
        private val androidjni: IdataScannerInterface? = null
    }
}
