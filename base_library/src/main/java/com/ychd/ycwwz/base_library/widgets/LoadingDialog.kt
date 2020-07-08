package com.ychd.ycwwz.base_library.widgets

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ychd.ycwwz.base_library.R

/**
 * @author : HaoBoy
 * @date : 2019/4/10
 * @description :加载框的封装
 **/
object LoadingDialog {

    private var dialog: Dialog? = null

    fun show(context: Context, msg: String) {

        //当context为空或者context不为activity的时候
        if (context == null || (context is Activity).not()|| (context as Activity).isFinishing) {
            return
        }

        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
            dialog = null
        }
        dialog =
            createLoadingDialog(context, msg)
        dialog!!.show()
    }

    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
            dialog = null
        } else if (dialog != null) {
            dialog = null
        }
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    private fun createLoadingDialog(context: Context, msg: String): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.base_dialog_progress, null)
        val layout = view.findViewById(R.id.dialog_view) as FrameLayout// 加载布局
        val text = view.findViewById(R.id.loadingTv) as TextView
        if (TextUtils.isEmpty(msg)) text.visibility = View.GONE
        else text.text = msg
        val loadingImage = view.findViewById(R.id.loadingIv) as ImageView
        val animationDrawable = AnimationUtils.loadAnimation(context, R.anim.load_animation)
        loadingImage.startAnimation(animationDrawable)
        val mDialog = Dialog(context, R.style.loading_dialog)
        mDialog.setContentView(
            layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        mDialog.setCanceledOnTouchOutside(false)
        return mDialog
    }

}