package com.ychd.ycwwz.game_library.ui

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.BaseApplication
import com.ychd.ycwwz.base_library.CommonDef
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.base.BaseLazyFragment
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.utils.TLog
import com.ychd.ycwwz.base_library.widgets.LoadingDialog
import com.ychd.ycwwz.game_library.R
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.web_library.webview.CustomWebCromeClient
import com.ychd.ycwwz.web_library.webview.JsInterface
import kotlinx.android.synthetic.main.game_fragment_layout.*

/**
 * @author zhou_hao
 * @date 2020-07-09
 * @description: 游戏页面(fragment)
 */
class GameFragment : BaseLazyFragment(), OnLazyClickListener {

    override fun layoutResId(): Int = R.layout.game_fragment_layout

    private var isLoadFinish = false

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
     * 设置WebViewClient
     */
    private fun setWebViewClient() {
        gameCommonWebView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                LoadingDialog.show(
                    requireActivity(),
                    ""
                )
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LoadingDialog.dismiss()
                TLog.i("GameFragment-> onPageFinished:$url")
                isLoadFinish = true
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
                val url = request?.url.toString()
                TLog.i("GameFragment-> shouldOverrideUrlLoading:$url")
                if (isLoadFinish && url.startsWith(CommonDef.game_web_url).not()) {
                    GameActivity.startGameActivity(requireActivity(), request!!.url.toString())
                    return true
                }
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