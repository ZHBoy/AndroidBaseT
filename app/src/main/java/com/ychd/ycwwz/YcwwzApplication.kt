package com.ychd.ycwwz

import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mobads.AdView
import com.qq.e.comm.managers.GDTADManager
import com.qq.e.comm.managers.setting.GlobalSetting
import com.tencent.mmkv.MMKV
import com.ychd.ycwwz.adlibrary.ttad.ConstantTt
import com.ychd.ycwwz.adlibrary.ttad.TTAdManagerHolder
import com.ychd.ycwwz.base_library.BaseApplication
import com.ychd.ycwwz.base_library.mmkv.MMKVUtils
import com.ychd.ycwwz.provider_library.router.common.provider.ISplashApplicationProvider

/**
 * @author: zhou_hao
 * @date: 2020-02-11
 * @description:
 **/
class YcwwzApplication: BaseApplication(){
    override fun onCreate() {
        super.onCreate()
        AdView.setAppSid(this, "")

        //穿山甲广告初始化
        TTAdManagerHolder.init(this)

        ARouter.getInstance()
            .navigation(ISplashApplicationProvider::class.java)
            .initModuleApp(this)

        // 通过调用此方法初始化 SDK。如果需要在多个进程拉取广告，每个进程都需要初始化 SDK。
        GDTADManager.getInstance().initWith(this, ConstantTt.APP_GDT_ID)
//        GlobalSetting.setChannel(1)
        GlobalSetting.setEnableMediationTool(true)
        AdView.setAppSid(this, "b6050625")


        //腾讯mmkv
        MMKV.initialize(this)
        MMKVUtils.getInstance()

    }
}