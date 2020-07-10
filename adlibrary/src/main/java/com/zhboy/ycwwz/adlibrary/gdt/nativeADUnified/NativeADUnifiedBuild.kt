package com.zhboy.ycwwz.adlibrary.gdt.nativeADUnified

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.*
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.zhboy.ycwwz.base_library.utils.TLog


class NativeADUnifiedBuild (val activity: Activity,val postId:String,val adCount:Int,val mContainer: NativeAdContainer,val mMediaView: MediaView,val clickTop:RelativeLayout,val clickBottom: RelativeLayout,val vClick:View,val mDownloadButton:Button,val mCTAButton:Button,val listener: GDTNativeADUnifiedListener): NativeADUnifiedListener {

    private var mAdData: NativeUnifiedADData? = null
    private var mAdManager: NativeUnifiedAD? = null
    private val TAG="NativeADUnifiedBuild"
    init {
        if (activity != null) {

            mAdManager = NativeUnifiedAD(activity,  postId, this)
            /**
             * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前，调用下面两个方法，有助于提高视频广告的eCPM值 <br/>
             * 如果广告位仅支持图文广告，则无需调用
             */

            /**
             * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
             *
             * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
             *
             * 例如开发者设置了VideoOption.AutoPlayPolicy.NEVER，表示从不自动播放 <br/>
             * 但满足某种条件(如晚上10点)时，开发者调用了startVideo播放视频，这在用户看来仍然是自动播放的
             */
            mAdManager?.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
            /**
             * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前，调用下面两个方法，有助于提高视频广告的eCPM值 <br></br>
             * 如果广告位仅支持图文广告，则无需调用
             */
            /**
             * 设置本次拉取的视频广告，从用户角度看到的视频播放策略
             *
             *
             *
             * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br></br>
             *
             * 例如开发者设置了VideoOption.AutoPlayPolicy.NEVER，表示从不自动播放 <br></br>
             * 但满足某种条件(如晚上10点)时，开发者调用了startVideo播放视频，这在用户看来仍然是自动播放的
             */


            /**
             * 该接口已经废弃，仅支持sdk渲染，不再支持开发者自己渲染 <p/>
             * 当前仅支持VideoADContainerRender.SDK VideoADContainerRender.DEV不再支持
             */
            mAdManager?.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK); // 视频播放前，用户看到的广告容器是由SDK渲染的

            mAdManager?.loadData(adCount);

        }
    }


    override fun onADLoaded(ads: MutableList<NativeUnifiedADData>?) {
        if (ads != null && ads.size > 0) {
            listener.onADLoaded(ads[0])
        }
    }


    fun initAd( ad:NativeUnifiedADData){

        val clickableViews: MutableList<View> =  java.util.ArrayList()
        clickableViews.add(mContainer)
        clickableViews.add(clickTop)
        clickableViews.add(clickBottom)
      //  clickableViews.add(mDownloadButton)
        clickableViews.add(mCTAButton)
        vClick.setOnClickListener{
            clickTop.performClick()
        }
        // 将布局与广告进行绑定
        // 将布局与广告进行绑定
        ad.bindAdToView(activity, mContainer, null, clickableViews)
        // 设置广告事件监听
        // 设置广告事件监听
        ad.setNativeAdEventListener(object : NativeADEventListener {
            override fun onADExposed() {
                TLog.i(TAG, "广告曝光")
            }

            override fun onADClicked() {
                TLog.i(TAG, "广告被点击")
            }

            override fun onADError(error: AdError) {
                TLog.i(TAG, "错误回调 error code :" + error.errorCode
                        + "  error msg: " + error.errorMsg
                )
            }

            override fun onADStatusChanged() {
                TLog.i(TAG, "广告状态变化")
                updateAdAction(ad)
            }
        })
        updateAdAction(ad)

        /**
         * 营销组件
         * 支持项目：智能电话（点击跳转拨号盘），外显表单
         * bindCTAViews 绑定营销组件监听视图，注意：bindCTAViews的视图不可调用setOnClickListener，否则SDK功能可能受到影响
         * ad.getCTAText 判断拉取广告是否包含营销组件，如果包含组件，展示组件按钮，否则展示download按钮
         */
        val CTAViews: MutableList<View> =
            java.util.ArrayList()
        CTAViews.add(mContainer)
        ad.bindCTAViews(CTAViews)
        val ctaText = ad.ctaText //获取组件文案

        if (!TextUtils.isEmpty(ctaText)) { //如果拉取广告包含CTA组件，则渲染该组件
        //当广告中有营销组件时，隐藏下载按钮，仅为demo示例所用，开发者可自行决定mDownloadButton按钮是否显示
            mCTAButton.text=ctaText
            mCTAButton.visibility=View.GONE
            mDownloadButton.visibility = View.GONE
        }
        if (ad.adPatternType == AdPatternType.NATIVE_VIDEO) {
            listener?.onStartVideo()
            // 视频广告需对MediaView进行绑定，MediaView必须为容器mContainer的子View
            ad.bindMediaView(mMediaView, VideoOption.Builder()
                .setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI).build(),  // 视频相关回调
                object : NativeADMediaListener {
                    override fun onVideoInit() {
                        TLog.i(TAG, "onVideoInit: ")
                    }

                    override fun onVideoStop() {

                    }

                    override fun onVideoLoading() {
                        TLog.i(TAG, "onVideoLoading: ")
                    }

                    override fun onVideoReady() {
                        TLog.i(TAG, "onVideoReady: ")
                    }

                    override fun onVideoLoaded(videoDuration: Int) {
                        TLog.i(TAG,"onVideoLoaded: ")
                    }

                    override fun onVideoClicked() {

                    }

                    override fun onVideoStart() {
                        TLog.i(TAG, "onVideoStart: ")
                    }

                    override fun onVideoPause() {
                        TLog.i(TAG,"onVideoPause: ")
                    }

                    override fun onVideoResume() {
                        TLog.i(TAG, "onVideoResume: ")
                    }

                    override fun onVideoCompleted() {
                        TLog.i(TAG, "onVideoCompleted: ")
                    }

                    override fun onVideoError(error: AdError) {
                        TLog.i(TAG, "onVideoError: ")
                    }
                })
        }
    }


    override fun onNoAD(p0: AdError?) {
        listener.onNoAD(p0?.errorCode.toString(),p0?.errorMsg!!)
    }

    // 获取广告资源并加载到UI

   private fun updateAdAction(ad: NativeUnifiedADData) {
        if (!ad.isAppAd) {
            mDownloadButton.text = "浏览"
            return
        }
        when (ad.appStatus) {
            0 -> mDownloadButton.text = "下载"
            1 -> mDownloadButton.text = "启动"
            2 -> mDownloadButton.text = "更新"
            4 -> mDownloadButton.text = ad.progress.toString() + "%"
            8 -> mDownloadButton.text = "安装"
            16 -> mDownloadButton.text = "下载失败，重新下载"
            else -> mDownloadButton.text = "浏览"
        }
    }

    fun resume(){
        if (mAdData != null) {
            // 必须要在Actiivty.onResume()时通知到广告数据，以便重置广告恢复状态
            mAdData?.resume();
        }
    }

    fun destroy(){
        if (mAdData != null) {
            // 必须要在Actiivty.destroy()时通知到广告数据，以便释放内存
            mAdData?.destroy();
        }
    }

    interface GDTNativeADUnifiedListener{
        fun onADLoaded(p0: NativeUnifiedADData?)
        fun onNoAD(errorCode:String ,errorMessage:String)
        fun onStartVideo()
    }
}