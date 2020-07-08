package com.ychd.ycwwz.base_library

import android.content.Context
import android.os.Build
import android.webkit.WebView
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.bun.miitmdid.core.JLibrary
import com.bun.miitmdid.core.MdidSdkHelper
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.ychd.ycwwz.base_library.base.CrashHandler
import com.ychd.ycwwz.base_library.utils.CommonUtils
import com.ychd.ycwwz.base_library.utils.MiitHelper
import org.jetbrains.anko.toast

/**
 * @author zhou_hao
 * @date 2020-07-06
 * @description: Application base类
 */
open class BaseApplication : MultiDexApplication() {

    companion object {

        var appContext: Context? = null
        // 是否是调试模式
        var debugging: Boolean = true

        @JvmField
        var oaid: String = ""

        fun getOaid(): String {
            return oaid
        }
    }

    override fun onCreate() {
        super.onCreate()
        // UncaughtExceptionHandler 在友盟 bugly 前初始化
        CrashHandler.getInstance(this)

        appContext = applicationContext

        //9.0要为工程进程配置webview的数据目录
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = CommonUtils.instance.getProcessName(applicationContext)
            if ("com.ychd.ycwwz" != processName) {//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName)
            }
        }
        if (debugging) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
            // 打印日志的时候打印线程堆栈
            ARouter.printStackTrace()
            toast("debug")
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)

        JPushInterface.setDebugMode(debugging)
        JPushInterface.init(this)

        setUMInfo()
        try {
            JLibrary.InitEntry(this)
            MdidSdkHelper.InitSdk(this, true, MiitHelper(MiitHelper.AppIdsUpdater { ids ->
                //根方法中,我们如果只需要oaid,则只获取oaid即可
                // String oaid=_supplier.getOAID();
                oaid = ids
            }))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 友盟配置
     */
    private fun setUMInfo() {
        /**
         *
         * 有鞥呢基础配置
         * 参数1:上下文，必须的参数，不能为空。
         * 参数2:【友盟+】 AppKey，非必须参数，如果Manifest文件中已配置AppKey，该参数可以传空，则使用Manifest中配置的AppKey，否则该参数必须传入。
         * 参数3:【友盟+】 Channel，非必须参数，如果Manifest文件中已配置Channel，该参数可以传空，则使用Manifest中配置的Channel，否则该参数必须传入，Channel命名请详见Channel渠道命名规范。
         * 参数4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机。
         * 参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空。
         */
        UMConfigure.init(
            this,
            "5e56166d895cca3b1e0000d0",
            null,
            UMConfigure.DEVICE_TYPE_PHONE,
            null
        )

        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        UMConfigure.setProcessEvent(true)
        // 选用AUTO页面采集模式
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        //友盟log
        UMConfigure.setLogEnabled(debugging)
        //友盟的错误统计（开发模式下关闭）
        MobclickAgent.setCatchUncaughtExceptions(!debugging)

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}