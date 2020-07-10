package com.zhboy.ycwwz.base_library.network.retrofit.callback

/**
 *@author : HaoBoy
 *@date : 2019/4/9
 *@description :网络请求回调基础类
 **/
data class BaseResultEntity<T>(var errorCode: Int, var errorMessage: String = "错误", var data: T?)