package com.ychd.ycwwz.splash_library.persenter

import com.ychd.ycwwz.base_library.extend.fromJson
import com.ychd.ycwwz.base_library.extend.gson
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextListener
import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.ychd.ycwwz.base_library.network.retrofit.http.HttpManager
import com.ychd.ycwwz.base_library.network.retrofit.http.RequestOption
import com.ychd.ycwwz.splash_library.service.SplashService
import com.ychd.ycwwz.splash_library.service.data.SplashBean
import com.ychd.ycwwz.splash_library.ui.SplashActivity

/**
 * @author ZhouH
 * @date 2019/12/6
 * @description:开屏页Presenter
 */
class SplashPresenter(private val view: SplashContract.View) : SplashContract.Presenter {

    private var activity: SplashActivity? = null

    init {
        activity = view.context() as SplashActivity
    }

    /**
     * 接口获取开屏页数据
     *
     **/
    override fun getSplashDateApi(listener: SplashContract.SplashResponseListener<SplashBean>?) {
        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = false
            })
            doHttpDeal(
                view.context() as SplashActivity,
                createService(SplashService::class.java).appSpalsh("0"),//返回的图片组类型 0是开屏
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        val resultEntity: SplashBean = gson.fromJson(json)
                        //正常加载
                        if (resultEntity.state == 1) {
                            listener?.onSuccess(resultEntity)
                        } else {
                            listener?.onError()
                        }
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        listener?.onError()
                    }
                })
        }
    }

    /**
     * 开屏页图片点击
     */
    override fun clickSpalshImage() {

        if (activity == null)
            return
    }

    init {
        view.setPresenter(this)
    }

}