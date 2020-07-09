package com.ychd.ycwwz.web_library.webview

import android.webkit.JavascriptInterface
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.web_library.ui.CommonWebActivity

/**
 *@author : YMH
 *@date : 2020/3/03
 * 与js交互类
 */
class JsInterface(val activity: RxAppCompatActivity) {

    /**
     *待定
     */
    @JavascriptInterface
    fun callVideoForMoreLottery() {

    }

    /**返回**/
    @JavascriptInterface
    fun goBack() {
        activity.finish()
    }

    /**
     * 返回
     */
    @JavascriptInterface
    fun goNext() {
        activity.finish()
    }
}