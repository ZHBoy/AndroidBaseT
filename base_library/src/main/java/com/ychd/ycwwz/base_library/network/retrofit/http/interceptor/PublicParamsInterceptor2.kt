package com.ychd.ycwwz.base_library.network.retrofit.http.interceptor

import cn.jpush.android.api.JPushInterface
import com.umeng.analytics.AnalyticsConfig
import com.ychd.ycwwz.base_library.BaseApplication.Companion.appContext
import com.ychd.ycwwz.base_library.BuildConfig
import com.ychd.ycwwz.base_library.utils.DevicesUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException


/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :公共参数拦截器
 **/
class PublicParamsInterceptor2 : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val oldRequest = chain.request()

        var newRequestBuild: Request.Builder? = oldRequest.newBuilder()
        val method = oldRequest.method()

        val map = HashMap<String, String?>()
        map["devicePlatform"] = "android"
        map["channelName"] = AnalyticsConfig.getChannel(appContext)
        map["appType"] = "0" //app类型 0：有财 1：好乡音
        map["androidId"] = DevicesUtils.instance.getAndroidId()
        map["registrationId"] = JPushInterface.getRegistrationID(appContext)
        map["oaid"] = DevicesUtils.instance.getIMEI(appContext!!)
        map["versionCode"] =""+ BuildConfig.VERSION_CODE
        map["versionName"] = BuildConfig.VERSION_NAME

        if ("POST" == method) {
            val oldBody = oldRequest.body()
            when (oldBody) {
                is FormBody -> {

                    val newBodyBuilder = FormBody.Builder()
                    newRequestBuild = oldRequest.newBuilder()

                    for (i in 0 until oldBody.size()) {
                        newBodyBuilder.add(oldBody.name(i), oldBody.value(i))
                    }
                    for (obj in map) {
                        newBodyBuilder.add(obj.key, obj.value ?: "")
                    }

                    newRequestBuild!!.post(newBodyBuilder.build())
                }
                is MultipartBody -> {
                }
                else -> {

                }
            }
        } else {
            // 添加新的参数
            val commonParamsUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())

            for (obj in map) {
                commonParamsUrlBuilder.addQueryParameter(obj.key, "${obj.value}")
            }

            newRequestBuild = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(commonParamsUrlBuilder.build())
        }

        val newRequest = newRequestBuild!!
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "zh")
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "ycwwz")
            .build()

        val response = chain.proceed(newRequest)
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()

        return response
            .newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .header("application/json", "Content-Type")
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept"
            )
            .addHeader("Access-Control-Max-Age", "3600")
            .addHeader("Access-Control-Allow-Origin", "*")
            .addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
            .build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }

}