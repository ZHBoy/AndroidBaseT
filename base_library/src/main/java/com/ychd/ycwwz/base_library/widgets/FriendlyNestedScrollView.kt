package com.ychd.ycwwz.base_library.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.annotation.Nullable
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

/**
 * 处理ScrollView滑动冲突
 */
class FriendlyNestedScrollView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    //它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
    private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var mDownX: Float = 0F
    private var mDownY: Float = 0F
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (noScroll) return false
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            mDownY = ev.y
            mDownX = ev.x
        }
        if (ev.actionMasked == MotionEvent.ACTION_MOVE) {
            val diffY = mDownY - ev.y
            if (canScrollVertically(1) &&
                (abs(diffY) > scaledTouchSlop && abs(diffY) > abs(mDownX - ev.x))
            ) {
                return true
            } else {
                return super.onInterceptTouchEvent(ev)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(arg0: MotionEvent?): Boolean {
        return !noScroll && super.onTouchEvent(arg0)
    }

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    private var noScroll = false
}