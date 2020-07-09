package com.ychd.ycwwz.user_library.ui.login

import com.alibaba.android.arouter.launcher.ARouter
import com.ychd.commonlibrary.data.login.LoginBean
import com.ychd.ycwwz.base_library.constants.AccessManager
import com.ychd.ycwwz.base_library.constants.SpDef
import com.ychd.ycwwz.base_library.extend.fromJson
import com.ychd.ycwwz.base_library.extend.gson
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextResponseListener
import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.ychd.ycwwz.base_library.network.retrofit.http.HttpManager
import com.ychd.ycwwz.base_library.network.retrofit.http.RequestOption
import com.ychd.ycwwz.base_library.utils.DevicesUtils
import com.ychd.ycwwz.base_library.mmkv.MMKVUtils
import com.ychd.ycwwz.base_library.utils.ToastUtil
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.user_library.service.AccountService
import retrofit2.Response

/**
 * @author:  ZhouH
 * @date: 2019/12/7
 * @description: 登录相关接口实现
 **/
class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {

    /**
     * 用户登录（微信登录）
     * userType 1 微信用户
     */
    override fun userLogin(code: String, listener: LoginContract.OnResponseListener<LoginBean>?) {

        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = false
            })
            doHttpDealForResponseBody(
                view.context(),
                createService(AccountService::class.java).userInByV207(
                    code, DevicesUtils.instance.getIMEI(view.context())
                ),
                object : HttpOnNextResponseListener<Response<String>>() {
                    override fun onNext(response: Response<String>) {

                        val resultEntity: LoginBean = gson.fromJson(response.body() ?: "")

                        if (resultEntity.state == 0) {
                            ToastUtil.showShortToast(resultEntity.message ?: "")
                            view.context().finish()
                        } else {
                            resultEntity.data?.let {
                                //清除用户信息(前提是之前是登录过的)
                                AccessManager.clearUserInfo()

                                val h = response.headers()
                                val d = h.values("authorization")
                                AccessManager.setUserAuthorization(d[0])

                                MMKVUtils.encode(SpDef.USER_UID, it.userId ?: "")
                                MMKVUtils.encode(SpDef.USER_PHONE, it.phone ?: "")
                                MMKVUtils.encode(SpDef.USER_NICK_NAME, it.nickname ?: "")
                                MMKVUtils.encode(SpDef.USER_TYPE, it.userType ?: "")
                                MMKVUtils.encode(SpDef.USER_OPEN_ID, it.openId ?: "")
                                MMKVUtils.encode(SpDef.USER_HEAD_IMAGE, it.headImageUrl ?: "")
                                MMKVUtils.encode(SpDef.USER_CREAT_TIME, it.createTime ?: "")

                                val createTime =
                                    System.currentTimeMillis() - (it.createTime ?: "0").toLong()
                                val time: Long = 3600 * 1000 * 24 * 7L //7天

                                //如果用户类型为空或者不是2
                                val userType = resultEntity.data?.userType
                                if ((userType.isNullOrEmpty() || userType != "2") && createTime > time) {
                                    ARouter.getInstance()
                                        .build(RouterApi.UserCenter.ROUTER_USER_LOGIN_WITH_PHONE_URL)
                                        .navigation()
                                }
                                view.context().finish()
                            }
                        }
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        ToastUtil.showShortToast("网络异常，请重新登录")
                    }
                })
        }
    }


    init {
        view.setPresenter(this)
    }

}