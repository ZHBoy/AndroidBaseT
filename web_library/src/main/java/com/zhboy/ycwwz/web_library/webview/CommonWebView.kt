package com.zhboy.ycwwz.web_library.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * 设置公共Webview
 */
class CommonWebView : WebView {

    constructor(context: Context) : super(getFixedContext(context)) {
        setDefaultSetting()
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        getFixedContext(
            context
        ), attrs
    ) {
        setDefaultSetting()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        getFixedContext(
            context
        ), attrs, defStyleAttr
    ) {
        setDefaultSetting()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        getFixedContext(context),
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        setDefaultSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setDefaultSetting() {
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = false
        //noinspection deprecation
        settings.javaScriptEnabled = true //让webView支持JS
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true//支持localStorage
        settings.setAppCacheEnabled(false)
        settings.blockNetworkImage = false//解决图片不显示
        settings.databaseEnabled = true//支持database
        //设置字体大小（）
        settings.textZoom = WebDataDef.WEBVIEW_TEXT_ZOOM
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
    }

    companion object {
        //适配5.0机型
        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT in 21..22) context.createConfigurationContext(
                Configuration()
            ) else context
        }
    }
}
