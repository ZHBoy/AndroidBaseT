package com.ychd.ycwwz.wxapi

import android.os.Bundle
import android.widget.Toast
import com.tencent.mm.opensdk.constants.ConstantsAPI.*
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.event.login.LoginEvent
import com.ychd.ycwwz.base_library.wechat.WXAPISingleton
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant
import org.greenrobot.eventbus.EventBus

/**
 * 描述：WeChat SDK。接收微信发送的请求，或者接收发送到微信请求的响应结果
 * 注意：注意做好版本兼容逻辑，如果 WXEntryActivity 实现为透明 Activity，最好在 onReq 和 onResp 处理完成后
 * finish 掉当前界面，避免收到不支持的事件的时候，透明 Activity 无法关闭。
 */
class WXEntryActivity : RxAppCompatActivity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 如果没回调 onResp，可能是没有在 WXEntryActivity 中将接收到的 intent 及实现了 IWXAPIEventHandler
        // 接口的对象传递给IWXAPI接口的handleIntent方法
        WXAPISingleton.WXAPI_INSTANCE.handleIntent(intent, this)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {}

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    // resp.errCode ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
    override fun onResp(resp: BaseResp) {
        when (resp.errCode) {
            // 失败回调
            BaseResp.ErrCode.ERR_AUTH_DENIED,
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                if (COMMAND_SENDAUTH == resp.type) {// 登陆
                    Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show()
                }
                if (COMMAND_SENDMESSAGE_TO_WX == resp.type) {// 分享
                    Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
            // 成功回调
            BaseResp.ErrCode.ERR_OK -> {
                when (resp.type) {
                    COMMAND_SENDAUTH -> {// 登录
                        // 拿到了微信返回的code,立马再去请求access_token。note 注意 finish()
                        val code = (resp as SendAuth.Resp).code
                        EventBus.getDefault().post(LoginEvent(code))
                        finish()

                    }
                    COMMAND_SENDMESSAGE_TO_WX -> {// 分享
                        // note 注意 finish()
                        finish()
                        when(resp.transaction){
                            // 分享天气任务
                            WeChatParameterConstant.TRANSACTION_SHARE_WEATHER_TASK->{
//                                EventBus.getDefault().post(ShareWeatherTaskEvent())//通知
                            }
                        }
                    }
                    COMMAND_LAUNCH_WX_MINIPROGRAM -> {// 打开小程序 点击小程序的 相应按钮才回调 关闭小程序不会回调
                        // note 注意 finish()

                        val launchMiniProResp = resp as WXLaunchMiniProgram.Resp
                        //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
                        val extraData = launchMiniProResp.extMsg

                    }
                }
            }
        }
    }
}

