package com.zhboy.ycwwz.web_library.webview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.webkit.WebChromeClient
import com.zhboy.ycwwz.base_library.BaseApplication
import com.zhboy.ycwwz.web_library.R

/**
 * @author zhou_hao
 * @date 2020-07-07
 * @description: 重写getDefaultVideoPoster
 */
open class CustomWebCromeClient : WebChromeClient() {

    /**
     * 含视频H5崩溃的问题
     */
    override fun getDefaultVideoPoster(): Bitmap? {
        if (super.getDefaultVideoPoster() == null) {
            return BitmapFactory.decodeResource(
                BaseApplication.appContext!!.resources,
                R.mipmap.icon_lancher
            )
        }
        return super.getDefaultVideoPoster()
    }
}