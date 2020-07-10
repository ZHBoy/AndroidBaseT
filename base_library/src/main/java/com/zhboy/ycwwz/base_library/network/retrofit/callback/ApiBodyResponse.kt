package com.zhboy.ycwwz.base_library.network.retrofit.callback


import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.zhboy.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.zhboy.ycwwz.base_library.network.retrofit.exception.ApiErrorType
import com.zhboy.ycwwz.base_library.network.retrofit.http.RequestOption
import com.zhboy.ycwwz.base_library.BaseApplication
import com.zhboy.ycwwz.base_library.R
import com.zhboy.ycwwz.base_library.utils.TLog
import com.zhboy.ycwwz.base_library.widgets.LoadingDialog
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *@author : HaoBoy
 *@date : 2019/4/9
 *@description :网络请求的返回(将response的原始数据返回回来)
 **/
class ApiBodyResponse<T> constructor(
    private val context: Context,
    private val msg: String,
    private val option: RequestOption,
    private val listener: HttpOnNextResponseListener<T>?
) : Observer<T> {

    companion object {
        private const val START_TIME_NUM = 2000
    }

    private val className = context.javaClass.simpleName
    private var startTime: Long = 0
    private var disposable: Disposable? = null

    override fun onComplete() {
        LoadingDialog.dismiss()
        if (startTime > 0 && BaseApplication.debugging) {
            startTime = System.currentTimeMillis() - startTime
            if (startTime > START_TIME_NUM) {
                TLog.i("接口访问 " + className.toString() + context.resources.getString(R.string.remote_elapsed_time_warm) + startTime)
            } else {
                TLog.i("接口访问 " + className.toString() + context.resources.getString(R.string.remote_elapsed_time_like) + startTime)
            }
            startTime = 0
        }
        disposable?.dispose()
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        listener?.onSubscribe(d)

        startTime = System.currentTimeMillis()
        if (option.isShowProgress) {
            if (context is Activity)
                LoadingDialog.show(context, msg)
        }
    }

    override fun onNext(response: T) {
        listener?.onNext(response)
    }

    override fun onError(error: Throwable) {
        LoadingDialog.dismiss()
        errorDo(error)
    }

    private fun errorDo(e: Throwable) {
        TLog.i("HttpLoggingInterceptor", "http_error>>>>>>${e.message}")
        if (e is HttpException) {

            val apiErrorModel: ApiErrorModel? = when (e.code()) {
                ApiErrorType.INTERNAL_SERVER_ERROR.code ->
                    ApiErrorType.INTERNAL_SERVER_ERROR.getApiErrorModel(context)
                ApiErrorType.BAD_GATEWAY.code ->
                    ApiErrorType.BAD_GATEWAY.getApiErrorModel(context)
                ApiErrorType.NOT_FOUND.code ->
                    ApiErrorType.NOT_FOUND.getApiErrorModel(context)
                ApiErrorType.PARSING_FAILURE.code ->
                    ApiErrorType.PARSING_FAILURE.getApiErrorModel(context)
                ApiErrorType.RELOGIN.code -> {
                    ApiErrorType.RELOGIN.getApiErrorModel(context)
                }
                else -> otherError(e)
            }
            listener?.onError(e.code(), apiErrorModel)
            return
        }

        val apiErrorType: ApiErrorType = when (e) {  //发送网络问题或其它未知问题，请根据实际情况进行修改
            is UnknownHostException -> ApiErrorType.NETWORK_NOT_CONNECT
            is ConnectException -> ApiErrorType.NETWORK_NOT_CONNECT
            is SocketTimeoutException -> ApiErrorType.CONNECTION_TIMEOUT
            else -> ApiErrorType.UNEXPECTED_ERROR
        }
        listener?.onError(apiErrorType.code, apiErrorType.getApiErrorModel(context))
    }

    private fun otherError(e: HttpException): ApiErrorModel? {
        var model: ApiErrorModel? = null
        try {
            model =
                Gson().fromJson(e.response()?.errorBody()?.charStream(), ApiErrorModel::class.java)

        } catch (e: Exception) {
            TLog.i(e.toString())
        }

        return model
    }


}