package com.zhboy.ycwwz.adlibrary.ttad.banner

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.zhboy.ycwwz.adlibrary.ttad.TTAdManagerHolder


/**
 * @author:  ZhouH
 * @date: 2019/11/14
 * @description:
 **/
class InteractionExpBuildTt(val activity: Activity?, val codeId: String, val width:Float, val height:Float, val listener: TTBannerListener?) {

    private var mTTAdNative: TTAdNative? = null
    private var ttAdManager: TTAdManager? = null
    private var mTTAd: TTNativeExpressAd? = null


    fun init() {
        if (activity != null) {
            ttAdManager = TTAdManagerHolder.get()
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            ttAdManager?.requestPermissionIfNecessary(activity)
            //step3:创建TTAdNative对象,用于调用广告请求接口
            mTTAdNative = ttAdManager?.createAdNative(activity.applicationContext)
            loadExpressAd()
        }
    }

    private fun loadExpressAd() {
        //设置广告参数
        val adSlot = AdSlot.Builder()
            .setCodeId(codeId) //广告位id
            .setSupportDeepLink(true)
            .setAdCount(1) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(width, height) //期望个性化模板广告view的size,单位dp
            .setImageAcceptedSize(width.toInt(), height.toInt())//这个参数设置即可，不影响个性化模板广告的size
            .build()
        //加载广告
        mTTAdNative?.loadInteractionExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, message: String) {
//                mExpressContainer.removeAllViews()
                listener?.onError(code, message)
            }

            override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>?) {
                if (ads == null || ads.isEmpty()) {
                    return
                }
                mTTAd = ads[0]
              // mTTAd?.setSlideIntervalTime(30 * 1000)//设置轮播间隔 ms,不调用则不进行轮播展示
                bindAdListener(mTTAd!!)
                mTTAd?.render()//调用render开始渲染广告
            }
        })
    }

    //绑定广告行为
    private fun bindAdListener(ad: TTNativeExpressAd) {
        ad.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                //广告被点击
            }

            override fun onAdShow(view: View?, type: Int) {
                //广告展示
            }

            //返回view的宽高 单位 dp
            override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                //在渲染成功回调时展示广告，提升体验
//                mExpressContainer.removeAllViews()
//                mExpressContainer.addView(view)
                mTTAd?.showInteractionExpressAd(activity)
                listener?.onRenderSuccess(view)
            }

            override fun onRenderFail(view: View?, msg: String?, code: Int) {
                //渲染失败
                listener?.onRenderFail()
            }

        })

        if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return
        }
        //可选，下载监听设置
        ad.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {
//                点击开始下载
            }

            override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
//               下载中，点击暂停
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
//               下载暂停，点击继续
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
//                下载失败，点击重新下载
            }

            override fun onInstalled(fileName: String, appName: String) {
//                安装完成，点击图片打开
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {
//                点击安装
            }
        })
    }


    fun destoryBannerTt() {
        //调用destroy()方法释放
        mTTAd?.destroy()
    }

    interface TTBannerListener {

        //渲染成功
        fun onRenderSuccess(view: View?)

        //加载失败
        fun onError(code: Int, message: String)

        //渲染失败
        fun onRenderFail()

        //用户点击了关闭按钮中的确定
        fun onSelected(position: Int, value: String)

    }
}