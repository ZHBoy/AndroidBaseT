package com.ychd.ycwwz.user_library.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ychd.ycwwz.base_library.IntentDataDef
import com.ychd.ycwwz.base_library.base.AppManager
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.utils.StringUtils
import com.ychd.ycwwz.base_library.utils.ToastUtil
import com.ychd.ycwwz.base_library.wechat.WXAPISingleton
import com.ychd.ycwwz.base_library.widgets.common.CommonDialog
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.user_library.R
import kotlinx.android.synthetic.main.activity_login_with_phone.*

/**
 *@author : HaoBoy
 *@date : 2019/4/12
 *@description :用户手机号登录
 **/
@Route(path = RouterApi.UserCenter.ROUTER_USER_LOGIN_WITH_PHONE_URL)
class LoginWithPhoneActivity : BaseActivity(), OnLazyClickListener {

    private var dialog: CommonDialog? = null

    override fun resLayout(): Int = R.layout.activity_login_with_phone

    override fun init() {

    }

    override fun logic() {
        ivBack.setOnClickListener(this)
        llWxLogin.setOnClickListener(this)
        tvHelper.setOnClickListener(this)
        tvLoginWithWx.setOnClickListener(this)
        tvJump.setOnClickListener(this)

        etLoginPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etLoginPhone.text.length == 11) {
                    llWxLogin.setBackgroundResource(R.drawable.backgroud_e74723_radius_4)
                } else {
                    llWxLogin.setBackgroundResource(R.drawable.backgroud_gray_radius_4)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    override fun onBackPressed() {
        AppManager.instance.finishActivity("LoginActivity")
        finish()
//        super.onBackPressed()
    }

    override fun onLazyClick(v: View) {
        when (v.id) {

            //点击跳过
            R.id.tvJump -> {
                AppManager.instance.finishActivity("LoginActivity")
                finish()
            }

            R.id.ivBack -> {
                AppManager.instance.finishActivity("LoginActivity")
                finish()
            }

            R.id.tvLoginWithWx -> {
                finish()
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

            R.id.llWxLogin -> {
                val phone = etLoginPhone.text.toString()
                if (phone.isEmpty()) {
                    ToastUtil.showShortToast("手机号不能为空")
                    return
                }
                if (phone.length < 11) {
                    ToastUtil.showShortToast("请输入11位手机号")
                    return
                }
                if (StringUtils.isPhoneLegal(phone).not()) {
                    ToastUtil.showShortToast("手机号不合法")
                    return
                }
                ARouter.getInstance()
                    .build(RouterApi.UserCenter.ROUTER_USER_LOGIN_URL)
                    .withString(IntentDataDef.INTENT_PHONE_TO_VERIFY, phone)
                    .navigation()
            }
        }
    }
}