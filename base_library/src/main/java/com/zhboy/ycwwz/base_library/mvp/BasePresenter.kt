package com.zhboy.ycwwz.base_library.mvp

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :MVP P层
 **/
interface BasePresenter {

    interface  ResponseListener<T>{
        fun getDataSuccess(dataBean: T)
        fun getDataError()
    }
}