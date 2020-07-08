package com.ychd.ycwwz.base_library.data

/**
 * 公共信息类
 */
class ConfigBean {

    var state: Int = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {
        var hl_app_key: String? = null
        var wx_app_secret: String? = null
        var wx_app_id: String? = null
        var news_article: String? = null
        var weather_key: String? = null
        var share_lottery_num: String? = null
        var weather_secret: String? = null
        var video_state: String? = null
        var news_video: String? = null
        var weather_code: String? = null
        var every_day_lottery_num: String? = null
        var first_advertising_probability: String? = null
        var new_user_advertising_day: String? = null
        var second_advertising_probability: String? = null
        var is_show_ec: Int = 0

        var video_show_ad_item_number: String? = null //视频模块展示广告的条目
        var video_show_ad_jl_probability: String? = null //展示激励视频的概率（100以内的值）
        var continuous_lottery_video: Int? = null //连续抽奖第几次得激励视屏弹窗

        var show_vip_popup_no_lottery_probability: Int? = 0//展示vip弹框的概率（100以内的值）

        var watch_video_time: Int? = 33    //奖励金币时间

        var integral_egg_show_time: Int? = 120

        var flash_is_open: Int? = 0//0是关1是开

        var read_news_award_time: Int? = 25//倒计时默认值

        var share_to_wx_dialog_closeable: Int? = 0//中金币后分享弹框是否可以关闭 0不可以 1可以

        var lottery_give_gift_to_firend_text: String? = null//抽奖页送好友礼物的文案

        var douyin_apk_is_show: String? = "0"//是否是抖音渠道

        var customerWXId: String? = null//微信的客服号码

        var is_all_show_egg_reward_tt: String? = "0"//是否全部显示彩蛋的激励视频 默认不行

        var every_day_lottery_num_stay_time: String? = null

        var ads_probability_page: Int = 100

        var ads_probability_banner: Int = 100

        var ads_probability_video: Int = 100

        var zhuanpan_run_ms: Long = 100 //转盘转动的时间

        var weather_app_share_url: String? = null //天气分享的URL链接

        var weather_app_download_url: String? = null //天气下载的URL链接

        var yc_weather_popup_is_open: Int = 1 //天气下载弹框是否显示0关 1开

        var ads_probability_page_baidu: Int = 0

        var ads_probability_banner_baidu: Int = 0

        var ads_probability_video_baidu: Int = 0

        var ads_probability_page_chuanshanjia: Int = 0

        var ads_probability_banner_chuanshanjia: Int = 0

        var ads_probability_video_chuanshanjia: Int = 0

        var ads_probability_page_guandiantong: Int = 0

        var ads_probability_banner_guandiantong: Int = 0

        var ads_probability_video_guandiantong: Int = 0

        var ad_skip_page_probability: Int = 0

        var lottery_popup_bananer_ads: Int = 0

        var lottery_popup_bananer_tlj: Int = 0

        var lottery_popup_bananer_redPacket: Int = 0

        var lottery_popup_bananer_jump: Int = 0

        var yc_prize_conversion_pid: String = "mm_111470606_1569400186_110294200015"

        var see_taobao_page_time = 60//浏览商品得抽奖机会的时间

        var see_taobao_page_time_reward = 1//浏览商品得抽奖机会的个数


        // 新用户 0 元购 开关 0代表否 1代表是
        var new_user_0price_taobao_prize_is_opne = 0
        // 新用户 150 次兑换 开关 0代表否 1代表是
        var new_user_150letterynum_prize_is_opne = 0
        // 活动开始时间（活动开始后注册的用户视为 新用户）
        var new_user_free_prize_start_time: Long = 0L
        // 活动持续时间
        var new_user_free_prize_duration: Long = 0L

        var yc_roll_news = ""

        // 新人引导弹框是否可以关闭 0不显示 1显示
        var control_to_login_close_is_show = 0
    }
}
