package com.zhboy.ycwwz.base_library.utils

import android.app.ActivityManager
import android.content.ClipData
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.util.ArrayList


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


class CommonUtils {
    companion object {
        val instance: CommonUtils by lazy { CommonUtils() }
    }

    //通过PackageInfo得到的想要启动的应用的包名
    private fun getPackageInfo(context: Context): PackageInfo? {
        var pInfo: PackageInfo? = null

        try {
            //通过PackageManager可以得到PackageInfo
            val pManager = context.getPackageManager()
            pInfo = pManager.getPackageInfo(
                context.getPackageName(),
                PackageManager.GET_CONFIGURATIONS
            )

            return pInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pInfo
    }

    /**
     * 获取当前进程名
     */
    fun getProcessName(context: Context): String? {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.runningAppProcesses?.map {
            if (it.pid == android.os.Process.myPid()) {
                return it.processName
            }
        }
        return null
    }

    /**
     * 自动弹出弹出软键盘
     */
    fun showKeyboard(context: Context, editText: EditText) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.showSoftInput(editText, 0)
    }

    /**
     * 复制微信到剪切板
     */
    fun copy(context: Context, text: String) {

        //获取剪贴板管理器
        val cm = context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", text)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }


    /**
     * 启动浏览器打开页面
     */
    fun startBrowser(mContext: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
        mContext.startActivity(intent)
    }

    fun bitmapTobyteArray(bitmap: Bitmap): ByteArray {
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val datas = baos.toByteArray()
        return datas
    }

    //获取包名
    fun getPackageName(context: Context): String {
        return getPackageInfo(context)!!.packageName
    }

    fun <T> averageAssignFixLength(source: List<T>?, splitItemNum: Int): List<List<T>> {
        val result = ArrayList<List<T>>()

        if (source != null && source.run { isNotEmpty() } && splitItemNum > 0) {
            if (source.size <= splitItemNum) {
                // 源List元素数量小于等于目标分组数量
                result.add(source)
            } else {
                // 计算拆分后list数量
                val splitNum =
                    if (source.size % splitItemNum == 0) source.size / splitItemNum else source.size / splitItemNum + 1

                var value: List<T>? = null
                for (i in 0 until splitNum) {
                    if (i < splitNum - 1) {
                        value = source.subList(i * splitItemNum, (i + 1) * splitItemNum)
                    } else {
                        // 最后一组
                        value = source.subList(i * splitItemNum, source.size)
                    }

                    result.add(value)
                }
            }
        }

        return result
    }

    fun glodtoAccount(glodContent:String):String{

        var glodContentDouble:Double=glodContent.toDouble()

        var l=10000

        var df:DecimalFormat= DecimalFormat("0.00")

        return df.format((glodContentDouble/l))

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
     fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}