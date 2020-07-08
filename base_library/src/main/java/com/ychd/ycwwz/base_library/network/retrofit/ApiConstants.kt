package com.ychd.ycwwz.base_library.network.retrofit

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :接口相关
 **/
class ApiConstants private constructor() {

    companion object {
        //接口前缀
        const val BASE_URL = "https://calendar.yhzm.net/"

        //app的更新
        const val COMMON_APP_UPDATE = "calendar/extra/getAppVersion"

        //获取congfig配置信息
        const val COMMON_CONFIG = "calendar/extra/getConfig"

        //获取渠道广告是否开启
        const val GET_CHANNEL_IS_OPEN = "calendar/extra/getAdsIsOpenByChannelName"

        //开屏页
        const val COMMON_APP_OPERATION_PAGE = "calendar/operation/listAppOperationPage"

        //新版用户登录（只有微信登录在2.0.7版本加入）
        const val USER_LOGIN_BY_V207 = "calendar/user/register/v3"

        //绑定手机号
        const val USER_BIND_PHONE = "calendar/user/bindPhone"

        //获取验证码
        const val USER_VERIFY_CODE = "calendar/sms/sendVerifyCode"


    }
}