package com.ychd.ycwwz.user_library.ui.login

import com.ychd.commonlibrary.data.login.LoginBean
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.mvp.BaseView

/**
 * @author:  ZhouH
 * @date: 2019/12/7
 * @description: 登录相关接口
 **/
interface LoginContract {

    interface Presenter : BasePresenter {
        /**有财用户登录**/
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