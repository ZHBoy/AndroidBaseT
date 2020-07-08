package com.ychd.ycwwz.base_library.utils.timer

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *@author : HaoBoy
 *@date : 2019/4/10
 *@description :倒计时
 **/
class RxTimer {


    private var disposables: CompositeDisposable = CompositeDisposable()

    /**
     * 倒计时  传入的参数为秒
     */
    @SuppressLint("CheckResult")
    fun countDown(count: Long, listener: TimerListener?) {
        if (count <= 0) return
        disposables.clear()
        Observable.intervalRange(0, count + 1, 0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t: Long -> count - t }
            .subscribe({ listener?.onSuccess(it) },
                { listener?.onError(it) },
                { listener?.onCompleted() },
                { disposables.add(it) })

    }


    /**
     * 正计时
     */
    @SuppressLint("CheckResult")
    fun countUp(listener: TimerListener?) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t: Long -> t + 1 }
            .subscribe({ listener?.onSuccess(it) },
                { listener?.onError(it) },
                { listener?.onCompleted() },
                { disposables.add(it) })
    }

    fun finish() {
        disposables.clear()
    }

}