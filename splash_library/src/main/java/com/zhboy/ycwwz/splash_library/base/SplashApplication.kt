package com.zhboy.ycwwz.splash_library.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhboy.ycwwz.base_library.BaseApplication
import com.zhboy.ycwwz.base_library.utils.TLog
import com.zhboy.ycwwz.provider_library.router.common.RouterApi
import com.zhboy.ycwwz.provider_library.router.common.provider.ISplashApplicationProvider
import com.zhboy.ycwwz.splash_library.ui.SplashActivity
import org.jetbrains.anko.intentFor

@Route(path = RouterApi.SplashLibrary.ROUTER_SPLASH_APPLICATION)
class SplashApplication : BaseApplication(), ISplashApplicationProvider {
    override fun init(context: Context?) {

    }

    /**
     * Application 初始化
     */

    override fun initModuleApp(application: Application) {
        addLifecycleListener(application)
    }

    /**
     * 所有 Application 初始化后的自定义操作
     */
    override fun initModuleData(application: Application) {

    }


    // activity 的数量
    var mActivityCount = 0
        private set

    // activity 从前台切换到后台的时间
    var mFore2BackTime: Long = 0L
        private set

    // activity 从前台切换到后台 再切换到前台时 需要跳转到 SplashActivity 的时间间隔
    var mDiffTime4ReFore2Splash: Long = 1000 * 3
        private set

    /**
     * 添加 activity 生命周期监听
     */
    private fun addLifecycleListener(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {
                mActivityCount++
                if (mActivityCount == 1) {
                    TLog.i("说明从后台回到了前台")

                    val time = System.currentTimeMillis() - mFore2BackTime
                    TLog.i("time: $time")
                    if (activity != null
                        && (activity is SplashActivity).not()
                        && time >= mDiffTime4ReFore2Splash
                    ) {
                        activity.startActivity(
                            activity.intentFor<SplashActivity>()
                        )
                    }
                }
            }

            override fun onActivityStopped(activity: Activity?) {
                TLog.i("onActivityStopped")
                mActivityCount--
                if (mActivityCount == 0) {
                    TLog.i("说明从前台回到了后台")
                    mFore2BackTime = System.currentTimeMillis()
                }
            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }
        })
    }

}