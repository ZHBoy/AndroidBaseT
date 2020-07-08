package com.ychd.ycwwz.base_library.extend

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.ychd.ycwwz.base_library.BaseApplication

/******************************************Gson的扩展******************************************/

val gson
    get() = Gson()

fun Any.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T : Any> Gson.fromJson(json: String): T {
    return gson.fromJson(json, T::class.java)
}


/**
 * 短提示
 * 用法: "string".toast(context)
 */
fun Any.toastShort(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

/**
 * 长提示
 * 用法: "string".toast(context)
 */
fun Any.toastLong(context: Context, duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

/******************************************Log的扩展******************************************/

/**
 * 扩展log info
 */
fun Any.logDebgger(msg: String) {
    if (BaseApplication.debugging) {
        Log.d("_" + this::class.java.simpleName, msg)
    }
}

/**
 * 扩展log error
 */
fun Any.logError(msg: String) {
    if (BaseApplication.debugging) {
        Log.e("_" + this::class.java.simpleName, msg)
    }
}

/******************************************View的点击延时，防重复点击******************************************/

/**
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else -601
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 600
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

/**
 * 带延迟过滤的点击事件监听，见[View.OnClickListener]
 * 延迟时间根据triggerDelay获取：600毫秒，不能动态设置
 */
interface OnLazyClickListener : View.OnClickListener {

    override fun onClick(v: View?) {
        if (v?.clickEnable() == true) {
            onLazyClick(v)
        }
    }

    fun onLazyClick(v: View)
}