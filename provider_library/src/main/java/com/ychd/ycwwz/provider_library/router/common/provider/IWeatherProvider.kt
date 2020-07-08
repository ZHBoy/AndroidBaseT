package com.ychd.ycwwz.provider_library.router.common.provider

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.ychd.ycwwz.base_library.data.VideoInputBean
import com.ychd.ycwwz.provider_library.router.common.data.ShareWeatherTaskData

interface IWeatherProvider : IProvider {

    fun getDayWeather(context: Context, lon: Double, lat: Double,listener: DataCallBackListener)

    interface DataCallBackListener{

        fun getDataInfo(videoInputBean: VideoInputBean)
    }

    // 任务分享天气 data
    var shareWeatherTaskData: ShareWeatherTaskData
    // 任务分享天气
    fun getTaskShareWeatherPicDialog(activity: Activity,clickShareBlock:()->Unit): Dialog?
}