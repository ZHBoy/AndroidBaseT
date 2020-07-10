package com.zhboy.ycwwz.game_library.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.WindowManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhboy.ycwwz.base_library.IntentDataDef
import com.zhboy.ycwwz.base_library.base.BaseActivity
import com.zhboy.ycwwz.base_library.extend.OnLazyClickListener
import com.zhboy.ycwwz.base_library.widgets.LoadingDialog
import com.zhboy.ycwwz.game_library.R
import com.zhboy.ycwwz.provider_library.router.common.RouterApi
import com.zhboy.ycwwz.web_library.webview.CustomWebCromeClient
import com.zhboy.ycwwz.web_library.webview.WebDataDef
import kotlinx.android.synthetic.main.game_activity_layout.*
import org.jetbrains.anko.intentFor

/**
 * @author zhou_hao
 * @date 2020-07-09
 * @description: 游戏页面
 */
@Route(path = RouterApi.GameLibrary.ROUTER_GAME_HOME)
class GameActivity : BaseActivity(), OnLazyClickListener {

    private var toUrl: String? = null

    override fun resLayout(): Int = R.layout.game_activity_layout

    override fun init() {
        toUrl = intent.getStringExtra(IntentDataDef.INTENT_WEB_GAME_DETAIL_URL_KEY)

        setWebViewClient()
        gameActivityWebV.webChromeClient = MyWebChromeClient()
        gameActivityWebV.loadUrl(toUrl)
    }

    override fun logic() {
        toolbarBackIv.setOnClickListener(this)
    }

    override fun onLazyClick(v: View) {
        when (v.id) {
            R.id.toolbarBackIv -> {
                if (gameActivityWebV != null && gameActivityWebV.canGoBack()) {
                    gameActivityWebV.goBack()
                } else {
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (gameActivityWebV != null && gameActivityWebV.canGoBack()) {
            gameActivityWebV.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        if (gameActivityWebV != null) {
            gameActivityWebV.visibility = View.GONE
            gameActivityWebV.webViewClient = null
            gameActivityWebV.webChromeClient = null
            gameActivityWebV.removeJavascriptInterface(WebDataDef.WEBVIEW_JAVASCRIPT_INTERFACE)
            gameActivityWebV.destroy()
        }
        super.onDestroy()
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

    /**
     * 设置WebViewClient
     */
    private fun setWebViewClient() {
        gameActivityWebV.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                LoadingDialog.show(
                    this@GameActivity,
                    ""
                )
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null) {
                    //android4.4.4加载错误界面data:text/html,chromewebdata
                    if (url.startsWith("http") || url.startsWith("https")) {
                        gameActivityWebV.visibility = View.VISIBLE
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

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }
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
                return BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.icon_lancher
                )
            }
            return super.getDefaultVideoPoster()
        }
    }

    companion object {

        //控制要不要显示顶部bar
        fun startGameActivity(con: Context, url: String) {
            con.startActivity(
                con.intentFor<GameActivity>(
                    IntentDataDef.INTENT_WEB_GAME_DETAIL_URL_KEY to url
                )
            )
        }
    }

}