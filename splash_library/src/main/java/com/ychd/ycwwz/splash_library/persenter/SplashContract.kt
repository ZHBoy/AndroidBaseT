package com.ychd.ycwwz.splash_library.persenter

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.mvp.BaseView
import com.ychd.ycwwz.splash_library.service.data.SplashBean

/**
 *@author : HaoBoy
 *@date : 2019/4/11
 *@description :开屏页mvp控制类
 **/
interface SplashContract {

    interface Presenter : BasePresenter {

        /** 开屏页跳转**/
        fun clickSpalshImage()

        /** 接口获取开屏页数据**/
        fun getSplashDateApi(listener: SplashResponseListener<SplashBean>?)

    }

    interface View : BaseView<Presenter> {
        fun context(): RxAppCompatActivity
    }

    interface SplashResponseListener<T> {
        fun onSuccess(splash: T)

        fun onError()
    }

}