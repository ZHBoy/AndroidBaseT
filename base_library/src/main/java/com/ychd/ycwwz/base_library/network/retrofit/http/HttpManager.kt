package com.ychd.ycwwz.base_library.network.retrofit.http

import android.content.Context
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.network.retrofit.ApiConstants
import com.ychd.ycwwz.base_library.network.retrofit.callback.ApiBodyResponse
import com.ychd.ycwwz.base_library.network.retrofit.callback.ApiResponse
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextListener
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextResponseListener
import com.ychd.ycwwz.base_library.utils.TLog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :网络请求管理器
 **/
class HttpManager private constructor() {

    companion object {
        @JvmStatic
        fun instance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = HttpManager()
    }

    private var msg: String = ""

    private var option: RequestOption? = null

    private var activityEvent: ActivityEvent? = null

    fun showProgress(msg: String): HttpManager {
        this.msg = msg
        return this
    }

    fun setOption(option: RequestOption?): HttpManager {
        this.option = option
        return this
    }

    /**
     * 控制结束生命周期的节点默认是ActivityEvent.DESTROY
     */
    fun setBindUntilEventType(activityEvent: ActivityEvent): HttpManager {
        this.activityEvent = activityEvent
        return this
    }

    /**
     * 用户非RxAppCompatActivity()比如Service 、BroadcastReceiver 以及在application中调用的等
     */
    fun doHttpDeal(
        context: Context,
        observable: Observable<String>,
        listener: HttpOnNextListener
    ) {
        observable
            /*http请求线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isEmpty())
                    error("数据格式错误！")
                it
            }
            .apply {
                val apiResponse = if (option == null) {
                    ApiResponse(
                        context,
                        msg,
                        RequestOption(),
                        listener
                    )
                } else {
                    ApiResponse(
                        context,
                        msg,
                        option!!,
                        listener
                    )
                }
                option = null
                this.subscribe(apiResponse)
            }
    }

    /**
     * 自动处理生命周期的调用
     */
    fun doHttpDeal(
        context: RxAppCompatActivity,
        observable: Observable<String>,
        listener: HttpOnNextListener
    ) {
        observable
            /*http请求线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            .compose(context.bindUntilEvent(ActivityEvent.DESTROY))
            .map {
                if (it.isEmpty())
                    error("数据格式错误！")
                it
            }
            .apply {
                val apiResponse = if (option == null) {
                    ApiResponse(
                        context,
                        msg,
                        RequestOption(),
                        listener
                    )
                } else {
                    ApiResponse(
                        context,
                        msg,
                        option!!,
                        listener
                    )
                }
                option = null
                this.subscribe(apiResponse)
            }
    }

    /**
     * 自动处理生命周期的调用（返回response原始数据）
     */
    fun doHttpDealForResponseBody(
        context: RxAppCompatActivity,
        observable: Observable<Response<String>>,
        listener: HttpOnNextResponseListener<Response<String>>
    ) {
        observable
            /*http请求线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            .compose(context.bindUntilEvent(ActivityEvent.DESTROY))
            .map {
                if (it.body().isNullOrEmpty()) {
                    error("数据格式错误！")
                }
                it
            }
            .apply {

                val apiResponse = if (option == null) {
                    ApiBodyResponse(
                        context,
                        msg,
                        RequestOption(),
                        listener
                    )
                } else {
                    ApiBodyResponse(
                        context,
                        msg,
                        option!!,
                        listener
                    )
                }
                option = null
                this.subscribe(apiResponse)
            }
    }

    fun <T> createService(
        serviceClass: Class<T>,
        baseUrl: String? = ApiConstants.BASE_URL
    ): T {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(baseUrl ?: ApiConstants.BASE_URL)

        return retrofitBuilder
            .client(OkHttpClientUtil.instance)
            .build()
            .create(serviceClass)
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor {
            TLog.i("HttpLoggingInterceptor", it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    //将参数值转成RequestBoby
    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }
}