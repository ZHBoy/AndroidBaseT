package com.ychd.ycwwz.base_library.network.retrofit.http

import android.text.TextUtils
import com.ychd.ycwwz.base_library.network.retrofit.ApiConstants

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :网络请求工具类
 **/
class RequestOption {


    var isShowProgress = true

    var isCanCancel = true

    var method = ""

    var connectionTime = 10L//请求时间

    var readTime = 15L//读取时间

    var writeTime = 15L//写入时间

    var cookieNetWorkTime = 60

    var cookieNoNetWorkTime = 60 * 60 * 24 * 30

    var retryCount = 1

    var retryDelay = 100

    var retryIncreaseDelay = 10

    var cacheUrl = ""

    fun getUrl() = if (TextUtils.isEmpty(cacheUrl)) {
        "${ApiConstants.BASE_URL}$method"
    } else {
        cacheUrl
    }
}