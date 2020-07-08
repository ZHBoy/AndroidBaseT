package com.ychd.ycwwz.base_library.service

import com.ychd.ycwwz.base_library.network.retrofit.ApiConstants
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author zhou_hao
 * @date 2020-02-13
 * @description: 公共接口service
 */
interface CommonService {

    /**
     * 获取app版本信息
     */
    @GET(ApiConstants.COMMON_APP_UPDATE)
    fun getAppUpdate(): Observable<String>

    /**
     * 获取app版本信息
     */
    @GET(ApiConstants.COMMON_CONFIG)
    fun getConfig(): Observable<String>

    /**
     * 获取渠道广告是否开启
     */
    @GET(ApiConstants.GET_CHANNEL_IS_OPEN)
    fun getChannelAdIsOpen(): Observable<String>


}