package com.zhboy.ycwwz.base_library.wechat

import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zhboy.ycwwz.base_library.BaseApplication.Companion.appContext

class WXAPISingleton {
    companion object {
        val WXAPI_INSTANCE: IWXAPI by lazy {
            val iwxapi = WXAPIFactory.createWXAPI(
                appContext,
                WeChatParameterConstant.APP_ID_WECHAT, false
            )
            iwxapi.registerApp(WeChatParameterConstant.APP_ID_WECHAT)
            iwxapi
        }
    }
}