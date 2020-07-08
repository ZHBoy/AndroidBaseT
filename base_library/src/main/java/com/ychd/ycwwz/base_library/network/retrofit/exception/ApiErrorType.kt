package com.ychd.ycwwz.base_library.network.retrofit.exception

import android.content.Context
import com.ychd.ycwwz.base_library.R
import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :服务器异常类型
 **/
enum class ApiErrorType(val code: Int, private val messageId: Int) {

    //根据实际情况进行增删
    INTERNAL_SERVER_ERROR(500, R.string.INTERNAL_SERVER_ERROR),
    BAD_GATEWAY(502, R.string.BAD_GATEWAY),
    RELOGIN(403, R.string.RELOGIN),
    NOT_FOUND(404, R.string.NOT_FOUND),
    CONNECTION_TIMEOUT(408, R.string.CONNECTION_TIMEOUT),
    NETWORK_NOT_CONNECT(499, R.string.NETWORK_NOT_CONNECT),
    UNEXPECTED_ERROR(700, R.string.UNEXPECTED_ERROR),
    PARSING_FAILURE(1001, R.string.PARSING_FAILURE),
    REQUEST_TOOMANY(429, R.string.REQUEST_TOO_MANY);

    private val DEFAULT_CODE = 1

    fun getApiErrorModel(context: Context): ApiErrorModel {
        return ApiErrorModel(
            DEFAULT_CODE,
            context.getString(messageId)
        )
    }
}