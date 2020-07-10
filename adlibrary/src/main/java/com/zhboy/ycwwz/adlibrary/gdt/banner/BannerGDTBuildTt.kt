package com.zhboy.ycwwz.adlibrary.gdt.banner

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError
import com.zhboy.ycwwz.base_library.utils.TLog


/**
 * @author:  YMH
 * @date: 2020/03/25
 * @description:
 **/
class BannerGDTBuildTt(val activity: Activity?, val bannerContainer: ViewGroup, val posId:String, val scaleWidth:Int, val scaleHeight:Int, val listener: GDTBannerListener?) {

    private var bv:UnifiedBannerView?=null
    fun init() {
        if (activity != null) {
           getBanner()?.loadAD()
        }
    }

    /**
     * 创建横幅广告的View，并添加至接界面布局中
     * 注意：只有将AdView添加到布局中后，才会有广告返回
     */
    private fun getBanner(): UnifiedBannerView? {
        if (this.bv != null) {
            return this.bv
        }
        if (this.bv != null) {
            bannerContainer?.removeView(bv)
            bv?.destroy()
        }
        this.bv = UnifiedBannerView(activity,  posId, object:
            UnifiedBannerADListener {
            override fun onADCloseOverlay() {

            }

            override fun onADExposure() {

            }

            override fun onADClosed() {
                listener?.onADClosed()
            }

            override fun onADLeftApplication() {

            }

            override fun onADOpenOverlay() {

            }

            override fun onNoAD(adError: AdError) {
                TLog.i("splashBuidGDT", adError.getErrorMsg() + adError.getErrorCode())
                listener?.onError(adError?.errorMsg!!)
            }

            override fun onADReceive() {
                listener?.onRenderSuccess()
            }

            override fun onADClicked() {

            }

        })
        bannerContainer?.addView(bv, getUnifiedBannerLayoutParams())
        return this.bv
    }

    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private fun getUnifiedBannerLayoutParams(): FrameLayout.LayoutParams? {
        return FrameLayout.LayoutParams(scaleWidth, scaleHeight)
    }

    fun destory(){
        bannerContainer?.removeAllViews();
        if (bv != null) {
            bv?.destroy();
            bv = null;
        }
    }


    public fun loadAd(){
        getBanner()?.loadAD()
    }

    interface GDTBannerListener {

        //渲染成功
        fun onRenderSuccess()

        //加载失败
        fun onError( message: String)


        fun onADClosed()


    }
}