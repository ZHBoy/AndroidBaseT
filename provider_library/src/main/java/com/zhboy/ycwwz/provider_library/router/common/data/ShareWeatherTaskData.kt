package com.zhboy.ycwwz.provider_library.router.common.data

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.zhboy.ycwwz.provider_library.R

// 任务分享  data
/**
 * @param date 日期 04月27日
 * @param temperature
 * @param skyconIconResId  状况 icon id
 * @param skycon  状况
 * @param airQualityColor 空气质量颜色
 * @param airQuality 空气质量
 */
class ShareWeatherTaskData(
    var place: String = "",
    var bgUrl: String = "",
    var date: String = "",
    var temperature: String = "",
    @DrawableRes var skyconIconResId: Int = R.drawable.backgroud_c9c9c9_radius_30,
    var skycon: String = "",
    @ColorInt var airQualityColor: Int = Color.parseColor("#00CAE2"),
    var airQuality: String = ""
) {
    /**
     * 数据加载完毕
     */
    fun dataPrepareComplete():Boolean {
        return place.isNullOrBlank().not() && bgUrl.isNullOrBlank().not() && airQuality.isNullOrBlank().not()
    }

}