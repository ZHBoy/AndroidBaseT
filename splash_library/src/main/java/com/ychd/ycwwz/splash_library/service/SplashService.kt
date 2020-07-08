package com.ychd.ycwwz.splash_library.service

import com.ychd.ycwwz.base_library.network.retrofit.ApiConstants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

/**
 * @author zhou_hao
 * @date 2020-02-13
 * @description: 公共接口service
 */
interface SplashService {

    /**
     * 开屏页
     */
    @GET(ApiConstants.COMMON_APP_OPERATION_PAGE)
    fun appSpalsh(@Query("type") type: String): Observable<String>

}