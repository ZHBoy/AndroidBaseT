package com.zhboy.ycwwz.base_library.network.retrofit.callback

import com.zhboy.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.zhboy.ycwwz.base_library.utils.ToastUtil
import io.reactivex.rxjava3.disposables.Disposable

/**
 *@author : HaoBoy
 *@date : 2019/4/9
 *@description :网络请求回调监听
 **/
abstract class HttpOnNextListener {

    open fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
        apiErrorModel?.message?.let {
            ToastUtil.showShortToastCenter(it)
        }
    }

    open fun onCache(jsonResponse: String) {
        onNext(jsonResponse)
    }

    abstract fun onNext(json: String)

    open fun onSubscribe(disposable: Disposable?) {

    }

}