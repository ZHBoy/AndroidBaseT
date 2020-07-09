package com.ychd.ycwwz.base_library

import com.ychd.ycwwz.base_library.utils.DateUtils

/**
 *@author : HaoBoy
 *@date : 2019/5/3
 *@description :app相关常量
 **/
object CommonDef {

    //游戏sdk的首页URL地址
    var game_web_url = "https://ai.m.taobao.com/"

    //去登录x号是否可以关闭
    var CONTROL_TO_LOGIN_CLOSE_IS_SHOW = 0

    //浏览淘宝模块多少秒（默认60），获取抽奖机会
    var WATCH_TAOBAO_GET_LOTTERY_TIME = 60

    //浏览淘宝模块多少秒（默认60），获取抽奖机会次数
    var WATCH_TAOBAO_GET_LOTTERY_NUMBER = 1

    //天气下载链接
    val weatherYybLink = "https://sj.qq.com/myapp/detail.htm?apkName=com.ychd.weather"
    //转动的秒数
    var RCIRCLE_NUM = 0
    //微信appid
    const val WX_APP_ID = "wxb13120b26cceefe0"

    //微信小程序ID
    const val WX_MINI_ID = "gh_a646e7b5f8b6"

    //客服电话
    const val HELPER_PHONE = "18611442728"

    var YC_PRIZE_CONVERSION_PID = "mm_111470606_1569400186_110294200015"

    //WebView字体控制，防止设置了系统字体大小，导致出现适配问题
    const val WEBVIEW_TEXT_ZOOM = 100

    //WebView交互的作用域
    const val WEBVIEW_JAVASCRIPT_INTERFACE = "rich"

    //天气刷新的时间间隔（1个小时）
    const val WEATHER_REFRESH_TIME = 3600000

    //获取当前时间
    var CURRENT_DATE: String = DateUtils.instance.getDateyyyMMdd()

    //金币当前转动时间
    var currentTime = 0

    //金币总转动时间
    var maxTime = 33000
    //倒计时（单位：分钟)用于一分钟调一次
    var countDownMinuteTotal = 1
    //倒计时（单位：分钟)
    var countDownMinute = 0
    //抽奖倒计时
    var drawCountdownTime: Int = 1200
    //资讯倒计时剩余
    var informationCountdown: Int = 1200
    //视频倒计时
    var videoCountdown = 0

    //彩蛋显示时间
    var maxWatchTime = 120

    var adsProbabilityPage = 0

    var adsProbabilityBanner = 100

    var adsProbabilityReward = 0

    //控制广告的开关 0关 1开
    var flashIsOpen = 0

    //后台切回前台显示开屏页
    var backgroudToForwardToShoSplash = System.currentTimeMillis()
    //时间间隔10000 即30秒
    const val intervalTime = 30000

    //存储当天的抽奖次数
    var TODAY_CHOU_NUM = 1

    //首页天气弹框分享，展示toast(记得置空)
    var home_weather_share_toast: String? = null


    //返回的倒计时依次的分钟数
    var countDowndurationlistString = "30"

    //有财天气分享的落地页
    var weather_app_share_url: String? = null
    //天气APP的下载链接
    var weather_app_download_url: String? = null
    //天气下载弹框是否显示0关 1开
    var yc_weather_popup_is_open: Int = 1

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


    //弹窗的比例（广告，礼金红包发放，图片+链接）
    var lottery_popup_bananer_ads: Int = 0
    var lottery_popup_bananer_tlj: Int = 0
    var lottery_popup_bananer_redPacket: Int = 0
    var lottery_popup_bananer_jump: Int = 0

    //误点的范围的概率
    var sizeConfig1:Int=100
    var sizeConfig2:Int=0
    var sizeConfig3:Int=0

    var alreadyLotteryNum: Int = 0
    var isNewUser: Int = 0
    var newUserConversionLotteryNum: Int = 0

    var isNotYouCaiCopy = true


    // 新用户 0 元购 开关
    var new_user_0price_taobao_prize_is_opne = 0
    // 新用户 150 次兑换 开关
    var new_user_150letterynum_prize_is_opne = 0
    // 活动开始时间（活动开始后注册的用户视为 新用户）
    var new_user_free_prize_start_time: Long = 0L
    // 活动持续时间
    var new_user_free_prize_duration: Long = 0L

    //礼品库界面滚动条内容
    var yc_roll_news = ""

    //淘宝一页获取条目数
    var taobao_list_page_size = 30


    //淘礼金数值
    var taoLiJin_Number=0.0
    //

    var useType_Lottry=1005

    var isNewDialog=0

    /**
     * 爆炸动画效果
     */
//    val mDrawableResIds = intArrayOf(
//        R.drawable.icon_bomb_1,
//        R.drawable.icon_bomb_2,
//        R.drawable.icon_bomb_3,
//        R.drawable.icon_bomb_4,
//        R.drawable.icon_bomb_5,
//        R.drawable.icon_bomb_6,
//        R.drawable.icon_bomb_7,
//        R.drawable.icon_bomb_8,
//        R.drawable.icon_bomb_9,
//        R.drawable.icon_bomb_10,
//        R.drawable.icon_bomb_11,
//        R.drawable.icon_bomb_12,
//        R.drawable.icon_bomb_13,
//        R.drawable.icon_bomb_14,
//        R.drawable.icon_bomb_15
//    )

}
