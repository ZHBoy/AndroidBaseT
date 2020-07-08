package com.ychd.ycwwz.base_library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle4.components.support.RxFragment
import com.umeng.analytics.MobclickAgent
import com.ychd.ycwwz.base_library.utils.TLog
import org.greenrobot.eventbus.EventBus

/**
 * @author zhou_hao
 * @date 2020-02-12
 * @description: Fragment基类(具备懒加载的)
 */
abstract class BaseLazyFragment : RxFragment() {

    private var viewInitiated = false // View是否创建
    private var isFirstVisible = true// 可见的次数

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId(), container, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if (isRegisterEventBus())
            EventBus.getDefault().unregister(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TLog.i("${javaClass.simpleName}---life-onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        this.viewInitiated = true
        if (isRegisterEventBus())
            EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        initData()
        TLog.i("${javaClass.simpleName}---life-onResume")
    }

    override fun onPause() {
        super.onPause()
        onInvisible()
        TLog.i("${javaClass.simpleName}--life-onPause")
    }

    private fun initData() {
        //第一次fragment 可见（数据加载放在onFirstVisible()）
        if (viewInitiated && isFirstVisible) {
            onFirstVisible()
            isFirstVisible = false
        }
        //fragment 可见
        else if (viewInitiated && !isFirstVisible) {
            onVisible()
        }

    }

    /**
     * fragment 可见
     */
    protected open fun onVisible() {
        TLog.i("${javaClass.simpleName}----onVisible")
        MobclickAgent.onPageStart(javaClass.simpleName) //统计页面("MainScreen"为页面名称，可自定义)
    }

    /**
     * fragment 不可见（包括切换到其他frangment、跳转到其他页面、切换到后台）
     */
    protected open fun onInvisible() {
        TLog.i("${javaClass.simpleName}----onInvisible")
        MobclickAgent.onPageEnd(javaClass.simpleName) //统计页面("MainScreen"为页面名称，可自定义)
    }

    /**
     * fragment 第一次可见
     */
    protected open fun onFirstVisible() {
        TLog.i("${javaClass.simpleName}----onFirstVisible")
        MobclickAgent.onPageStart(javaClass.simpleName) //统计页面("MainScreen"为页面名称，可自定义)
    }

    protected fun <T : View> getViewById(resId: Int): T {
        return this.view!!.findViewById(resId)
    }

    abstract fun layoutResId(): Int

    /**
     * 控制是否默认注册eventBus
     * true 是注册  false(默认)不注册
     */
    protected open fun isRegisterEventBus(): Boolean {
        return false
    }


}
