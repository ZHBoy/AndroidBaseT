package com.ychd.ycwwz.adlibrary.baiduad.banner

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.baidu.mobads.AdView
import com.baidu.mobads.AdViewListener
import com.baidu.mobads.AppActivity
import com.ychd.ycwwz.base_library.utils.TLog
import org.json.JSONObject


/**
 * @author:  YMH
 * @date: 2020/02/04
 * @description:
 **/
class BannerBDBuildTt(val activity: Activity?,val frameLayout:RelativeLayout, val adPlaceId: String, val scaleWidth:Int, val scaleHeight:Int, val listener: TTBannerListener?) {

    private var adView:AdView?=null
    fun init() {
        TLog.i("adserror","BannerBDBuildTt")
        if (activity != null) {
            // 默认请求http广告，若需要请求https广告，请设置AdSettings.setSupportHttps为true
            // AdSettings.setSupportHttps(true);
            // 代码设置AppSid，此函数必须在AdView实例化前调用
            // AdView.setAppSid("debug");
            // 设置'广告着陆页'动作栏的颜色主题
            // 目前开放了七大主题：黑色、蓝色、咖啡色、绿色、藏青色、红色、白色(默认) 主题
            AppActivity
                .setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_BLUE_THEME)
           bindBannerView()
        }
    }

    /**
     * 创建横幅广告的View，并添加至接界面布局中
     * 注意：只有将AdView添加到布局中后，才会有广告返回
     */
    public fun bindBannerView() {
        TLog.i("adserror","bindBannerView")
        adView = AdView(activity, adPlaceId)
        // 设置监听器
        adView?.setListener(object : AdViewListener {
            override fun onAdSwitch() {
                TLog.i("adserror","onAdSwitch")
            }

            override fun onAdShow(info: JSONObject) { // 广告已经渲染出来
                TLog.i("adserror","onAdShow")
            }

            override fun onAdReady(adView: AdView) { // 资源已经缓存完毕，还没有渲染出来
                listener?.onRenderSuccess(adView)
                TLog.i("adserror","onAdReady")
            }

            override fun onAdFailed(reason: String) {
                listener?.onError(reason)
                TLog.i("adserror",reason)
            }

            override fun onAdClick(info: JSONObject) {
                TLog.i("adserror","onAdClick")
            }

            override fun onAdClose(arg0: JSONObject) {
                TLog.i("adserror","onAdClose")
            }
        })

        val dm = DisplayMetrics()
        (activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            .getMetrics(dm)
        val winW = dm.widthPixels
        val winH = dm.heightPixels
        val width = Math.min(winW, winH)
        val height = width * scaleHeight / scaleWidth
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        val rllp = RelativeLayout.LayoutParams(width, height)
        rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        frameLayout.addView(adView, rllp)

    }

    fun destory(){
        //调用destroy()方法释放
        adView?.destroy()
    }


    interface TTBannerListener {

        //渲染成功
        fun onRenderSuccess(view: View?)

        //加载失败
        fun onError( message: String)

        //渲染失败
        fun onRenderFail()


    }
}