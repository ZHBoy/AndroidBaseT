package com.ychd.ycwwz

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ychd.weather.AgreementDialog
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.base.CustomFragmentStatePagerAdapter
import com.ychd.ycwwz.base_library.data.AppUpdateBean
import com.ychd.ycwwz.base_library.event.main.MainActivitySwitchEvent
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.presenter.CommonPresenter
import com.ychd.ycwwz.base_library.utils.SPUtils
import com.ychd.ycwwz.base_library.widgets.AppUpdateDialog
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_bottom.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

@Route(path = RouterApi.MainLibrary.ROUTER_MAIN_URL)
class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener, OnLazyClickListener {

    private var mFragments: ArrayList<Fragment> = ArrayList()
    // 上一次选择的 tab
    private var mPreSelectTab: View? = null
    private var mAgreementDialog: AgreementDialog? = null

    private var mCommonPresenter: CommonPresenter? = null

    override fun resLayout(): Int {
        return R.layout.activity_main
    }

    override fun isRegisterEventBus(): Boolean {
        return true
    }

    override fun init() {
        //设置底部栏
        setTabLayout()
        initApi()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initApi()
    }

    private fun initApi() {
        if (mCommonPresenter == null)
            mCommonPresenter = CommonPresenter()

//        JPushInterface.getRegistrationID(appContext)//极光推送的ID

        appUpdate()
        getAppConfig()
    }

    override fun onResume() {
        super.onResume()
        if (SPUtils.getObjectForKey(SPUtils.BROKER_SHARED_DATA_NOT_CLEAR, "agreement", 0) == 0) {
            if (mAgreementDialog?.isShowing == true) {
                return
            }
            if (mAgreementDialog == null) {
                mAgreementDialog = AgreementDialog.Builder(this).build()
            }
            mAgreementDialog?.show()
        }
    }

    /**
     * 版本更新逻辑
     */
    private fun appUpdate() {
        mCommonPresenter?.appUpdate(this, object : BasePresenter.ResponseListener<AppUpdateBean> {
            override fun getDataSuccess(dataBean: AppUpdateBean) {
                val versionCode = dataBean.data!!.versionCode
                //说明大于当前版本,弹出更新弹框
                if (versionCode.isNullOrEmpty().not() && versionCode!!.toInt() > BuildConfig.VERSION_CODE
                ) {
                    AppUpdateDialog(this@MainActivity, dataBean).show()
                }
            }

            override fun getDataError() {
            }
        })
    }

    /**
     * 获取配置信息
     */
    private fun getAppConfig() {
        mCommonPresenter?.getConfig(this, null)
    }

    private fun setTabLayout() {
        mFragments.add(FragmentDemo.newInstance())
        mFragments.add(FragmentDemo2.newInstance())
        mFragments.add(FragmentDemo3.newInstance())
        mFragments.add(FragmentDemo4.newInstance())

        val mainAdapter = CustomFragmentStatePagerAdapter(
            supportFragmentManager,
            //设置Fragment最大生命周期
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            mFragments, null
        )

        mainVp.adapter = mainAdapter
        mainVp.addOnPageChangeListener(this)

        mainVp.currentItem = 0
        rl_main_weather.isSelected = true
        mainVp.offscreenPageLimit = mFragments.size

        rl_main_weather.setOnClickListener(this)
        rl_main_welfare.setOnClickListener(this)
        rl_main_person.setOnClickListener(this)
        rl_activity.setOnClickListener(this)

    }

    override fun logic() {
        //是否显示福利小红点
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onLazyClick(v: View) {
        when (v.id) {
            R.id.rl_main_weather -> {
                mainVp.currentItem = MainActivitySwitchEvent.WEATHER_FRAGMENT_INDEX
            }
            R.id.rl_main_welfare -> {
                mainVp.currentItem = MainActivitySwitchEvent.WELFARE_FRAGMENT_INDEX
            }
            R.id.rl_main_person -> {
                mainVp.currentItem = MainActivitySwitchEvent.MINE_FRAGMENT_INDEX
            }
            R.id.rl_activity -> {
                mainVp.currentItem = MainActivitySwitchEvent.ACTIVITY_FRAGMENT_INDEX
            }
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (mPreSelectTab != null) {
            mPreSelectTab!!.isSelected = false
        }
        when (position) {
            MainActivitySwitchEvent.WEATHER_FRAGMENT_INDEX -> {
                rl_main_weather.isSelected = true
                mPreSelectTab = rl_main_weather
            }
            MainActivitySwitchEvent.WELFARE_FRAGMENT_INDEX -> {
                rl_main_welfare.isSelected = true
                mPreSelectTab = rl_main_welfare
            }
            MainActivitySwitchEvent.MINE_FRAGMENT_INDEX -> {
                rl_main_person.isSelected = true
                mPreSelectTab = rl_main_person
            }
            MainActivitySwitchEvent.ACTIVITY_FRAGMENT_INDEX -> {
                rl_activity.isSelected = true
                mPreSelectTab = rl_activity
            }
            else -> {
            }
        }

    }

    /**
     * 切换 fragment
     */
    @Subscribe
    fun switchFragment(mainActivitySwitchEvent: MainActivitySwitchEvent) {
        if (mPreSelectTab != null) {
            mPreSelectTab!!.isSelected = false
        }
        val fragmentIndex = mainActivitySwitchEvent.fragmentIndex
        mainVp.currentItem = fragmentIndex
        val childView = llBottomRooter.getChildAt(fragmentIndex)
        childView.isSelected = true
        mPreSelectTab = childView
    }

    //记录用户首次点击返回键的时间
    private var mExitTime = System.currentTimeMillis()  //为当前系统时间，单位：毫秒

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime < 800) {
            this.finish()   //关闭本活动页面
        } else {
            toast("再按返回键退出！")
            mExitTime = System.currentTimeMillis()   //这里赋值最关键，别忘记
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgreementDialog?.dismiss()
        mAgreementDialog = null
        mPreSelectTab = null
    }

}
