package com.ychd.ycwwz.base_library.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * @author zhou_hao
 * @date 2020-02-12
 * @description: activity 基类
 */
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        base()
        super.onCreate(savedInstanceState)

        setContentView(resLayout())
//        StatusToolUtils.setTranslucent(window, false)
        init()
        logic()

        if (isRegisterEventBus())
            EventBus.getDefault().register(this)

    }

    abstract fun resLayout(): Int

    abstract fun init()

    abstract fun logic()

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)

    }

    open fun base() {
        try {
            AppManager.instance.addActivity(this)
            this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 控制是否默认注册eventBus
     * true 是注册  false(默认)不注册
     */
    protected open fun isRegisterEventBus(): Boolean {
        return false
    }

    override fun onDestroy() {
        if (isRegisterEventBus())
            EventBus.getDefault().unregister(this)
        AppManager.instance.finishActivity(this)
        super.onDestroy()
    }

    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     *
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus

            if (v != null && isShouldHideSoftKeyBoard(v, ev)) {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    /**
     * 判断在什么情况下隐藏软键盘，点击EditText视图显示软键盘
     *
     * @param view  Incident event
     * @param event
     * @return
     */
    private fun isShouldHideSoftKeyBoard(view: View, event: MotionEvent): Boolean {
        if (view is EditText) {
            val l = intArrayOf(0, 0)
            view.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + view.getHeight()
            val right = left + view.getWidth()
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        // if the focus is EditText,ignore it;
        return false
    }


}