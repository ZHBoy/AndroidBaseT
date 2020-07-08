package com.ychd.ycwwz.base_library.wechat

import android.graphics.Bitmap
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.MUSIC_DATA_URL_TYPE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.MUSIC_LOW_BAND_DATA_URL_TYPE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.MUSIC_LOW_BAND_URL_TYPE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.MUSIC_URL_TYPE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_IMAGE_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_MUSIC_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_TEXT_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_VIDEO_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.VIDEO_URL_TYPE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.AUTH_LOGIN_WECHAT_STATE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.MINI_PROGRAM_TYPE_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_MINI_PROGRAM_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.SHARE_WEBPAGE_WECHAT
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.THUMB_SIZE_SHARE_IMAGE
import com.ychd.ycwwz.base_library.wechat.WeChatParameterConstant.Companion.WX_MINI_ID
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.*
import java.io.File
import androidx.annotation.IntRange
import com.ychd.ycwwz.base_library.utils.BitmapUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 作者：changzhiyuan
 * 日期：2019/7/21 18:35
 * 版本：
 * 描述：微信
 * scene 发送的目标场景：
 *       1.分享到对话 --> SendMessageToWX.Req.WXSceneSession
 *       2.分享到朋友圈 --> SendMessageToWX.Req.WXSceneTimeline
 *       3.分享到收藏 --> SendMessageToWX.Req.WXSceneFavorite
 *
 * transaction 对应该请求的事务ID，通常由Req发起，回复Resp时应填入对应事务ID
 *
 */

class WeChatUtils {
    companion object {
        private const val THUMB_BITMAP_SIZE = 32 * 1024L
        /**
         * 构建 SendMessageToWX.Req
         */
        private fun buildReq(
            transaction: String,
            msg: WXMediaMessage,
            scene: Int = SendMessageToWX.Req.WXSceneSession
        )
                : SendMessageToWX.Req {
            val req = SendMessageToWX.Req()
            req.transaction = transaction
            req.message = msg
            req.scene = scene
            return req
        }

        /**
         * 微信分享文字
         * 长度需大于 0 且不超过 10KB
         */
        fun shareTextWeChat(
            text: String, scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_TEXT_WECHAT
        ) {
            try {
                //初始化一个 WXTextObject 对象，填写分享的文本内容
                val textObj = WXTextObject()
                if (text.isEmpty()) {
                    // todo ToastUtil.showCustomToast("文本内容为空")
                    return
                }
                textObj.text = text

                //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
                val msg = WXMediaMessage()
                msg.mediaObject = textObj

                val req = buildReq(transaction, msg, scene)
                //调用api接口，发送数据到微信
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }


        /**
         * 微信分享图片
         * bmp 内容大小不超过 10MB
         * thumbData 限制内容大小不超过 32KB
         */
        fun shareImageWeChat(
            bmp: Bitmap, targetSize: Int = 10 * 1024 * 1024, thumbBmp: Bitmap? = null,
            scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_IMAGE_WECHAT
        ) {
            try {
                CoroutineScope(Dispatchers.Main).launch {
                    // 初始化 WXImageObject 和 WXMediaMessage 对象
                    var bitmap: Bitmap = bmp
                    withContext(Dispatchers.IO) {
                        bitmap = BitmapUtils.instance.compressImage(bmp, targetSize)
                    }
                    val imgObj = WXImageObject(bitmap)
                    val msg = WXMediaMessage()
                    msg.mediaObject = imgObj
                    thumbBmp?.let {
                        msg.thumbData = BitmapUtils.instance.bitmapToByteArray4AssignSize(
                            thumbBmp,
                            THUMB_BITMAP_SIZE,
                            true
                        )
                    }
                    //构造一个Req
                    val req = buildReq(transaction, msg, scene)
                    //调用api接口，发送数据到微信
                    WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }


        /**
         * 微信分享图片 path
         * imagePath 对应图片内容大小不超过 10MB
         * thumbData 限制内容大小不超过 32KB
         */
        fun shareImageWeChat(
            imagePath: String, thumbBmp: Bitmap, scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_IMAGE_WECHAT
        ) {
            try {
                val file = File(imagePath)
                if (!file.exists()) {
                    // todo ToastUtil.showCustomToast("图片不存在")
                    return
                }

                // 初始化 WXImageObject 和 WXMediaMessage 对象
                val imgObj = WXImageObject()
                imgObj.imagePath = imagePath

                val msg = WXMediaMessage()
                msg.mediaObject = imgObj
                msg.thumbData = BitmapUtils.instance.bitmapToByteArray4AssignSize(
                    thumbBmp,
                    THUMB_BITMAP_SIZE,
                    true
                )

                //构造一个Req
                val req = buildReq(transaction, msg, scene)
                //调用api接口，发送数据到微信
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }

        /**
         * 微信分享音乐
         * thumbData 限制内容大小不超过 32KB
         * musicUrl、musicLowBandUrl、musicDataUrl、musicLowBandDataUrl
         */
        fun shareMusicWeChat(
            musicUrl: String, title: String, description: String, thumbBmp: Bitmap,
            scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_MUSIC_WECHAT,
            @IntRange(from = 0, to = 3)
            urlType: Int = 0
        ) {
            try {
                val music = WXMusicObject()
                when (urlType) {
                    MUSIC_URL_TYPE -> music.musicUrl = musicUrl
                    MUSIC_LOW_BAND_URL_TYPE -> music.musicLowBandUrl = musicUrl
                    MUSIC_DATA_URL_TYPE -> music.musicDataUrl = musicUrl
                    MUSIC_LOW_BAND_DATA_URL_TYPE -> music.musicLowBandDataUrl = musicUrl
                }
                val msg = WXMediaMessage()
                msg.mediaObject = music
                msg.title = title
                msg.description = description
                msg.thumbData = BitmapUtils.instance.bitmapToByteArray4AssignSize(
                    thumbBmp,
                    THUMB_BITMAP_SIZE,
                    true
                )

                val req = buildReq(transaction, msg, scene)
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }


        /**
         * 微信分享视频
         */
        fun shareVideoWeChat(
            videoUrl: String, title: String, description: String, thumbBmp: Bitmap,
            @IntRange(
                from = 0,
                to = 1
            ) urlType: Int = 0,
            scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_VIDEO_WECHAT
        ) {
            try {
                val video = WXVideoObject()
                if (urlType == VIDEO_URL_TYPE) {
                    video.videoUrl = videoUrl
                } else {
                    video.videoLowBandUrl = videoUrl
                }

                val msg = WXMediaMessage(video)
                msg.mediaTagName
                msg.messageAction
                msg.title = title
                msg.description = description

                if (urlType == VIDEO_URL_TYPE) {
                    msg.thumbData = BitmapUtils.instance.bitmapToByteArray4AssignSize(
                        thumbBmp,
                        THUMB_BITMAP_SIZE,
                        true
                    )
                }

                val req = buildReq(transaction, msg, scene)
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }

        /**
         * 微信分享视频
         */
        fun shareVideoWeChat(
            path: String, title: String, description: String,
            scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_VIDEO_WECHAT
        ) {
            try {
                val gameVideoFileObject = WXGameVideoFileObject()
                gameVideoFileObject.filePath = path

                val msg = WXMediaMessage()
                msg.setThumbImage(
                    BitmapUtils.instance.extractThumbNail(
                        path,
                        THUMB_SIZE_SHARE_IMAGE,
                        THUMB_SIZE_SHARE_IMAGE,
                        true
                    )
                )
                msg.title = title
                msg.description = description
                msg.mediaObject = gameVideoFileObject

                val req = buildReq(transaction, msg, scene)
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }

        /**
         * 微信分享网页
         */
        fun shareWebpageWeChat(
            webpageUrl: String, title: String, description: String, thumbBmp: Bitmap,
            scene: Int = SendMessageToWX.Req.WXSceneSession,
            transaction: String = SHARE_WEBPAGE_WECHAT
        ) {
            try {
                val webpage = WXWebpageObject()
                webpage.webpageUrl = webpageUrl

                val msg = WXMediaMessage(webpage)
                msg.title = title
                msg.description = description
                msg.thumbData =
                    BitmapUtils.instance.bitmapToByteArray4AssignSize(thumbBmp, 120 * 1024L, true)

                val req = buildReq(transaction, msg, scene)
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }


        /**
         * 微信分享小程序
         */
        fun shareMiniProgramWeChat(
            path: String = "",
            thumbBmp: Bitmap? = null,
            webpageUrl: String = "http://xy.bestxyin.com",
            title: String = "",
            description: String = "",
            id: String = WX_MINI_ID,
            transaction: String = SHARE_MINI_PROGRAM_WECHAT,
            @IntRange(
                from = 0,
                to = 2
            )
            miniprogramType: Int = MINI_PROGRAM_TYPE_WECHAT
        ) {
            try {
                val miniProgramObj = WXMiniProgramObject()
                // 兼容低版本的网页链接
                miniProgramObj.webpageUrl = webpageUrl
                // 正式版:0，测试版:1，体验版:2
                miniProgramObj.miniprogramType = miniprogramType
                // 小程序原始id
                miniProgramObj.userName = id
                //小程序页面路径 拉起小程序页面的可带参路径，不填默认拉起小程序首页
                miniProgramObj.path = path
                val msg = WXMediaMessage(miniProgramObj)
                msg.title = title
                msg.description = description
                // 小程序消息封面图片，小于128k
                thumbBmp?.let {
                    msg.thumbData = BitmapUtils.instance.bitmapToByteArray4AssignSize(
                        thumbBmp,
                        120 * 1024L,
                        true
                    )
                }

                val req = SendMessageToWX.Req()
                req.transaction = transaction
                req.message = msg
                req.scene = SendMessageToWX.Req.WXSceneSession  // 目前只支持会话
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo ToastUtil.showCustomToast("打开微信失败")
            }

        }

        /**
         *  打开微信小程序
         */
        fun openMiniProgramWeChat(
            path: String = "",
            @IntRange(
                from = 0,
                to = 2
            )
            miniprogramType: Int = MINI_PROGRAM_TYPE_WECHAT, id: String = WX_MINI_ID
        ) {
            try {
                val req = WXLaunchMiniProgram.Req()
                // 填小程序原始id
                req.userName = id
                //小程序页面路径 拉起小程序页面的可带参路径，不填默认拉起小程序首页
                req.path = path
                req.miniprogramType = miniprogramType
                WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
            } catch (e: Exception) {
                // todo ToastUtil.showCustomToast("打开微信失败")
            }
        }


        /**
         * 授权登录
         * @param scope 应用授权作用域，如获取用户个人信息则填写 snsapi_userinfo
         * @param state 用于保持请求和回调的状态，授权请求后原样带回给第三方。
         */
        fun authLoginWeChat(
            scope: String = "snsapi_userinfo",
            state: String = AUTH_LOGIN_WECHAT_STATE
        ) {
            val req = SendAuth.Req()
            req.scope = scope
            req.state = state
            WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
        }
    }
}
