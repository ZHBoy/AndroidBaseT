package com.zhboy.ycwwz.user_library.service

import com.zhboy.ycwwz.base_library.network.retrofit.ApiConstants
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * @author zhou_hao
 * @date 2020-02-13
 * @description: 用户相关接口service
 */
interface AccountService {

    /**
     * 3.0.3后的用户注册（仅有微信登录）
     *
     * @param userType  微信用户
     * @param code 微信的code
     * @return
     */
    @POST(ApiConstants.USER_LOGIN_BY_V207)
    @FormUrlEncoded
    fun userInByV207(
        @Field("code") code: String,
        @Field("imei") imei: String
    ): Observable<Response<String>>


    /**
     * 2.0.7后的用户微信登录需要绑定手机号
     *
     * @param phone  手机号
     * @param userId       用户设备号
     * @param verifyCode 验证码
     * @return
     */
    @POST(ApiConstants.USER_BIND_PHONE)
    @FormUrlEncoded
    fun userBindPhone(
        @Field("userId") userId: String,
        @Field("phone") phone: String,
        @Field("verifyCode") verifyCode: String
    ): Observable<String>

    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @return
     */
    @POST(ApiConstants.USER_VERIFY_CODE)
    @FormUrlEncoded
    fun getVerifyCode(@Field("phone") phone: String): Observable<String>

}