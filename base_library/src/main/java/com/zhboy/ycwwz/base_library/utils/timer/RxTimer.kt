package com.zhboy.ycwwz.base_library.utils.timer

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
            .subscribe(object :Observer<Long>{
                override fun onComplete() {
                    listener?.onCompleted()
                }

                override fun onSubscribe(d: Disposable?) {
                    disposables.add(d)
                }

                override fun onNext(t: Long?) {
                    listener?.onSuccess(t!!)
                }

                override fun onError(e: Throwable?) {
                    listener?.onError(e!!)
                }

            })

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
            .subscribe(object :Observer<Long>{
                override fun onComplete() {
                    listener?.onCompleted()
                }

                override fun onSubscribe(d: Disposable?) {
                    disposables.add(d)
                }

                override fun onNext(t: Long?) {
                    listener?.onSuccess(t!!)
                }

                override fun onError(e: Throwable?) {
                    listener?.onError(e!!)
                }

            })
    }

    fun finish() {
        disposables.clear()
    }

}