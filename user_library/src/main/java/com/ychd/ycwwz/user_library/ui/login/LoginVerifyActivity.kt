package com.ychd.ycwwz.user_library.ui.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent.KEYCODE_DEL
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ychd.ycwwz.base_library.IntentDataDef
import com.ychd.ycwwz.base_library.base.AppManager
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.constants.AccessManager
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.extend.fromJson
import com.ychd.ycwwz.base_library.extend.gson
import com.ychd.ycwwz.base_library.network.retrofit.callback.HttpOnNextListener
import com.ychd.ycwwz.base_library.network.retrofit.exception.ApiErrorModel
import com.ychd.ycwwz.base_library.network.retrofit.http.HttpManager
import com.ychd.ycwwz.base_library.network.retrofit.http.RequestOption
import com.ychd.ycwwz.base_library.utils.*
import com.ychd.ycwwz.base_library.utils.timer.RxTimer
import com.ychd.ycwwz.base_library.utils.timer.TimerListener
import com.ychd.ycwwz.base_library.wechat.WXAPISingleton
import com.ychd.ycwwz.base_library.widgets.common.CommonDialog
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.user_library.R
import com.ychd.ycwwz.user_library.data.BindPhoneBean
import com.ychd.ycwwz.user_library.service.AccountService
import kotlinx.android.synthetic.main.activity_login_with_phone_verify.*

/**
 *@author : HaoBoy
 *@date : 2019/4/12
 *@description :用户手机号登录-验证码页面
 **/
@Route(path = RouterApi.UserCenter.ROUTER_USER_LOGIN_VERIFY_URL)
class LoginVerifyActivity : BaseActivity(), View.OnClickListener, TextWatcher {

    private var rxTimer: RxTimer? = null

    private var phone: String? = null

    private var dialog: CommonDialog? = null
    override fun resLayout(): Int = R.layout.activity_login_with_phone_verify

    @SuppressLint("SetTextI18n")
    override fun init() {

        phone = intent.getStringExtra(IntentDataDef.INTENT_PHONE_TO_VERIFY)

        if (phone.isNullOrEmpty().not()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvVerifyTip.text = Html.fromHtml(
                    "已发送短信验证码至<font color='#C43413'>$phone</font><br/>输入4位验证码即可登录",
                    Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                tvVerifyTip.text = "已发送短信验证码至$phone\n输入4位验证码即可登录"
            }
            //发送验证码
            sendCode(phone!!)
        }
    }

    override fun logic() {
        //监听文字改变
        etVerify1.addTextChangedListener(this)
        etVerify2.addTextChangedListener(this)
        etVerify3.addTextChangedListener(this)
        etVerify4.addTextChangedListener(this)
        tvRefreshCode.setOnClickListener(this)
        tvHelper.setOnClickListener(this)
        ivBack.setOnClickListener(this)

        etVerify1.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_DEL) {
                etVerify1.setBackgroundResource(R.drawable.verification_edit_bg_normal)
            }
            false
        }
        etVerify2.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_DEL && etVerify2.text.isNullOrEmpty()) {
                etVerify2.setBackgroundResource(R.drawable.verification_edit_bg_normal)
                etVerify1.requestFocus()
            }
            false
        }
        etVerify3.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_DEL && etVerify3.text.isNullOrEmpty()) {
                etVerify3.setBackgroundResource(R.drawable.verification_edit_bg_normal)
                etVerify2.requestFocus()
            }
            false
        }
        etVerify4.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_DEL && etVerify4.text.isNullOrEmpty()) {
                etVerify4.setBackgroundResource(R.drawable.verification_edit_bg_normal)
                etVerify3.requestFocus()
            }
            false
        }

        Handler().postDelayed({
            CommonUtils.instance.showKeyboard(this, etVerify1)
        }, 200)

        //粘贴的监听
        etVerify1.setOnPasteTwoCallback {
            val copyText = SoftInputUtil.getClipboard(this@LoginVerifyActivity)
            if (copyText.length == 4 && StringUtils.isNumeric(copyText)) {
                etVerify1.setText(copyText[0].toString())
                etVerify2.setText(copyText[1].toString())
                etVerify3.setText(copyText[2].toString())
                etVerify4.setText(copyText[3].toString())
                etVerify4.setSelection(1)
            } else {
                ToastUtil.showShortToast("请先复制验证码！")
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvRefreshCode -> {
                //发送验证码
                sendCode(phone!!)
            }
            R.id.tvHelper -> {
                dialog = CommonDialog.Builder(this)
                    .setTitle("")
                    .setMessage("客服微信已经复制\n快打开微信粘贴吧")
                    .setCancelAble(false)
                    .setCancelBtn("知道了", null)
                    .setConfirmBtn("去加好友", object : OnLazyClickListener {
                        override fun onLazyClick(v: View) {
                            WXAPISingleton.WXAPI_INSTANCE.openWXApp()
                        }

                    }).build()
                dialog?.show()
            }
            R.id.ivBack -> {
                finish()
            }
        }
    }

    /**
     * 监听输入框输入
     */
    override fun afterTextChanged(s: Editable?) {
        controlEtInput()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    /**
     * 控制下一个输入框输入
     */
    private fun controlEtInput() {
        val code =
            etVerify1.text.toString() + etVerify2.text.toString() + etVerify3.text.toString() + etVerify4.text.toString()
        if (code.length == 4) {
            userBindPhone(code)
        } else {
            if (etVerify1.text.isNullOrBlank()) {
                etVerify1.requestFocus()
                return
            } else {
                etVerify1.setBackgroundResource(R.drawable.verification_edit_bg_focus)
                etVerify1.setSelection(1)
            }
            if (etVerify2.text.isNullOrBlank()) {
                etVerify2.requestFocus()
                return
            } else {
                etVerify2.setBackgroundResource(R.drawable.verification_edit_bg_focus)
                etVerify2.setSelection(1)
            }

            if (etVerify3.text.isNullOrBlank()) {
                etVerify3.requestFocus()
                etVerify1.setSelection(1)
                return
            } else {
                etVerify3.setBackgroundResource(R.drawable.verification_edit_bg_focus)
                etVerify3.setSelection(1)
            }

            if (etVerify4.text.isNullOrBlank()) {
                etVerify4.requestFocus()
                etVerify1.setSelection(1)
                return
            } else {
                etVerify4.setBackgroundResource(R.drawable.verification_edit_bg_focus)
                etVerify4.setSelection(1)
            }
        }
    }

    private fun timer() {
        if (rxTimer == null) {
            rxTimer = RxTimer()
        }
        tvNoReceiveVerifyTime.visibility = View.VISIBLE
        tvNoVerifyTimeTip.visibility = View.VISIBLE
        tvRefreshCode.visibility = View.GONE

        rxTimer?.countDown(60, object : TimerListener() {

            override fun onSuccess(value: Long) {
                tvNoReceiveVerifyTime.text = value.toString()
            }

            override fun onCompleted() {
                tvNoReceiveVerifyTime.visibility = View.GONE
                tvNoVerifyTimeTip.visibility = View.GONE
                tvRefreshCode.visibility = View.VISIBLE
            }
        })
    }

    /**
     * 发送手机验证码
     */
    private fun sendCode(phone: String) {
        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = false
            })
            doHttpDeal(
                this@LoginVerifyActivity,
                createService(AccountService::class.java).getVerifyCode(phone),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        timer()
                    }
                }
            )
        }
    }

    /**
     * 绑定手机号
     */
    private fun userBindPhone(verifyCode: String) {
        if (phone.isNullOrEmpty()) {
            return
        }

        HttpManager.instance().apply {
            setOption(RequestOption().apply {
                isShowProgress = true
            })
            doHttpDeal(
                this@LoginVerifyActivity,
                createService(AccountService::class.java).userBindPhone(
                    AccessManager.getUserUid(), phone!!, verifyCode
                ),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        if (json.isBlank()) return

                        val resultEntity: BindPhoneBean = gson.fromJson(json)
                        if (resultEntity.data != null && resultEntity.state == 1) {
                            AppManager.instance.finishActivity("LoginWithPhoneActivity")
                            AppManager.instance.finishActivity("LoginActivity")
                            finish()
                        }
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        finish()
                    }
                })
        }
    }

    override fun onDestroy() {
        rxTimer?.finish()
        super.onDestroy()
    }

}