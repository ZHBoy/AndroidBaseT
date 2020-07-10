package com.zhboy.ycwwz.adlibrary.gdt.information

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize
import com.qq.e.ads.nativ.NativeExpressAD
import com.qq.e.ads.nativ.NativeExpressADView
import com.qq.e.ads.nativ.NativeExpressMediaListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.zhboy.ycwwz.base_library.utils.BitmapUtils
import com.zhboy.ycwwz.base_library.utils.DisplayUtil
import com.zhboy.ycwwz.base_library.utils.TLog


class InformationFlowBuildGDT(
    val activity: Activity,
    val codeId: String,
    val parentView: RelativeLayout,
    val listener: InformationFlowListener
) {

    private var nativeExpressAD: NativeExpressAD? = null
    private var nativeExpressADView: NativeExpressADView? = null

    private var TAG = "InformationFlowBuildGDT"


    fun init() {
        if (activity != null) {
            loadInformationAd()
        }
    }

    // 1.加载广告，先设置加载上下文环境和条件
    private fun loadInformationAd() {
        nativeExpressAD = NativeExpressAD(
            activity,
            ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT),
            codeId,
            object : NativeExpressAD.NativeExpressADListener {
                override fun onADCloseOverlay(p0: NativeExpressADView?) {

                }

                override fun onADLoaded(adList: MutableList<NativeExpressADView>?) {
                    TLog.i(TAG, "onADLoaded: " + adList?.size)
                    // 释放前一个 NativeExpressADView 的资源
                    if (nativeExpressADView != null) {
                        nativeExpressADView?.destroy()
                    }
                    parentView.removeAllViews()
                    // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                    if (adList?.size!! > 0) {
                        nativeExpressADView = adList[0]
                        if (nativeExpressADView?.boundData?.adPatternType === AdPatternType.NATIVE_VIDEO) {
                            nativeExpressADView?.setMediaListener(mediaListener)
                        }
                        val margin = DisplayUtil.dip2px(activity, 40f)
                        val w = DisplayUtil.getScreenWidth(activity) - margin
                        val scale = w / DisplayUtil.getScreenWidth(activity).toFloat()
                        parentView.post {
                            nativeExpressADView?.scaleX = 0.98f
                            nativeExpressADView?.scaleY = 0.98f
                            BitmapUtils.instance.clipViewCornerByDp(nativeExpressADView!!, 20f)
                            nativeExpressADView?.render()

                            parentView.addView(nativeExpressADView)

                            parentView.scaleX = scale
                            parentView.scaleY = scale
                        }
                    }
                }

                override fun onADOpenOverlay(p0: NativeExpressADView?) {

                }

                override fun onRenderFail(p0: NativeExpressADView?) {
                    listener?.onRenderFail()
                }

                override fun onADExposure(p0: NativeExpressADView?) {

                }

                override fun onADClosed(p0: NativeExpressADView?) {

                }

                override fun onADLeftApplication(p0: NativeExpressADView?) {

                }

                override fun onNoAD(error: AdError?) {
                    TLog.i(TAG, "onNoAD: " + error?.errorCode!! + error?.errorMsg!!)
                    listener?.onError(error?.errorCode!!, error?.errorMsg!!)

                }

                override fun onADClicked(p0: NativeExpressADView?) {
                    listener?.onAdClicked()
                }

                override fun onRenderSuccess(p0: NativeExpressADView?) {
                    listener?.onRenderSuccess(p0)
                }

            }
        ) // 传入Activity
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD?.setVideoOption(
            VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()
        ) //
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br></br>
         * 如果广告位仅支持图文广告，则无需调用
         */
        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略
         *
         *
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br></br>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        nativeExpressAD?.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // 本次拉回的视频广告，从用户的角度看是自动播放的
        nativeExpressAD?.loadAD(1)
    }


    private val mediaListener: NativeExpressMediaListener = object : NativeExpressMediaListener {
        override fun onVideoInit(nativeExpressADView: NativeExpressADView) {

        }

        override fun onVideoLoading(nativeExpressADView: NativeExpressADView) {
            TLog.i(TAG, "onVideoLoading")
        }

        override fun onVideoReady(
            nativeExpressADView: NativeExpressADView,
            l: Long
        ) {
            TLog.i(TAG, "onVideoReady")
        }

        override fun onVideoCached(p0: NativeExpressADView?) {

        }

        override fun onVideoStart(nativeExpressADView: NativeExpressADView) {

        }

        override fun onVideoError(p0: NativeExpressADView?, p1: AdError?) {

        }

        override fun onVideoPause(nativeExpressADView: NativeExpressADView) {

        }

        override fun onVideoComplete(nativeExpressADView: NativeExpressADView) {

        }


        override fun onVideoPageOpen(nativeExpressADView: NativeExpressADView) {
            TLog.i(TAG, "onVideoPageOpen")
        }

        override fun onVideoPageClose(nativeExpressADView: NativeExpressADView) {
            TLog.i(TAG, "onVideoPageClose")
        }
    }

    interface InformationFlowListener {
        //渲染成功
        fun onRenderSuccess(view: View?)

        //加载失败
        fun onError(code: Int, message: String)

        //渲染失败
        fun onRenderFail()

        //广告点击事件
        fun onAdClicked()

    }

    public fun onDestroy() {
        // 4.使用完了每一个 NativeExpressADView 之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView?.destroy()
        }
    }


}


