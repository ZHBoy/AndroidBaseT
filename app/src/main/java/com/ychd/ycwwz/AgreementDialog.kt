package com.ychd.weather

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.*
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.ychd.ycwwz.R
import com.ychd.ycwwz.base_library.IntentDataDef
import com.ychd.ycwwz.base_library.utils.DisplayUtil
import com.ychd.ycwwz.base_library.utils.MyLinkedMovementMethod
import com.ychd.ycwwz.base_library.utils.SPUtils
import com.ychd.ycwwz.base_library.utils.ToastUtil
import com.ychd.ycwwz.provider_library.router.common.RouterApi


/**
 * @author : YMH
 * @date : 2020/1/10
 * @description :用户协议弹框提示的封装
 **/
open class AgreementDialog : Dialog {

    private var tvAgreementTip: TextView? = null
    private var btnAgreementContent: Button? = null
    private var btnAgreementRefuse: TextView? = null


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

        setContentView(R.layout.dialog_agreement)

        setCanceledOnTouchOutside(false)
        window.setGravity(Gravity.CENTER)
        // 隐藏标题栏
        window.setBackgroundDrawableResource(R.color.transparent)
        val params = window.attributes
        params.width = DisplayUtil.getScreenWidth(context) / 10 * 9
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params

        tvAgreementTip = findViewById(R.id.tvAgreementTip)
        btnAgreementContent = findViewById(R.id.btnAgreementContent)
        btnAgreementRefuse = findViewById(R.id.btnAgreementRefuse)

    }


    class Builder(private val context: Context) {

        private var systemDialog: AgreementDialog? = null

        private var cancelable = false


        fun setCancelAble(isCancel: Boolean): Builder {
            this.cancelable = isCancel
            return this
        }


        fun build(): AgreementDialog {
            systemDialog =
                AgreementDialog(context)

            systemDialog!!.setCancelable(cancelable)
            systemDialog!!.btnAgreementContent!!.setOnClickListener { v ->
                //本地存储
                SPUtils.setObject(SPUtils.BROKER_SHARED_DATA_NOT_CLEAR, "agreement", 1)
                systemDialog!!.dismiss()
            }

            systemDialog!!.btnAgreementRefuse!!.setOnClickListener { v ->
                ToastUtil.showLongToastCenter("请您阅读并同意本协议后，才能使用")
            }

            val text = "《有财天气用户服务协议》和《有财天气用户隐私政策》"
            val spannableString = SpannableString(text)

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ARouter.getInstance().build(RouterApi.WebLibrary.ROUTER_WEB_BASE_URL)
                        .withString(
                            IntentDataDef.INTENT_WEB_COMMON_URL_KEY, IntentDataDef.USERAGREEMENT
                        ).navigation()
                    systemDialog?.tvAgreementTip?.highlightColor =
                        ContextCompat.getColor(context, R.color.transparent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    ARouter.getInstance().build(RouterApi.WebLibrary.ROUTER_WEB_BASE_URL)
                        .withString(
                            IntentDataDef.INTENT_WEB_COMMON_URL_KEY, IntentDataDef.PRIVACYPOLICY
                        ).navigation()
                    systemDialog?.tvAgreementTip?.highlightColor =
                        ContextCompat.getColor(context, android.R.color.transparent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }

            }, 13, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 当点击非富文本区域时，让TextView的父容器去执行点击事件
            systemDialog?.tvAgreementTip?.movementMethod = MyLinkedMovementMethod.getInstance()
            systemDialog?.tvAgreementTip?.text = spannableString
            return systemDialog!!
        }
    }
}
