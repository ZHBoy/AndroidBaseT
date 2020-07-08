package com.ychd.ycwwz.base_library.presenter

import com.ychd.ycwwz.base_library.CommonDef
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.data.AppUpdateBean
import com.ychd.ycwwz.base_library.data.ConfigBean
import com.ychd.ycwwz.base_library.data.OnlyDataBean
import com.ychd.ycwwz.base_library.extend.fromJson
import com.ychd.ycwwz.base_library.extend.gson
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextListener
import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.ychd.ycwwz.base_library.network.retrofit.http.HttpManager
import com.ychd.ycwwz.base_library.network.retrofit.http.RequestOption
import com.ychd.ycwwz.base_library.service.CommonService
import io.reactivex.disposables.Disposable

/**
 *@author :YMH
 *@date : 2020/2/26
 *@description :公共类的P层
 **/
class CommonPresenter : ICommon.Presenter {

    /**
     * 清除数据
     */
    fun destoryData() {
        channalAdDisposable?.dispose()
        channalAdDisposable = null
    }

    //按渠道关闭广告
    private var channalAdDisposable: Disposable? = null

    /**
     * 获取渠道配置
     */
    override fun getChannalAdIsOpen(activity: BaseActivity) {
        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = false
            })
            doHttpDeal(
                activity,
                createService(CommonService::class.java).getChannelAdIsOpen(),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        val resultEntity: OnlyDataBean = gson.fromJson(json)
                        if (resultEntity.state == 1) {
                            if (resultEntity.data == "1") {
                                CommonDef.flashIsOpen = 1
                            } else {
                                CommonDef.flashIsOpen = 0
                            }
                        } else {
                            CommonDef.flashIsOpen = 0
                        }
                        channalAdDisposable?.dispose()
                        channalAdDisposable = null
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        CommonDef.flashIsOpen = 0
                        channalAdDisposable?.dispose()
                        channalAdDisposable = null
                    }

                    override fun onSubscribe(disposable: Disposable?) {
                        super.onSubscribe(disposable)
                        channalAdDisposable = disposable
                    }
                }
            )
        }
    }

    /**
     * 配置信息
     */
    override fun getConfig(
        activity: BaseActivity,
        resultData: BasePresenter.ResponseListener<ConfigBean>?
    ) {
        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = false
            })
            doHttpDeal(
                activity,
                createService(CommonService::class.java).getConfig(),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        val resultEntity: ConfigBean = gson.fromJson(json)

                        CommonDef.CONTROL_TO_LOGIN_CLOSE_IS_SHOW =
                            resultEntity.data?.control_to_login_close_is_show ?: 0
                        CommonDef.weather_app_download_url =
                            resultEntity.data?.weather_app_download_url
                        CommonDef.weather_app_share_url = resultEntity.data?.weather_app_share_url
                        CommonDef.maxTime = (resultEntity.data?.watch_video_time as Int) * 1000
                        CommonDef.maxWatchTime = resultEntity.data?.integral_egg_show_time!!
                        CommonDef.drawCountdownTime = resultEntity.data?.read_news_award_time!! * 60
                        CommonDef.countDownMinute = resultEntity.data?.read_news_award_time!!
                        CommonDef.adsProbabilityPage = resultEntity.data?.ads_probability_page!!
                        CommonDef.adsProbabilityBanner = resultEntity.data?.ads_probability_banner!!
                        CommonDef.adsProbabilityReward = resultEntity.data?.ads_probability_video!!
                        //倒计时得奖的时间
                        CommonDef.countDowndurationlistString =
                            resultEntity.data?.every_day_lottery_num_stay_time!!
                        CommonDef.ads_probability_page_baidu =
                            resultEntity.data?.ads_probability_page_baidu!!
                        CommonDef.ads_probability_banner_baidu =
                            resultEntity.data?.ads_probability_banner_baidu!!
                        CommonDef.ads_probability_video_baidu =
                            resultEntity.data?.ads_probability_video_baidu!!
                        CommonDef.ads_probability_page_chuanshanjia =
                            resultEntity.data?.ads_probability_page_chuanshanjia!!
                        CommonDef.ads_probability_banner_chuanshanjia =
                            resultEntity.data?.ads_probability_banner_chuanshanjia!!
                        CommonDef.ads_probability_video_chuanshanjia =
                            resultEntity.data?.ads_probability_video_chuanshanjia!!
                        CommonDef.ads_probability_page_guandiantong =
                            resultEntity.data?.ads_probability_page_guandiantong!!
                        CommonDef.ads_probability_banner_guandiantong =
                            resultEntity.data?.ads_probability_banner_guandiantong!!
                        CommonDef.ads_probability_video_guandiantong =
                            resultEntity.data?.ads_probability_video_guandiantong!!

                        CommonDef.ad_skip_page_probability =
                            resultEntity.data?.ad_skip_page_probability!!

                        CommonDef.lottery_popup_bananer_ads =
                            resultEntity.data?.lottery_popup_bananer_ads!!

                        CommonDef.lottery_popup_bananer_tlj =
                            resultEntity.data?.lottery_popup_bananer_tlj!!

                        CommonDef.lottery_popup_bananer_redPacket =
                            resultEntity.data?.lottery_popup_bananer_redPacket!!

                        CommonDef.lottery_popup_bananer_jump =
                            resultEntity.data?.lottery_popup_bananer_jump!!

                        CommonDef.yc_weather_popup_is_open =
                            resultEntity.data?.yc_weather_popup_is_open ?: 1

                        CommonDef.YC_PRIZE_CONVERSION_PID =
                            resultEntity.data?.yc_prize_conversion_pid!!

                        CommonDef.WATCH_TAOBAO_GET_LOTTERY_TIME =
                            resultEntity.data?.see_taobao_page_time ?: 60//浏览商品得抽奖机会的时间

                        CommonDef.WATCH_TAOBAO_GET_LOTTERY_NUMBER =
                            resultEntity.data?.see_taobao_page_time_reward ?: 1//浏览商品得抽奖机会的个数
                        CommonDef.new_user_150letterynum_prize_is_opne =
                            resultEntity.data?.new_user_150letterynum_prize_is_opne ?: 1

                        // 新用户 0 元购 开关
                        CommonDef.new_user_0price_taobao_prize_is_opne =
                            resultEntity.data!!.new_user_0price_taobao_prize_is_opne
                        // 新用户 150 次兑换 开关
                        CommonDef.new_user_150letterynum_prize_is_opne =
                            resultEntity.data!!.new_user_150letterynum_prize_is_opne
                        // 活动开始时间（活动开始后注册的用户视为 新用户）
                        CommonDef.new_user_free_prize_start_time =
                            resultEntity.data!!.new_user_free_prize_start_time
                        // 活动持续时间
                        CommonDef.new_user_free_prize_duration =
                            resultEntity.data!!.new_user_free_prize_duration
                        CommonDef.yc_roll_news = resultEntity.data!!.yc_roll_news
//
                        resultData?.getDataSuccess(resultEntity)
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        resultData?.getDataError()
                    }
                }
            )
        }

    }

    /**
     * 版本更新
     */
    override fun appUpdate(
        activity: BaseActivity,
        resultData: BasePresenter.ResponseListener<AppUpdateBean>
    ) {
        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                this.isShowProgress = false
            })
            doHttpDeal(
                activity,
                createService(CommonService::class.java).getAppUpdate(),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        if (json.isBlank()) return
                        val taskBean: AppUpdateBean = gson.fromJson(json)
                        if (taskBean.state == 0) {
                            resultData.getDataSuccess(taskBean)
                        } else {
                            resultData.getDataError()
                        }

                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        resultData.getDataError()
                    }
                }
            )
        }
    }


}