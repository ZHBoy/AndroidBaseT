package com.zhboy.ycwwz.base_library.network.retrofit.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ThreeServiceErrorInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful){
            return response
        }else{
            val oriCode = response.code()
            val oriMessage = response.message()
            return response.newBuilder().code(-999).message("三方服务错误:$oriMessage:$oriCode").build()
        }
    }
}