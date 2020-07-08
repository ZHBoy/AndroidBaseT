package com.ychd.ycwwz.adlibrary.ttad.newexpress

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdManager
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ychd.ycwwz.adlibrary.ttad.TTAdManagerHolder
import com.ychd.ycwwz.base_library.utils.DisplayUtil
import com.ychd.ycwwz.base_library.utils.TLog

/**
 * @author:  ZhouH
 * @date: 2019/12/20
 * @description:资讯信息流广告
 **/
class NewExpressAdTt(val activity: Activity?, val codeId: String, val listener: NewExpressAdTtListener?) {

    private var mTTAdNative: TTAdNative? = null
    private var mTTNativeExpressAd: TTNativeExpressAd? = null
    private var ttAdManager: TTAdManager? = null

    fun init() {
        if (activity != null) {
            ttAdManager = TTAdManagerHolder.get()
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            ttAdManager?.requestPermissionIfNecessary(activity)
            //step3:创建TTAdNative对象,用于调用广告请求接口
            mTTAdNative = ttAdManager?.createAdNative(activity.applicationContext)
        }
    }



    /**
     * 加载feed广告
     */
     fun loadNesListAd() {

        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
            .setCodeId(codeId)
            .setSupportDeepLink(true)
//            .setImageAcceptedSize(640, 320)
            .setExpressViewAcceptedSize(DisplayUtil.getScreenWidth(activity!!).toFloat(), 0f) //期望模板广告view的size,单位dp
            .setAdCount(1) //请求广告数量为1到3条
            .build()
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative?.loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, message: String) {
                listener?.onError(code, message)
            }

            override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>?) {

                if (ads?.isNotEmpty() == true) {
                    mTTNativeExpressAd = ads[0]
                    bindAdListener(ads[0])
                }
            }
        })
    }

    /**
     *拿到广告设置监听
     */
    private fun bindAdListener(ad: TTNativeExpressAd) {

        ad.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View, type: Int) {
//                    TToast.show(this@NativeExpressListActivity, "广告被点击")
                TLog.i("NewExpressAdTt  广告被点击")
                listener?.onAdClicked()
            }

            override fun onAdShow(view: View, type: Int) {
//                    TToast.show(this@NativeExpressListActivity, "广告展示")
                TLog.i("NewExpressAdTt  广告展示")
                listener?.onAdShow(view)
            }

            override fun onRenderFail(view: View, msg: String, code: Int) {
//                    TToast.show(this@NativeExpressListActivity, "$msg code:$code")
                TLog.i("NewExpressAdTt  onRenderFail")
                listener?.onRenderFail()
            }

            override fun onRenderSuccess(view: View, width: Float, height: Float) {
                //返回view的宽高 单位 dp
//                    TToast.show(this@NativeExpressListActivity, "渲染成功")
                TLog.i("NewExpressAdTt  渲染成功")
                listener?.onRenderSuccess(view)
            }
        })
        ad.render()
    }

    fun destroy() {
        mTTNativeExpressAd?.destroy()
        mTTAdNative = null
        ttAdManager = null
    }

    /**
     * 事件的监听
     */
    interface NewExpressAdTtListener {
        //渲染成功
        fun onRenderSuccess(view: View?)

        //加载失败
        fun onError(code: Int, message: String)

        //渲染失败
        fun onRenderFail()

        //广告点击事件
        fun onAdClicked()

        //广告展示
        fun onAdShow(view: View?)


    }
}