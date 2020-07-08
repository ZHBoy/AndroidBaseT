package com.ychd.ycwwz

import com.alibaba.android.arouter.launcher.ARouter
import com.ychd.ycwwz.base_library.base.BaseLazyFragment
import com.ychd.ycwwz.base_library.utils.TLog
import com.ychd.ycwwz.provider_library.router.common.IntentData
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import kotlinx.android.synthetic.main.fragment_layout.*

class FragmentDemo3 : BaseLazyFragment() {
    override fun layoutResId(): Int = R.layout.fragment_layout

    override fun onFirstVisible() {
        super.onFirstVisible()
        toLogin.text = "FragmentDemo3"
        toLogin.setOnClickListener {
            ARouter.getInstance()
                .build(RouterApi.UserCenter.ROUTER_USER_LOGIN_URL)
//            .withInt(IntentData.UserCenterIntentData.intentLogin, 0)
                .withString(IntentData.UserCenterIntentData.intentLogin, "17802118931")
                .navigation()

        }
        TLog.i("FragmentDemo3:onFirstVisible")
    }

    override fun onVisible() {
        super.onVisible()
        TLog.i("FragmentDemo3:onVisible")
    }

    override fun onInvisible() {
        super.onInvisible()
        TLog.i("FragmentDemo3:onInvisible")
    }

    companion object {
        fun newInstance() = FragmentDemo3()
    }

}
