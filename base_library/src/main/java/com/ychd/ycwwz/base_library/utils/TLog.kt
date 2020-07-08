package com.ychd.ycwwz.base_library.utils

import android.util.Log
import com.ychd.ycwwz.base_library.BaseApplication
import org.jetbrains.anko.AnkoLogger

/**
 * @author zhou_hao
 * @date 2020-02-12
 * @description: Log封装
 */
object TLog : AnkoLogger {

    private val isOpenDebug = BaseApplication.debugging

    private val debugInfo = true
    private val debugError = true

    @Synchronized
    fun i(msg: String) {
        if (isOpenDebug && debugInfo) {
            Log.i("TLog", msg)
        }

    }

    @Synchronized
    fun i(tag: String, msg: String) {
        if (isOpenDebug && debugInfo) {
            Log.i(tag, msg)
        }
    }

    @Synchronized
    fun i(msg: String, count: Int) {
        if (isOpenDebug && debugInfo) {
            if (msg.length > count) {
                val show = msg.substring(0, count)
                i(show)
                if ((msg.length - count) > count) {
                    val partLog = msg.substring(count, msg.length)
                    i(partLog, count)
                } else {
                    val surplusLog = msg.substring(count, msg.length)
                    i(surplusLog)
                }
            } else {
                i(msg)
            }
        }
    }

    fun e(msg: String) {
        if (isOpenDebug && debugError)
            try {
                error(msg)
            } catch (e: Exception) {
            }
    }


}