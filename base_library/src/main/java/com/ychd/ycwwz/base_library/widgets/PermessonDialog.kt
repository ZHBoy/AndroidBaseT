package com.ychd.ycwwz.base_library.widgets

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.ychd.ycwwz.base_library.BaseApplication.Companion.appContext
import com.ychd.ycwwz.base_library.R
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.utils.DisplayUtil

/**
 * @author : YMH
 * @date : 2020/3/10
 * @description :权限检测弹框
 **/
open class PermessonDialog : Dialog {

    private var tvBindingPhoneTitle:TextView?=null
    private var btBindingPhoneLeft: Button? = null
    private var btBindingPhoneRight: Button? = null
    private var mOnBackListener: OnBackListener? = null

    interface OnBackListener {
        fun back()
    }

    constructor(context: Context) : super(context, R.style.SystemDialog) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?)
            : super(context, cancelable, cancelListener)

    private fun init() {
        val window = this.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.permeson_dialog)

        setCanceledOnTouchOutside(false)
        window.setGravity(Gravity.CENTER)
        // 隐藏标题栏
        window.setBackgroundDrawableResource(R.color.transparent)
        val params = window.attributes
        params.width = DisplayUtil.getScreenWidth(appContext) / 8 * 7
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params

        tvBindingPhoneTitle=findViewById(R.id.tvBindingPhoneTitle)
        btBindingPhoneLeft = findViewById(R.id.btBindingPhoneLeft)
        btBindingPhoneRight = findViewById(R.id.btBindingPhoneRight)

    }

    override fun onBackPressed() {
        mOnBackListener?.back()
        super.onBackPressed()
    }

    class Builder(private val context: Context) {

        private var systemDialog: PermessonDialog? = null

        private var title = ""
        private var cancel = ""
        private var confirm = ""
        private var view: View? = null
        private var cancelable = true


        private var cancelLis: View.OnClickListener? = null
        private var confirmLis: View.OnClickListener? = null


        fun setCancelAble(isCancel: Boolean): Builder {
            this.cancelable = isCancel
            return this
        }

        fun setTitle(text: String): Builder {
            this.title = text
            return this
        }


        fun setCancelBtn(text: String, listener: OnLazyClickListener?): Builder {
            this.cancel = text
            this.cancelLis = listener
            return this
        }

        fun setConfirmBtn(text: String, listener: OnLazyClickListener?): Builder {
            this.confirm = text
            this.confirmLis = listener
            return this
        }



        /**
         * 设置一个view到dialog的中间部位
         */
        fun setView(view: View): Builder {
            this.view = view
            return this
        }

        fun build(): PermessonDialog {
            systemDialog = PermessonDialog(context)

            systemDialog!!.tvBindingPhoneTitle!!.text = title
            systemDialog!!.btBindingPhoneLeft!!.text = cancel
            systemDialog!!.btBindingPhoneRight!!.text = confirm
            systemDialog!!.setCancelable(cancelable)
            systemDialog!!.btBindingPhoneRight!!.setOnClickListener { v ->
                if (confirmLis != null) {
                    confirmLis!!.onClick(v)
                }
                systemDialog!!.dismiss()

            }
            systemDialog!!.btBindingPhoneLeft!!.setOnClickListener { v ->
                if (cancelLis != null) {
                    cancelLis!!.onClick(v)
                }
                systemDialog!!.dismiss()
            }

            return systemDialog!!
        }
    }
}
