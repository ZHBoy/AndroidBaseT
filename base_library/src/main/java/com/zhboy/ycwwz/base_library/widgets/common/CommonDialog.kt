package com.zhboy.ycwwz.base_library.widgets.common

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zhboy.ycwwz.base_library.R
import com.zhboy.ycwwz.base_library.extend.OnLazyClickListener
import com.zhboy.ycwwz.base_library.utils.CustomAnimatorUtils
import com.zhboy.ycwwz.base_library.utils.DisplayUtil
import com.zhboy.ycwwz.base_library.utils.StringUtils

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :弹框提示的封装 基本满足所有情况。
 **/
open class CommonDialog : Dialog {

    private var tvTitle: TextView? = null
    private var tvContent: TextView? = null
    private var btRight: TextView? = null
    private var btRightLl: LinearLayout? = null
    private var btRightIv: ImageView? = null
    private var btLeft: Button? = null
    private var commonCloseIv: ImageView? = null
    private var ivTopImg: ImageView? = null

    private var mOnBackListener: OnBackListener? = null

    interface OnBackListener {
        fun back()
    }

    constructor(context: Context) : super(context, R.style.SystemDialog) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    protected constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    )
            : super(context, cancelable, cancelListener)

    private fun init() {
        val window = this.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.common_dialog_layout)

        setCanceledOnTouchOutside(false)
        window.setGravity(Gravity.CENTER)
        // 隐藏标题栏
        window.setBackgroundDrawableResource(R.color.transparent)
        val params = window.attributes
        params.width = DisplayUtil.getScreenWidth(context) / 9 * 8
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params

        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        btRight = findViewById(R.id.btRight)
        btRightLl = findViewById(R.id.btRightLl)
        btRightIv = findViewById(R.id.btRightIv)
        btLeft = findViewById(R.id.btLeft)
        commonCloseIv = findViewById(R.id.commonCloseIv)
        ivTopImg = findViewById(R.id.ivTopImg)

    }

    override fun onBackPressed() {
        mOnBackListener?.back()
        super.onBackPressed()
    }

    class Builder(private val context: Context) {

        private var systemDialog: CommonDialog? = null

        private var title = ""
        private var msg = ""
        private var cancel = ""
        private var confirm = ""
        private var bottom = ""
        private var view: View? = null
        private var cancelable = true
        private var isClickClose = true//是否点击按钮直接关闭
        //是否展示地步去邀请的按钮
        private var isShowBottomButton = true
        private var isShowTopClose = true
        //顶部图片
        private var topImgRes: Int = 0
        private var isShowBottomIv = false//低部按钮左侧图片是否展示
        private var isShowAnimator = false//是否按钮动画


        private var cancelLis: View.OnClickListener? = null
        private var confirmLis: View.OnClickListener? = null
        private var bottomLis: View.OnClickListener? = null
        private var closeLis: View.OnClickListener? = null

        fun setShowBottomIv(isShowBottomIv: Boolean): Builder {
            this.isShowBottomIv = isShowBottomIv
            return this
        }

        fun setClickClose(isClickClose: Boolean): Builder {
            this.isClickClose = isClickClose
            return this
        }

        //顶不图片
        fun setTopImage(topImgRes: Int): Builder {
            this.topImgRes = topImgRes
            return this
        }

        fun setTopClose(isShowTopClose: Boolean): Builder {
            this.isShowTopClose = isShowTopClose
            return this
        }

        fun setCancelAble(isCancel: Boolean): Builder {
            this.cancelable = isCancel
            return this
        }

        fun setTitle(text: String): Builder {
            this.title = text
            return this
        }

        fun setMessage(text: String): Builder {
            this.msg = text
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

        fun setConfirmBtnAnimator(isShowAnimator: Boolean): Builder {
            this.isShowAnimator = isShowAnimator
            return this
        }

        fun setCloseLst(listener: OnLazyClickListener?): Builder {
            this.closeLis = listener
            return this
        }

        fun setBottom(
            tvBottomTip: String,
            isShowBottomButton: Boolean,
            listener: OnLazyClickListener?
        ): Builder {
            this.bottom = tvBottomTip
            this.bottomLis = listener
            this.isShowBottomButton = isShowBottomButton
            return this
        }

        /**
         * 设置一个view到dialog的中间部位
         */
        fun setView(view: View): Builder {
            this.view = view
            return this
        }

        fun build(): CommonDialog {
            systemDialog = CommonDialog(context)
            if (TextUtils.isEmpty(title)) {
                systemDialog!!.tvTitle!!.visibility = View.GONE
            }
            if (TextUtils.isEmpty(msg)) {
                systemDialog!!.tvContent!!.visibility = View.GONE
            }
            if (TextUtils.isEmpty(cancel)) {
                systemDialog!!.btLeft!!.visibility = View.GONE
            }
            if (TextUtils.isEmpty(confirm)) {
                systemDialog!!.btRight!!.visibility = View.GONE
            }
            if (isShowTopClose) {
                systemDialog!!.commonCloseIv!!.visibility = View.VISIBLE
            }

            if (topImgRes != 0) {
                systemDialog!!.ivTopImg!!.setImageResource(topImgRes)
            }
            if (isShowBottomIv) {
                systemDialog!!.btRightIv!!.visibility = View.VISIBLE
            }
            if (isShowAnimator) {
                CustomAnimatorUtils.scaleShandongO1(systemDialog!!.btRightLl!!, Animation.INFINITE)
            }

            StringUtils.setHtmlTextView(systemDialog!!.tvTitle!!, title)
            StringUtils.setHtmlTextView(systemDialog!!.tvContent!!, msg)

            systemDialog!!.btLeft!!.text = cancel
            systemDialog!!.btRight!!.text = confirm
            systemDialog!!.setCancelable(cancelable)
            systemDialog!!.btRightLl!!.setOnClickListener { v ->
                if (confirmLis != null) {
                    confirmLis!!.onClick(v)
                }
                if (isClickClose) {
                    systemDialog!!.dismiss()
                }
            }
            systemDialog!!.btLeft!!.setOnClickListener { v ->
                if (cancelLis != null) {
                    cancelLis!!.onClick(v)
                }
                if (isClickClose) {
                    systemDialog!!.dismiss()
                }
            }

            systemDialog!!.commonCloseIv!!.setOnClickListener { v ->
                if (closeLis != null) {
                    closeLis!!.onClick(v)
                }
                systemDialog!!.dismiss()
            }

            return systemDialog!!
        }
    }
}
