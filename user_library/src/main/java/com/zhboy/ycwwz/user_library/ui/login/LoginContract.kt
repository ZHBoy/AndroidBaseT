package com.zhboy.ycwwz.user_library.ui.login

import com.zhboy.commonlibrary.data.login.LoginBean
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.zhboy.ycwwz.base_library.mvp.BasePresenter
import com.zhboy.ycwwz.base_library.mvp.BaseView

/**
 * @author:  ZhouH
 * @date: 2019/12/7
 * @description: 登录相关接口
 **/
interface LoginContract {

    interface Presenter : BasePresenter {
        /**基础框架用户登录**/
        fun userLogin(code: String, listener: OnResponseListener<LoginBean>?)
    }

    interface View : BaseView<Presenter> {
        fun context(): RxAppCompatActivity
    }

    interface OnResponseListener<T> {
        fun onSuccess(result: T)
        fun onError()
    }
}