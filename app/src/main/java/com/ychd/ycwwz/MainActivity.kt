package com.ychd.ycwwz

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.base.CustomFragmentStatePagerAdapter
import com.ychd.ycwwz.base_library.data.AppUpdateBean
import com.ychd.ycwwz.base_library.event.main.MainActivitySwitchEvent
import com.ychd.ycwwz.base_library.mmkv.MMKVUtils
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.presenter.CommonPresenter
import com.ychd.ycwwz.base_library.widgets.AppUpdateDialog
import com.ychd.ycwwz.game_library.ui.GameFragment
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

@Route(path = RouterApi.MainLibrary.ROUTER_MAIN_URL)
class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private var mFragments: ArrayList<Fragment> = ArrayList()
    private var mAgreementDialog: AgreementDialog? = null

    private var mCommonPresenter: CommonPresenter? = null

    override fun resLayout(): Int {
        return R.layout.activity_main
    }

    override fun isRegisterEventBus(): Boolean {
        return true
    }

    override fun init() {

        //去掉背景色
        navigationBar.itemIconTintList = null
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
        //todo 跟随设备存储
        if (MMKVUtils.decodeInt("agreement") ?: 0 == 0) {
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
        mFragments.add(GameFragment.newInstance())
        mFragments.add(FragmentDemo2.newInstance())
        mFragments.add(FragmentDemo3.newInstance())

        val mainAdapter = CustomFragmentStatePagerAdapter(
            supportFragmentManager,
            //设置Fragment最大生命周期
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            mFragments, null
        )

        mainVp.adapter = mainAdapter
        mainVp.addOnPageChangeListener(this)

        mainVp.currentItem = 0
        mainVp.offscreenPageLimit = mFragments.size

    }

    override fun logic() {
        navigationBar?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_home -> {
                    val fragmentIndex = MainActivitySwitchEvent.HOME_FRAGMENT_INDEX
                    mainVp.currentItem = fragmentIndex

                    return@OnNavigationItemSelectedListener true
                }

                R.id.main_welfare -> {
                    val fragmentIndex = MainActivitySwitchEvent.WELFARE_FRAGMENT_INDEX
                    mainVp.currentItem = fragmentIndex

                    return@OnNavigationItemSelectedListener true
                }

                R.id.main_task -> {
                    val fragmentIndex = MainActivitySwitchEvent.TASL_FRAGMENT_INDEX
                    mainVp.currentItem = fragmentIndex

                    return@OnNavigationItemSelectedListener true
                }

                else -> {
                }
            }
            false
        }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        navigationBar.selectedItemId =
            navigationBar.menu.getItem(position).itemId
    }


    /**
     * 切换 fragment
     */
    @Subscribe
    fun switchFragment(mainActivitySwitchEvent: MainActivitySwitchEvent) {

        val fragmentIndex = mainActivitySwitchEvent.fragmentIndex
        mainVp.currentItem = fragmentIndex

        navigationBar.selectedItemId =
            navigationBar.menu.getItem(fragmentIndex).itemId
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
    }

}
