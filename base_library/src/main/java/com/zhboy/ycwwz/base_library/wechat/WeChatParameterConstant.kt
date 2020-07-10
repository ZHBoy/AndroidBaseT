package com.zhboy.ycwwz.base_library.wechat

import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
import com.zhboy.ycwwz.base_library.BaseApplication

/**
 * 作者：changzhiyuan
 * 日期：2019-11-29 16:00
 * 版本：v 1.0
 * 描述：
 */
class WeChatParameterConstant {
    companion object {
        // APP_ID_WECHAT 替换为你的应用从 微信开放平台 申请到的合法的 appID
        const val APP_ID_WECHAT = "wx4c4d9db22baea27e"
        //微信小程序ID
        const val WX_MINI_ID = "gh_a646e7b5f8b6"

        // 分享微信小程序 视频详情
        const val SHARE_MINI_PROGRAM_WECHAT_VIDEO_DETAIL = "SHARE_MINI_PROGRAM_WECHAT_VIDEO_DETAIL"
        // 微信分享文本
        const val SHARE_TEXT_WECHAT = "SHARE_TEXT_WECHAT"
        // 微信分享图片
        const val SHARE_IMAGE_WECHAT = "SHARE_IMAGE_WECHAT"
        // 微信分享音乐
        const val SHARE_MUSIC_WECHAT = "SHARE_MUSIC_WECHAT"
        // 微信分享视频
        const val SHARE_VIDEO_WECHAT = "SHARE_VIDEO_WECHAT"
        // 微信分享网页
        const val SHARE_WEBPAGE_WECHAT = "SHARE_WEBPAGE_WECHAT"
        // 微信小程序
        const val SHARE_MINI_PROGRAM_WECHAT = "SHARE_MINI_PROGRAM_WECHAT"


        // 微信小程序 MINIPTOGRAM_TYPE_RELEASE正式 MINIPROGRAM_TYPE_TEST测试 MINIPROGRAM_TYPE_PREVIEW体验版
        val MINI_PROGRAM_TYPE_WECHAT =
            if (BaseApplication.debugging) MINIPROGRAM_TYPE_PREVIEW else MINIPTOGRAM_TYPE_RELEASE

        // 微信分享图片缩略图的大小
        const val THUMB_SIZE_SHARE_IMAGE = 150

        // 音频网页的URL地址
        const val MUSIC_URL_TYPE = 0
        // 供低带宽环境下使用的音频网页URL地址
        const val MUSIC_LOW_BAND_URL_TYPE = 1
        // 音频数据的URL地址
        const val MUSIC_DATA_URL_TYPE = 2
        // 供低带宽环境下使用的音频数据URL地址
        const val MUSIC_LOW_BAND_DATA_URL_TYPE = 3

        // 视频链接
        const val VIDEO_URL_TYPE = 0
        // 供低带宽的环境下使用的视频链接
        const val VIDEO_LOW_BANDURL_TYPE = 1

        // 授权登录
        const val AUTH_LOGIN_WECHAT_STATE = "AUTH_LOGIN_WECHAT_STATE"

        // 分享 任务（示例：分享回调）
        const val TRANSACTION_SHARE_WEATHER_TASK = "TRANSACTION_SHARE_WEATHER_TASK"

    }
}