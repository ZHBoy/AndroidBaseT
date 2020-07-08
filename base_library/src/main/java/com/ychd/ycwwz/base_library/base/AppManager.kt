package com.ychd.ycwwz.base_library.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*

/*
    Activity管理器
 */
class AppManager private constructor() {

    private val activityStack: Stack<Activity> = Stack()

    companion object {
        val instance: AppManager by lazy { AppManager() }
    }

    /**
     * 获取当前栈内activity
     */
    fun getStackList(): Stack<Activity> {
        return activityStack
    }

    /**
     * 获取当前栈内activity个数
     */
    fun getStackNumber(): Int {
        return activityStack.size
    }

    /*
        Activity入栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /*
        Activity出栈
     */
    fun finishActivity(activity: Activity) {
        activity.finish()
        activityStack.remove(activity)
    }

    /*
       Activity出栈
    */
    fun finishActivity(activityName: String) {
        for (activity in activityStack) {
            if (activity.componentName.className.contains(".$activityName")) {
                activity.finish()
                activityStack.remove(activity)
                return
            }
        }
    }

    /*
        获取当前栈顶
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /*
        清理栈
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    /*
        退出应用程序
     */
    fun exitApp(context: Context) {
        finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        System.exit(0)
    }

    fun currentActivityCount(activity:Activity):Int{
       return activityStack.map {
            it.javaClass.simpleName
        }.count {
            it == activity.javaClass.simpleName
        }
    }
}
