package com.zhboy.ycwwz.base_library.widgets

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import com.zhboy.ycwwz.base_library.BaseApplication.Companion.appContext
import org.xml.sax.XMLReader

class SizeLabel(private val size: Int) : Html.TagHandler {
    private var startIndex = 0
    private var stopIndex = 0

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag.toLowerCase() == "size") {
            if (opening) {
                startIndex = output.length
            } else {
                stopIndex = output.length
                output.setSpan(
                    AbsoluteSizeSpan(dip2px(size.toFloat())),
                    startIndex,
                    stopIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    companion object {

        fun dip2px(dpValue: Float): Int {
            val scale = appContext!!.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}