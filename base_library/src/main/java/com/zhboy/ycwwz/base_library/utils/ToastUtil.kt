package com.zhboy.ycwwz.base_library.utils

import android.view.Gravity
import android.widget.Toast
import com.zhboy.ycwwz.base_library.BaseApplication.Companion.appContext

/**
 * @author zhou_hao
 * @date 2020-02-12
 * @description: toast工具类
 */
object ToastUtil {

    private var toast: Toast? = null//实现不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长

    /**
     * 短时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    fun showShortToast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }

    /**
     * 短时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    fun showShortToastCenter(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT)
         toast!!.setGravity(Gravity.CENTER, 0, 0)
    } else {
        toast!!.setText(msg)
    }
    toast!!.show()

    }

    /**
     * 短时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    fun showShortToastTop(msg: String) {

        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT)
            toast!!.setGravity(Gravity.TOP, 0, 0)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()

    }

    /**
     * 长时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    fun showLongToast(msg: String) {

        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_LONG)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()

    }

    /**
     * 长时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    fun showLongToastCenter(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_LONG)
            toast!!.setGravity(Gravity.CENTER, 0, 0)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }

    /**
     * 长时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    fun showLongToastTop(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(appContext, msg, Toast.LENGTH_LONG)
            toast!!.setGravity(Gravity.TOP, 0, 0)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }
}