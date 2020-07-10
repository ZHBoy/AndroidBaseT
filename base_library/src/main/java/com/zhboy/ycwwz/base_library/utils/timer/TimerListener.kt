package com.zhboy.ycwwz.base_library.utils.timer

import android.util.Log

/**
 *@author : HaoBoy
 *@date : 2019/4/10
 *@description :倒计时监听器
 **/
abstract class TimerListener {

    abstract fun onSuccess(value: Long)

    abstract fun onCompleted()

    open fun onError(throwable: Throwable) {
        Log.e("rxTimer", "timer error >>>>>>>>>> " + throwable.message)
    }

}