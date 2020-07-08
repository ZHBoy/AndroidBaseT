package com.ychd.ycwwz.base_library.data

import java.io.Serializable

/**
 * 视频合成天气录入信息（示例：子模块间互相传值）
 */
class VideoInputBean : Serializable {

    var skycon: String? = null //天气状况
    var windDirection: String? = null//风力
    var windLevel: Int = 0//风力
    var temperature: String? = null//温度
    var minTemperature: Int = 0//温度
    var maxTemperature: Int = 0//温度
    var date: String? = null//日期
    var quality: String? = null//空气质量
    var warn: String? = null//提醒

    var header: String? = null//头像链接
    var nikeName: String? = null//昵称

    var videoPath: String? = null//视频路径
    var cover: String? = null//视频封面

    var videoType: String = ""// 视频类型

    var place: String = ""// 地区
}