package com.ychd.ycwwz.user_library.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.event.login.LoginEvent
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.utils.CustomAnimatorUtils
import com.ychd.ycwwz.base_library.utils.MyLinkedMovementMethod
import com.ychd.ycwwz.base_library.utils.StatusToolUtils
import com.ychd.ycwwz.base_library.wechat.WXAPISingleton
import com.ychd.ycwwz.base_library.widgets.common.CommonDialog
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.user_library.R
import com.ychd.ycwwz.web_library.ui.CommonWebActivity
import com.ychd.ycwwz.web_library.webview.WebDataDef
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor

/**
 *@author : HaoBoy
 *@date : 2019/4/12
 *@description :登录页
 **/
@Route(path = RouterApi.UserCenter.ROUTER_USER_LOGIN_URL)
class LoginActivity : BaseActivity(), LoginContract.View, OnLazyClickListener {

    private var dialog: CommonDialog? = null

    private val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    private var presenter: LoginContract.Presenter? = null

    override fun context(): RxAppCompatActivity = this@LoginActivity
    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun resLayout(): Int = R.layout.activity_login

    //注册eventBus
    override fun isRegisterEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginBackEvent(loginData: LoginEvent) {
        loginData.wx_code.let { code ->
            if (code.isNotBlank()) {
                presenter?.userLogin(code, null)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun init() {
        LoginPresenter(this)

        val text = "登录抽奖即表明同意《有财惠生活用户服务协议》和《有财惠生活用户隐私政策》"
        val spannableString = SpannableString(text)

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                CommonWebActivity.startCommonWebActivity(
                    this@LoginActivity,
                    WebDataDef.WEB_FOR_USER_SERVICES_AGREEMENT,
                    true
                )
                tvLoginWithWxRule.highlightColor =
                    ContextCompat.getColor(this@LoginActivity, android.R.color.transparent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, 9, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                CommonWebActivity.startCommonWebActivity(
                    this@LoginActivity, WebDataDef.WEB_FOR_PRIVACY_POLICY,
                    true
                )
                tvLoginWithWxRule.highlightColor =
                    ContextCompat.getColor(this@LoginActivity, android.R.color.transparent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }, 24, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 当点击非富文本区域时，让TextView的父容器去执行点击事件
        tvLoginWithWxRule.movementMethod = MyLinkedMovementMethod.getInstance()
        tvLoginWithWxRule.text = spannableString

        CustomAnimatorUtils.scaleShandongO1(llWxLogin, Animation.INFINITE)
    }

    override fun logic() {
        ivBack.setOnClickListener(this)
        tvLoginWithPhone.setOnClickListener(this)
        tvHelper.setOnClickListener(this)
        llWxLogin.setOnClickListener(this)
        tvLoginWithWxRule.setOnClickListener(this)
    }

    override fun onLazyClick(v: View) {
        when (v.id) {
            R.id.ivBack -> {
                finish()
            }

            R.id.tvLoginWithPhone -> {
                startActivity(this.intentFor<LoginWithPhoneActivity>())
            }

            R.id.tvHelper -> {
                dialog = CommonDialog.Builder(this@LoginActivity)
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
            R.id.llWxLogin -> {
                checkPermission()
            }
        }
    }

    /**
     * 去登录或者授权
     */
    private fun toLogin() {
        try {
            regToWx()
        } catch (e: SecurityException) {
            finish()
        }
    }

    /**
     * 权限检查
     */
    @SuppressLint("CheckResult")
    private fun checkPermission() {
        rxPermissions.requestEachCombined(
            Manifest.permission.READ_PHONE_STATE
        )
            .subscribe { permission ->
                when {
                    permission.granted -> {
                        toLogin()
                    }
                    else -> //选择不再询问 需要去设置
                    {
                        dialog = CommonDialog.Builder(this@LoginActivity)
                            .setCancelAble(false)
                            .setTitle("温馨提示")
                            .setMessage(resources.getString(R.string.text_not_get_permission))
                            .setConfirmBtn("去开启", object : OnLazyClickListener {
                                override fun onLazyClick(v: View) {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                    dialog?.dismiss()
                                }
                            }).build()
                        dialog?.show()
                    }
                }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
        dialog = null
    }

    private fun regToWx() {
        loginingTv.visibility = View.VISIBLE
        val req = SendAuth.Req()
        req.transaction = System.currentTimeMillis().toString()  //transaction字段用与唯一标示一个请求
        req.scope = "snsapi_userinfo"//应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
        //用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        req.state = ""

        //调用api接口，发送数据到微信
        WXAPISingleton.WXAPI_INSTANCE.sendReq(req)
    }

}