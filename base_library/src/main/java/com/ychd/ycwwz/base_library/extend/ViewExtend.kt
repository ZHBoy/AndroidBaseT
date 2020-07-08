package com.ychd.ycwwz.base_library.extend

import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout

class ViewExtend {

    companion object {
        /**
         * 测量曲线
         */
        fun measureCurvePath(pointList: List<Point>, lineSmoothness: Float): Path {
            val path: Path = Path()
            var prePreviousPointX = Float.NaN
            var prePreviousPointY = Float.NaN
            var previousPointX = Float.NaN
            var previousPointY = Float.NaN
            var currentPointX = Float.NaN
            var currentPointY = Float.NaN
            var nextPointX: Float
            var nextPointY: Float
            val lineSize = pointList.size
            for (valueIndex in 0 until lineSize) {
                if (java.lang.Float.isNaN(currentPointX)) {
                    val point = pointList[valueIndex]
                    currentPointX = point.x.toFloat()
                    currentPointY = point.y.toFloat()
                }
                if (java.lang.Float.isNaN(previousPointX)) { //是否是第一个点
                    if (valueIndex > 0) {
                        val point = pointList[valueIndex - 1]
                        previousPointX = point.x.toFloat()
                        previousPointY = point.y.toFloat()
                    } else { //是的话就用当前点表示上一个点
                        previousPointX = currentPointX
                        previousPointY = currentPointY
                    }
                }
                if (java.lang.Float.isNaN(prePreviousPointX)) { //是否是前两个点
                    if (valueIndex > 1) {
                        val point = pointList[valueIndex - 2]
                        prePreviousPointX = point.x.toFloat()
                        prePreviousPointY = point.y.toFloat()
                    } else { //是的话就用当前点表示上上个点
                        prePreviousPointX = previousPointX
                        prePreviousPointY = previousPointY
                    }
                }
                // 判断是不是最后一个点了
                if (valueIndex < lineSize - 1) {
                    val point = pointList[valueIndex + 1]
                    nextPointX = point.x.toFloat()
                    nextPointY = point.y.toFloat()
                } else { //是的话就用当前点表示下一个点
                    nextPointX = currentPointX
                    nextPointY = currentPointY
                }
                if (valueIndex == 0) { // 将Path移动到开始点
                    path.moveTo(currentPointX, currentPointY)
                } else { // 求出控制点坐标
                    val firstDiffX = currentPointX - prePreviousPointX
                    val firstDiffY = currentPointY - prePreviousPointY
                    val secondDiffX = nextPointX - previousPointX
                    val secondDiffY = nextPointY - previousPointY
                    val firstControlPointX: Float =
                        previousPointX + lineSmoothness * firstDiffX
                    val firstControlPointY: Float =
                        previousPointY + lineSmoothness * firstDiffY
                    val secondControlPointX: Float =
                        currentPointX - lineSmoothness * secondDiffX
                    val secondControlPointY: Float =
                        currentPointY - lineSmoothness * secondDiffY
                    //画出曲线
                    path.cubicTo(
                        firstControlPointX,
                        firstControlPointY,
                        secondControlPointX,
                        secondControlPointY,
                        currentPointX,
                        currentPointY
                    )
                }
                // 更新值,
                prePreviousPointX = previousPointX
                prePreviousPointY = previousPointY
                previousPointX = currentPointX
                previousPointY = currentPointY
                currentPointX = nextPointX
                currentPointY = nextPointY
            }
            return path
        }

        /**
         * 将 layout 文件转换成 Bitmap
         */
        fun layoutResDrawToBitmap(
            layoutInflater: LayoutInflater, @LayoutRes layoutRes: Int,
            width: Int,
            height: Int
        ): Bitmap {
            val view: View = layoutInflater.inflate(layoutRes, null, false)
            val measuredWidth = View.MeasureSpec.makeMeasureSpec(
                width,
                View.MeasureSpec.EXACTLY
            )
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(
                height,
                View.MeasureSpec.EXACTLY
            )
            view.measure(measuredWidth, measuredHeight)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            return view.draw2Bitmap {
                drawColor(Color.WHITE)
            }
        }
    }
}

/**
 * 将 view 转换成 Bitmap
 */
fun View.draw2Bitmap(
    config: Bitmap.Config = Bitmap.Config.ARGB_8888,
    block: (Canvas.() -> Unit)? = null
): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
    }
    val bitmap = Bitmap.createBitmap(width, height, config)
    Canvas(bitmap).apply {
        block?.invoke(this)
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(this)
    }
    return bitmap
}

/**
 * 将 NestedScrollView 转换成 Bitmap
 */
fun NestedScrollView.draw2Bitmap(
    config: Bitmap.Config = Bitmap.Config.ARGB_8888,
    block: (Canvas.() -> Unit)? = null
): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
    }
    var h = 0
    var bitmap: Bitmap? = null
    for (i in 0 until this.childCount) {
        h += this.getChildAt(i).height
    }
    bitmap = Bitmap.createBitmap(this.width, h, config)
    val canvas = Canvas(bitmap)
    block?.invoke(canvas)
    this.draw(canvas)
    return bitmap
}

/**
 * 控制appbar的滑动
 * @param canScroll true 允许滑动 false 禁止滑动
 */
fun AppBarLayout.canAppBarLayoutScroll(canScroll: Boolean) {
    var childView = this.getChildAt(0)
    var mAppBarParams: AppBarLayout.LayoutParams =
        childView.layoutParams as AppBarLayout.LayoutParams
    if (canScroll) {
        mAppBarParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;
    } else {
        mAppBarParams.scrollFlags = 0
    }
    childView.layoutParams = mAppBarParams
}