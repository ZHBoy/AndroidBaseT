package com.zhboy.ycwwz.base_library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import android.annotation.TargetApi
import android.app.Activity
import com.zhboy.ycwwz.base_library.BaseApplication
import com.zhboy.ycwwz.base_library.BaseApplication.Companion.appContext
import java.lang.reflect.Method

/**
 *@author : HaoBoy
 *@date : 2019/4/10
 *@description :设备工具类,获取AndroidID、IMEi
 **/

class DevicesUtils {

    companion object {
        val instance: DevicesUtils by lazy { DevicesUtils() }
    }

    private var telephonemanager: TelephonyManager? = null

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(appContext!!.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取设备唯一标识
     * ⚠️ 调用前必须检测 READ_PHONE_STATE权限
     */
    fun getIMEI(ctx: Context): String {
        var imei = ""
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  //4.0以下 直接获取
                imei = getAndroidId()
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //5。0，6。0系统
                val imeiMaps = getImeiAndMeid(ctx)
                imei = getTransform(imeiMaps)
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // 10以下
                val imeiMaps = getIMEIforO(ctx)
                imei = try {
                    getTransform(imeiMaps)
                } catch (e: Exception) {
                    getAndroidId()
                }
            } else {
                if (getIdfa() == null || "".equals(getIdfa())) {
                    imei = getAndroidId()
                } else {
                    imei = getIdfa()
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        return imei
    }

    /**
     * 获取唯一标识idfa
     * <p>
     * 支持获取oaid的，优先获取oaid，
     * 其次是IMEI，
     * 如果没有得到IMEI权限，则获取AndroidId
     *
     * @param context
     * @return
     */
    fun getIdfa(): String {
        return BaseApplication.getOaid()
    }

    /**
     * 5.0统一使用这个获取IMEI IMEI2 MEID
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    @TargetApi(Build.VERSION_CODES.M)
    fun getImeiAndMeid(ctx: Context): Map<String, String> {
        val map = HashMap<String, String>()
        val mTelephonyManager = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager

        var clazz: Class<*>? = null
        var method: Method? = null//(int slotId)

        try {
            clazz = Class.forName("android.os.SystemProperties")
            method = clazz!!.getMethod("get", String::class.java, String::class.java)
            val gsm = method!!.invoke(null, "ril.gsm.imei", "") as String
            val meid = method!!.invoke(null, "ril.cdma.meid", "") as String
            map["meid"] = meid
            if (!TextUtils.isEmpty(gsm)) {
                //the value of gsm like:xxxxxx,xxxxxx
                val imeiArray =
                    gsm.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (imeiArray != null && imeiArray.size > 0) {
                    map["imei1"] = imeiArray[0]

                    if (imeiArray.size > 1) {
                        map["imei2"] = imeiArray[1]
                    } else {
                        if (mTelephonyManager?.getDeviceId(1) != null) {
                            map["imei2"] = mTelephonyManager.getDeviceId(1)
                        } else {
                            map["imei2"] = ""
                        }

                    }
                } else {
                    if (mTelephonyManager?.getDeviceId(0) != null) {
                        map["imei1"] = mTelephonyManager.getDeviceId(0)
                    } else {
                        map["imei1"] = ""
                    }
                    if (mTelephonyManager?.getDeviceId(1) != null) {
                        map["imei2"] = mTelephonyManager.getDeviceId(1)
                    } else {
                        map["imei2"] = ""
                    }

                }
            } else {
                if (mTelephonyManager?.getDeviceId(0) != null) {
                    map["imei1"] = mTelephonyManager.getDeviceId(0)
                } else {
                    map["imei1"] = ""
                }
                if (mTelephonyManager?.getDeviceId(1) != null) {
                    map["imei2"] = mTelephonyManager.getDeviceId(1)
                } else {
                    map["imei2"] = ""
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return map
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    fun getIMEIforO(context: Context): Map<String, String> {
        val map = HashMap<String, String>()
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei1 = tm.getImei(0)
        val imei2 = tm.getImei(1)
        if (TextUtils.isEmpty(imei1) && TextUtils.isEmpty(imei2)) {
            map["imei1"] = tm.meid ?: "imei_default_000000" //如果CDMA制式手机返回MEID
        } else {
            if (TextUtils.isEmpty(imei1).not()) {
                map["imei1"] = imei1
            }
            if (TextUtils.isEmpty(imei2).not()) {
                map["imei2"] = imei2
            }
        }
        return map
    }

    private fun getTransform(imeiMaps: Map<String, String>?): String {
        var imei = ""
        if (imeiMaps != null) {
            val imei1 = imeiMaps["imei1"] as String
            if (TextUtils.isEmpty(imei1)) {
                return imei
            }
            val imei2 = imeiMaps["imei2"] as String
            if (imei2 != null) {
                if (imei1.trim { it <= ' ' }.length == 15 && imei2.trim { it <= ' ' }.length == 15) {
                    //如果两个位数都是15。说明都是有效IMEI。根据从大到小排列
                    val i1 = java.lang.Long.parseLong(imei1.trim { it <= ' ' })
                    val i2 = java.lang.Long.parseLong(imei2.trim { it <= ' ' })
                    if (i1 > i2) {
                        imei = "$imei2;$imei1"
                    } else {
                        imei = "$imei1;$imei2"
                    }

                } else {  //
                    if (imei1.trim { it <= ' ' }.length == 15) {
                        //如果只有imei1是有效的
                        imei = imei1
                    } else if (imei2.trim { it <= ' ' }.length == 15) {
                        //如果只有imei2是有效的
                        imei = imei2
                    } else {
                        //如果都无效那么都为meid。只取一个就可以
                        imei = imei1
                    }

                }
            } else {
                imei = imei1
            }
        }
        return imei
    }

    /**
     * 获取application中指定的meta-data。本例中，调用方法时key就是UMENG_CHANNEL
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    fun getAppMetaData(ctx: Context?, key: String): String? {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null
        }
        var resultData: String? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                val applicationInfo =
                    packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key)
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return resultData
    }

    fun getMobileBrand(): String {
        return android.os.Build.BRAND
    }

    fun getOsVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }


    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    @Suppress("DEPRECATION")
    fun getAPNType(context: Context): String {
        //结果返回值
        var netType = ""
        //获取手机所有连接管理对象
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        //NetworkInfo对象为空 则代表没有网络
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = "WIFI"
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            netType =
                if (nSubType == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming) {
                    "4G"
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0 && !telephonyManager.isNetworkRoaming
                ) {
                    "3G"
                } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA && !telephonyManager.isNetworkRoaming
                ) {
                    "2G"
                } else {
                    "2G"
                }
        }
        return netType
    }


    @Suppress("DEPRECATION")
    fun getIpAddress(context: Context): String? {
        val info =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            // 3/4g网络
            when {
                info.type == ConnectivityManager.TYPE_MOBILE -> try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }
                info.type == ConnectivityManager.TYPE_WIFI -> {
                    //  wifi网络
                    val wifiManager = appContext!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiInfo = wifiManager.connectionInfo
                    return intIP2StringIP(wifiInfo.ipAddress)
                }
                info.type == ConnectivityManager.TYPE_ETHERNET -> // 有限网络
                    return getLocalIp()
            }
        }
        return null
    }

    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }


    // 获取有线网IP
    private fun getLocalIp(): String {
        try {
            val en = NetworkInterface
                .getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf
                    .inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {

        }

        return "0.0.0.0"

    }

    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                    ?: return false

            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            //有网
            return mNetworkInfo != null
        }
        return false//没有网
    }
}