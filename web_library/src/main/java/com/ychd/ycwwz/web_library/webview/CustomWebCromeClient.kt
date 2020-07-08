package com.ychd.ycwwz.web_library.webview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.webkit.WebChromeClient
import com.ychd.hweather.web_library.R
import com.ychd.ycwwz.base_library.BaseApplication

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