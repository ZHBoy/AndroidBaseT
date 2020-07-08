package com.ychd.ycwwz.base_library.network.retrofit.http

import com.ychd.ycwwz.base_library.BaseApplication
import com.ychd.ycwwz.base_library.network.retrofit.http.interceptor.PublicParamsInterceptor2
import com.ychd.ycwwz.base_library.utils.TLog
import java.util.concurrent.TimeUnit

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy

/**
 * @author zhou_hao
 * @date 2020-06-30
 * @description: OkHttpClient单例，防止创建多个线程池oom
 */
object OkHttpClientUtil {

    val instance: OkHttpClient
        get() = Singleton.INSTANCE.instance

    private enum class Singleton {
        INSTANCE;

        val instance: OkHttpClient

        init {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(10L, TimeUnit.SECONDS)
            builder.readTimeout(10L, TimeUnit.SECONDS)
            builder.writeTimeout(10L, TimeUnit.SECONDS)
            builder.addInterceptor(PublicParamsInterceptor2())//公共参数
            if (!BaseApplication.debugging) {
                builder.proxy(Proxy.NO_PROXY)//禁止使用代理
            }
            //防止内存溢出
            if (BaseApplication.debugging) {
                builder.addInterceptor(getHttpLoggingInterceptor())
            }

            val connectionPool = ConnectionPool(50, 60, TimeUnit.SECONDS)
            builder.connectionPool(connectionPool)
            instance = builder.build()
        }
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor {
            TLog.i("HttpLoggingInterceptor", it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }


}