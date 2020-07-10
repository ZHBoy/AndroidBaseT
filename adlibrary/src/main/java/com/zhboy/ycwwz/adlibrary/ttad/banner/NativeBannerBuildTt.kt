package com.zhboy.ycwwz.adlibrary.ttad.banner

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.zhboy.ycwwz.adlibrary.ttad.TTAdManagerHolder
import java.util.*


/**
 * @author:  ZhouH
 * @date: 2019/11/14
 * @description:
 **/
class NativeBannerBuildTt(val activity: Activity?, val codeId: String, val width:Float, val height:Float, val listener: NativeTTBannerListener?) {

    private var mTTAdNative: TTAdNative? = null
    private var ttAdManager: TTAdManager? = null


    fun init() {
        if (activity != null) {
            ttAdManager = TTAdManagerHolder.get()
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            ttAdManager?.requestPermissionIfNecessary(activity)
            //step3:创建TTAdNative对象,用于调用广告请求接口
            mTTAdNative = ttAdManager?.createAdNative(activity.applicationContext)
            loadBannerAd()
        }
    }
    @SuppressWarnings("ALL", "SameParameterValue")
    private fun loadBannerAd() {
        //设置广告参数
        val adSlot = AdSlot.Builder()
            .setCodeId(codeId) //广告位id
            .setSupportDeepLink(true)
            .setImageAcceptedSize(600, 400) //期望个性化模板广告view的size,单位dp
            .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
            .setAdCount(1) //请求广告数量为1到3条
            .build()
        //加载广告
        mTTAdNative?.loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onNativeAdLoad(ads: MutableList<TTNativeAd>?) {
                if (ads?.get(0) == null ){
                    return
                }
                listener?.onRenderSuccess(ads)
            }

            override fun onError(code: Int, message: String) {
                listener?.onError(code, message)
            }

        })
    }
     fun setAdData(nativeView: View, nativeAd: TTNativeAd) {

        //可根据广告类型，为交互区域设置不同提示信息
        when (nativeAd.interactionType) {
            TTAdConstant.INTERACTION_TYPE_DOWNLOAD -> {
                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                nativeAd.setActivityForDownloadApp(activity!!)
                nativeAd.setDownloadListener(mDownloadListener) // 注册下载监听器
            }
            TTAdConstant.INTERACTION_TYPE_DIAL -> {

            }
            TTAdConstant.INTERACTION_TYPE_LANDING_PAGE, TTAdConstant.INTERACTION_TYPE_BROWSER -> {

            }
            else -> {

            }
        }

        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        val clickViewList = ArrayList<View>()
        clickViewList.add(nativeView)

        //触发创意广告的view（点击下载或拨打电话）
        val creativeViewList = ArrayList<View>()
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        //creativeViewList.add(nativeView);
        //creativeViewList.add(mCreativeButton)

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        nativeAd.registerViewForInteraction(
            nativeView as ViewGroup,
            clickViewList,
            creativeViewList,
            object : TTNativeAd.AdInteractionListener {
                override fun onAdClicked(view: View, ad: TTNativeAd?) {
                    if (ad != null) {

                    }
                }

                override fun onAdCreativeClick(view: View, ad: TTNativeAd?) {
                    if (ad != null) {

                    }
                }

                override fun onAdShow(ad: TTNativeAd?) {
                    if (ad != null) {

                    }
                }
            })

    }

    //接入网盟的dislike 逻辑，有助于提示广告精准投放度
    private fun bindDislikeAction(ad: TTNativeAd, dislikeView: View) {
        val ttAdDislike = ad.getDislikeDialog(activity!!)
        if (ttAdDislike != null) {
            ttAdDislike!!.setDislikeInteractionCallback(object : TTAdDislike.DislikeInteractionCallback {
                override fun onRefuse() {

                }

                override fun onSelected(position: Int, value: String) {
                    listener?.onSelected(position,value)
                }

                override fun onCancel() {

                }
            })
        }
        dislikeView.setOnClickListener {
            if (ttAdDislike != null)
                ttAdDislike!!.showDislikeDialog()
        }
    }

    private val mDownloadListener = object : TTAppDownloadListener {
        override fun onIdle() {

        }

        @SuppressLint("SetTextI18n")
        override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {

        }

        @SuppressLint("SetTextI18n")
        override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {

        }

        override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {

        }

        override fun onInstalled(fileName: String, appName: String) {

        }

        override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {

        }
    }
    interface NativeTTBannerListener {

        //渲染成功
        fun onRenderSuccess(ads:MutableList<TTNativeAd>)

        //加载失败
        fun onError(code: Int, message: String)

        //渲染失败
        fun onRenderFail()
        //用户点击了关闭按钮中的确定
        fun onSelected(position: Int, value: String)
    }
}