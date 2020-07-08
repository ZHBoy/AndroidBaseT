package com.ychd.ycwwz.base_library.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * @author ZhouH
 * @date 2019/11/9
 * @description: 定义全局view的工具里
 */
class CustomRootViewUtils {

    companion object {
        val instance: CustomRootViewUtils by lazy { CustomRootViewUtils() }
    }


    /**
     * 显示View
     */
    fun addARootView(view: View, activity: Activity?) {
        /*Activity不为空并且没有被释放掉*/
        if (activity != null && !activity.isFinishing) {
            /*获取Activity顶层视图,并添加自定义View*/
            ((activity.window.decorView) as ViewGroup).addView(view)
        }
    }

    /**
     * 移除一个全局View
     */
    fun removeARootView(view: View, activity: Activity?) {
        /*Activity不为空并且没有被释放掉*/
        if (activity != null && !activity.isFinishing) {
            /*获取Activity顶层视图*/
            val root = (activity.window.decorView) as ViewGroup
            /*如果Activity中存在View对象则删除*/
            if (root.indexOfChild(view) != -1) {
                /*从顶层视图中删除*/
                root.removeView(view)
            }
        }
    }
}