package com.ychd.ycwwz.base_library.mvp

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :MVP  V层
 **/
interface BaseView<in T> {

    fun setPresenter(presenter : T)

}