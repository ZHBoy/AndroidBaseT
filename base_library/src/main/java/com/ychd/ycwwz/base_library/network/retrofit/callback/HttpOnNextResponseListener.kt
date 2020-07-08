package com.ychd.ycwwz.base_library.network.retrofit.callback

import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.ychd.ycwwz.base_library.utils.ToastUtil
import io.reactivex.rxjava3.disposables.Disposable

/**
 *@author : HaoBoy
 *@date : 2019/4/9
 *@description :网络请求回调监听(返回完整response)
 **/
abstract class HttpOnNextResponseListener<T> {

    open fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {

        apiErrorModel?.message?.let {
            ToastUtil.showShortToastCenter(it)
        }
    }

    abstract fun onNext(response: T)

    open fun onSubscribe(disposable: Disposable?) {
    }

}