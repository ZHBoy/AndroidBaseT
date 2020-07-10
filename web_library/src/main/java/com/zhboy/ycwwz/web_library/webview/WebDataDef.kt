package com.zhboy.ycwwz.web_library.webview

import com.zhboy.ycwwz.base_library.network.retrofit.ApiConstants

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

    val WEB_FOR_SHOW_LOGISTICS_LIST = ApiConstants.BASE_URL + "#/prizeRecord"

    const val WEB_FOR_USER_SERVICES_AGREEMENT = ApiConstants.BASE_URL + "#/userServicesAgreement"

    const val WEB_FOR_PRIVACY_POLICY = ApiConstants.BASE_URL + "#/privacyPolicy"



}
