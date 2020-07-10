package com.zhboy.ycwwz.game_library.ui

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.zhboy.ycwwz.base_library.CommonDef
import com.zhboy.ycwwz.base_library.base.BaseLazyFragment
import com.zhboy.ycwwz.base_library.extend.OnLazyClickListener
import com.zhboy.ycwwz.base_library.utils.TLog
import com.zhboy.ycwwz.base_library.widgets.LoadingDialog
import com.zhboy.ycwwz.game_library.R
import com.zhboy.ycwwz.web_library.webview.CustomWebCromeClient
import com.zhboy.ycwwz.web_library.webview.JsInterface
import kotlinx.android.synthetic.main.game_fragment_layout.*

/**
 * @author zhou_hao
 * @date 2020-07-09
 * @description: 游戏页面(fragment)
 */
class GameFragment : BaseLazyFragment(), OnLazyClickListener {

    override fun layoutResId(): Int = R.layout.game_fragment_layout

    //控制能否跳转二级页面
    private var isCanClickToGameActivity = false

    override fun onFirstVisible() {
        super.onFirstVisible()

        setWebViewClient()
        gameCommonWebView.webChromeClient = MyWebChromeClient()
        //设置交互作用域
        gameCommonWebView.addJavascriptInterface(
            JsInterface(requireActivity() as RxAppCompatActivity),
            CommonDef.WEBVIEW_JAVASCRIPT_INTERFACE
        )
        gameCommonWebView.loadUrl(CommonDef.game_web_url)
    }

    override fun onLazyClick(v: View) {

    }

    /**
     * 控制跳到二级页面
     */
    private fun setLinkEvent(url: String?): Boolean {
        if (url.isNullOrBlank()) {
            isCanClickToGameActivity = true
            return false
        }

        if (isCanClickToGameActivity && url.startsWith(CommonDef.game_web_url).not()) {
            GameActivity.startGameActivity(requireActivity(), url)
            return true
        }
        isCanClickToGameActivity = true
        return false
    }


    /**
     * 判断webview是否可以 回退
     */
    fun getWebViewIsCanBack(): Boolean {
        return if (gameCommonWebView != null && gameCommonWebView.canGoBack()) {
            gameCommonWebView.goBack()
            isCanClickToGameActivity = false
            true
        } else {
            false
        }
    }


    /**
     * 设置WebViewClient
     */
    private fun setWebViewClient() {
        gameCommonWebView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                TLog.i("GameFragment-> onPageStarted:$url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                TLog.i("GameFragment-> onPageFinished:$url")
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

            //适配低版本
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                TLog.i("GameFragment-> shouldOverrideUrlLoading-低版本:$url")
                return setLinkEvent(url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                TLog.i("GameFragment-> shouldOverrideUrlLoading-高版本:$url")
                return setLinkEvent(url)
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
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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

            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
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
        fun newInstance() = GameFragment()
    }
}