package com.ychd.ycwwz.provider_library.router.common.provider

import android.app.Application
import com.alibaba.android.arouter.facade.template.IProvider


interface ISplashApplicationProvider : IProvider {

    /**
     * Application 初始化
     */
    fun initModuleApp(application: Application)

    /**
     * 所有 Application 初始化后的自定义操作
     */
    fun initModuleData(application: Application)
}