package com.ychd.ycwwz.web_library.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.webkit.*
 import com.ychd.ycwwz.web_library.webview.JsInterface
import com.ychd.ycwwz.base_library.CommonDef
import com.ychd.ycwwz.base_library.IntentDataDef
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.widgets.LoadingDialog
import com.ychd.ycwwz.web_library.R
import com.ychd.ycwwz.web_library.webview.CustomWebCromeClient
import com.ychd.ycwwz.web_library.webview.WebDataDef
import kotlinx.android.synthetic.main.activity_web_view_common.*
import org.jetbrains.anko.intentFor

/**
 *@author : HaoBoy
 *@date : 2018/8/28
 *@description :
 **/
class CommonWebActivity : BaseActivity(), View.OnClickListener {
    override fun resLayout(): Int = R.layout.activity_web_view_common

    override fun init() {
        wvCommon.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                LoadingDialog.show(
                    this@CommonWebActivity,
                    ""
                )
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null) {
                    //android4.4.4加载错误界面data:text/html,chromewebdata
                    if (url.startsWith("http") || url.startsWith("https")) {
                        wvCommon.visibility = View.VISIBLE
                    }
                }
                LoadingDialog.dismiss()
            }

            //无网时调用
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
//                wvCommon.visibility = View.GONE
                LoadingDialog.dismiss()
            }

            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                return false
            }
        }

        wvCommon.webChromeClient = MyWebChromeClient()
        initData(intent)
    }


    override fun onClick(view: View?) {
        if (view == toolbarBackIv) {
            if (wvCommon != null && wvCommon.canGoBack()) {
                wvCommon.goBack()
            } else {
                finish()
            }
        }
    }

    private var toUrl: String? = null

    //是否展示听不toolBar
    private var isShowToolBar: Boolean = true


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        initData(intent)
    }

    @SuppressLint("AddJavascriptInterface")
    fun initData(intent: Intent) {

        toUrl = intent.getStringExtra(IntentDataDef.INTENT_WEB_COMMON_URL_KEY)

        isShowToolBar = intent.getBooleanExtra(IntentDataDef.INTENT_WEB_IS_SHOW_TOOL_BAR, true)

        if (isShowToolBar) {
            rlToolbar.visibility = View.VISIBLE
        } else {
            rlToolbar.visibility = View.GONE
        }

        //设置交互作用域
        wvCommon.addJavascriptInterface(JsInterface(this), CommonDef.WEBVIEW_JAVASCRIPT_INTERFACE)
        if (toUrl != null && !TextUtils.isEmpty(toUrl)) {
            wvCommon.loadUrl(toUrl)
        }

    }

    override fun logic() {
        toolbarBackIv.setOnClickListener(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }
    }

    override fun onBackPressed() {
        if (wvCommon != null && wvCommon.canGoBack()) {
            wvCommon.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        if (wvCommon != null) {
            wvCommon.visibility = View.GONE
            wvCommon.webViewClient = null
            wvCommon.webChromeClient = null
            wvCommon.removeJavascriptInterface(WebDataDef.WEBVIEW_JAVASCRIPT_INTERFACE)
            wvCommon.destroy()
        }

        super.onDestroy()
    }

    /**
     * 重写WebChromeClient，适配全屏播放视频
     */
    private inner class MyWebChromeClient : CustomWebCromeClient() {
        private var mCustomViewCallback: CustomViewCallback? = null
        //  横屏时，显示视频的view
        private var mCustomView: View? = null

        //全屏播放的时候
        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            super.onShowCustomView(view, callback)
            if (mCustomView != null) {
                callback.onCustomViewHidden()
                return
            }

            mCustomView = view
            mCustomView!!.visibility = View.VISIBLE
            mCustomViewCallback = callback
            flVideo.addView(mCustomView)
            flVideo.visibility = View.VISIBLE
            flVideo.bringToFront()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        //隐藏全屏播放
        override fun onHideCustomView() {
            super.onHideCustomView()
            if (mCustomView == null) {
                return
            }
            mCustomView!!.visibility = View.GONE
            flVideo.removeView(mCustomView)
            mCustomView = null
            flVideo.visibility = View.GONE
            try {
                mCustomViewCallback!!.onCustomViewHidden()
            } catch (e: Exception) {
            }

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            toolbarTitleTv.text = title
        }

        override fun getDefaultVideoPoster(): Bitmap? {
            if (super.getDefaultVideoPoster() == null) {
                return BitmapFactory.decodeResource(resources, R.mipmap.icon_lancher);
            }
            return super.getDefaultVideoPoster()
        }
    }

    companion object {

        //控制要不要显示顶部bar
        fun startCommonWebActivity(con: Context, url: String, isShowToolBar: Boolean) {
            con.startActivity(
                con.intentFor<CommonWebActivity>(
                    IntentDataDef.INTENT_WEB_IS_SHOW_TOOL_BAR to isShowToolBar,
                    IntentDataDef.INTENT_WEB_COMMON_URL_KEY to url
                )
            )
        }
    }
}
