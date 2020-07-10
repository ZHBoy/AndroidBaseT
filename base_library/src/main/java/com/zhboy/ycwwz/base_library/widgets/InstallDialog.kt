package com.zhboy.ycwwz.base_library.widgets

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.zhboy.ycwwz.base_library.R
import com.zhboy.ycwwz.base_library.utils.DisplayUtil

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :弹框提示的封装(只有两个按钮没有底部按钮)
 **/
open class InstallDialog : Dialog {


    private var btnInstallOK: Button? = null

    constructor(context: Context) : super(context, R.style.ads_dialog) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?)
            : super(context, cancelable, cancelListener)

    private fun init() {
        val window = this.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.install_dialog)

        setCanceledOnTouchOutside(false)
        window.setGravity(Gravity.CENTER)
        // 隐藏标题栏
        window.setBackgroundDrawableResource(R.color.transparent)
        val params = window.attributes
        params.width = DisplayUtil.getScreenWidth(context) / 7* 6
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        btnInstallOK = findViewById(R.id.btnInstallOK)
    }

    class Builder(private val context: Context) {

        private var systemDialog: InstallDialog? = null

        private var cancelable = true

        private var confirmLis: View.OnClickListener? = null

        fun setConfirmBtn( listener: View.OnClickListener?): Builder {
            this.confirmLis = listener
            return  this
        }

        fun build(): InstallDialog {
            systemDialog = InstallDialog(context)

            systemDialog!!.setCancelable(cancelable)
            systemDialog!!.btnInstallOK!!.setOnClickListener { v ->
                if (confirmLis != null) {
                    confirmLis!!.onClick(v)
                }
                systemDialog!!.dismiss()

            }
            return systemDialog!!
        }
    }
}
