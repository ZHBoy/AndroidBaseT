package com.ychd.ycwwz.web_library.webview

import com.ychd.ycwwz.base_library.network.retrofit.ApiConstants

/**
 *@author : YMH
 *@date : 2020/3/03
 *@description :与前端交互的URL地址
 **/
object WebDataDef {

    //WebView字体控制，防止设置了系统字体大小，导致出现适配问题
    const val WEBVIEW_TEXT_ZOOM = 100

    //WebView交互的作用域
    const val WEBVIEW_JAVASCRIPT_INTERFACE = "rich"

    //赚钱攻略
    val WEB_FOR_SHOW_LOGISTICS_LIST = ApiConstants.BASE_URL + "#/prizeRecord"

    //有财惠生活用户服务协议 / 小财好物用户服务协议
    const val WEB_FOR_USER_SERVICES_AGREEMENT = ApiConstants.BASE_URL + "#/userServicesAgreement"

    //有财惠生活隐私政策协议 / 小财好物隐私政策协议
    const val WEB_FOR_PRIVACY_POLICY = ApiConstants.BASE_URL + "#/privacyPolicy"



}
