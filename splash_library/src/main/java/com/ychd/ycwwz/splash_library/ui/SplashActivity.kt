package com.ychd.ycwwz.splash_library.ui

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.ftd.livepermissions.LivePermissions
import com.ftd.livepermissions.PermissionResult
import com.ychd.ycwwz.splash_library.persenter.SplashPresenter
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.umeng.analytics.AnalyticsConfig
import com.ychd.ycwwz.adlibrary.baiduad.splash.SplashBuildBD
import com.ychd.ycwwz.adlibrary.gdt.splash.SplashBuildGDT
import com.ychd.ycwwz.adlibrary.ttad.ConstantTt
import com.ychd.ycwwz.adlibrary.ttad.splash.SplashBuildTt
import com.ychd.ycwwz.base_library.BaseApplication.Companion.appContext
import com.ychd.ycwwz.base_library.BuildConfig
import com.ychd.ycwwz.base_library.CommonDef
import com.ychd.ycwwz.base_library.base.AppManager
import com.ychd.ycwwz.base_library.base.BaseActivity
import com.ychd.ycwwz.base_library.data.ConfigBean
import com.ychd.ycwwz.base_library.extend.OnLazyClickListener
import com.ychd.ycwwz.base_library.extend.clickWithTrigger
import com.ychd.ycwwz.base_library.mvp.BasePresenter
import com.ychd.ycwwz.base_library.presenter.CommonPresenter
import com.ychd.ycwwz.base_library.utils.GlideUtils
import com.ychd.ycwwz.base_library.utils.TLog
import com.ychd.ycwwz.base_library.utils.timer.RxTimer
import com.ychd.ycwwz.base_library.utils.timer.TimerListener
import com.ychd.ycwwz.base_library.widgets.PermessonDialog
import com.ychd.ycwwz.provider_library.router.common.RouterApi
import com.ychd.ycwwz.splash_library.R
import com.ychd.ycwwz.splash_library.persenter.SplashContract
import com.ychd.ycwwz.splash_library.service.data.SplashBean
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.math.roundToInt

/**
 * 工程的入口
 */
@Route(path = RouterApi.SplashLibrary.ROUTER_MAIN_SPLASH_URL)
class SplashActivity : BaseActivity(), OnLazyClickListener, SplashContract.View {

    private val SKIP_TEXT = "%d s"

    /**
     * 记录拉取广告的时间
     */
    private var fetchSplashADTime: Long = 0

    override fun context(): RxAppCompatActivity = this@SplashActivity

    private var presenter: SplashContract.Presenter? = null

    override fun setPresenter(presenter: SplashContract.Presenter) {
        this.presenter = presenter
    }

    override fun resLayout(): Int = R.layout.activity_splash

    //兜底的倒计时（结束就跳过）
    private var rxTimerBottom: RxTimer? = null

    private var rxTimer: RxTimer? = null

    private var isAdsReturnSucess: Boolean = false


    private var splashBuildGDT: SplashBuildGDT? = null

    private var splashBuildTt: SplashBuildTt? = null

    private var splashBuildBD: SplashBuildBD? = null

    private var isGetPermissionJump = false//授权弹框的点击跳转

    var isAdJump = false//广告点击的跳转

    var isJump = false//开屏页点击图片跳转

    var isGDT = true
    var canJump = false

    var dataType = 0

    var jumpUrl = ""

    //是否重新拉取过广告(拉去的次数)
    private var reLoadNumber = 0

    private var mCommonPresenter: CommonPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        reLoadNumber = 0
        timerBottom()
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun init() {

        SplashPresenter(this)

        //todo 用户退出
        tvVersion?.text = "v${BuildConfig.VERSION_NAME}"
        tvChannel.text = AnalyticsConfig.getChannel(appContext)

        mCommonPresenter = CommonPresenter()
        mCommonPresenter?.getChannalAdIsOpen(this)//渠道广告是否开启
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (mCommonPresenter == null)
            mCommonPresenter = CommonPresenter()

        reLoadNumber = 0
        timerBottom()
    }

    override fun onResume() {
        super.onResume()
        TLog.i("splashActivityRes", "onResume")

        //处理点击开屏图片跳转回来之后的逻辑(正常来说栈中只有SplashActivity，但是SplashActivity中onResume方法在以前栈顶destory之前调用)
        if (isJump) {
            isJump = false
            var isStartMainActivity = true
            AppManager.instance.getStackList().map { ac ->
                val a = ac.localClassName
                if ("ui.main.MainActivity" == a) {
                    isStartMainActivity = false
                    return@map
                }
            }
            if (isStartMainActivity) {
                ARouter.getInstance().build(RouterApi.MainLibrary.ROUTER_MAIN_URL).navigation()
                finish()
            } else {
                finish()
            }
            return
        }

        //广告的点击回调
        if (isAdJump) {
            isAdJump = false
            next()
            return
        }

        if (isGetPermissionJump) {
            next()
            return
        }
        if (isGDT) {
            if (canJump) {
                next()
            }
            canJump = true
            return
        }
    }


    override fun logic() {
        splashGuideCl.setOnClickListener(this)
        checkPermission()
    }

    override fun onLazyClick(v: View) {
        when (v.id) {
            R.id.splashGuideCl -> {
                if (dataType == 0)
                    return
                rxTimer?.finish()
                presenter?.clickSpalshImage()
            }
        }
    }

    /**
     * 权限检查
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    @SuppressLint("CheckResult")
    private fun checkPermission() {
        LivePermissions(this).request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).observe(this, Observer {
            when (it) {
                is PermissionResult.Grant -> {  //权限允许
                    mCommonPresenter?.getConfig(
                        this,
                        object : BasePresenter.ResponseListener<ConfigBean> {
                            override fun getDataSuccess(dataBean: ConfigBean) {
                                appStatistics()
                            }

                            override fun getDataError() {
                                appStatistics()
                            }
                        })
                }
                is PermissionResult.Rationale -> {  //权限拒绝
                    appAdSplash()
                }
                is PermissionResult.Deny -> {   //权限拒绝，且勾选了不再询问
//                    it.permissions.forEach {s->
//                        println("deny:${s}")//被拒绝的权限
//                    }
                    PermessonDialog
                        .Builder(this)
                        .setTitle(resources.getString(R.string.text_not_get_permission))
                        .setCancelBtn("暂不开启", object : OnLazyClickListener {
                            override fun onLazyClick(v: View) {
                                appAdSplash()
                            }
                        })
                        .setConfirmBtn("去开启", object : OnLazyClickListener {
                            override fun onLazyClick(v: View) {
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                                isGetPermissionJump = true//控制跳转到应用中心回来后能
                            }

                        })
                        .build().show()
                }
            }
        })
    }

    private fun appAdSplash() {

        vBottom.visibility = View.INVISIBLE
        appLogoWithImage.visibility = View.INVISIBLE
        adFrame.visibility = View.VISIBLE
        appLogo.visibility = View.VISIBLE
        val random = (0 until 100).random()
        if (random <= CommonDef.ads_probability_page_baidu) {
            fetchBDSplashAD()
            timer()
        } else if (random > CommonDef.ads_probability_page_baidu && random < CommonDef.ads_probability_page_chuanshanjia) {
            fetchTTSplashAD()
        } else {
            //广点通传入的跳过按钮，必须是可见状态且不能设置点击事件 详见：https://developers.adnet.qq.com/doc/android/union/union_splash#%E6%8E%A5%E5%85%A5%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9
            fetchSplashAD()
        }
    }

    private fun fetchTTSplashAD() {
        skipView.visibility = View.INVISIBLE
        isGDT = false
        splashBuildTt =
            SplashBuildTt(
                this@SplashActivity,
                ConstantTt.SPLASH_SLOT_CODE_ID,
                object : SplashBuildTt.TTSplashListener {
                    override fun onSkip() {
                        next()
                    }

                    override fun onError() {
                        if (isReLoadAd()) {
                            next()
                        } else {
                            TLog.i("isReLoadAd", "头条拉广告失败22222222")
                            TLog.i("isReLoadAd", "头条拉广告失败-reLoadNumber=$reLoadNumber")
                            reLoadNumber++
                            fetchSplashAD()
                        }
                    }

                    override fun onAdTimeOver() {
                        next()
                    }

                    override fun onAdClicked() {
                        isAdJump = true
                        rxTimer?.finish()
                    }

                    override fun onSplashAdLoad(ad: TTSplashAd?) {
                        if (ad?.splashView == null) {
                            next()
                        } else {
                            adFrame.removeAllViews()
                            //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                            adFrame.addView(ad.splashView)
                            //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                            //ad.setNotAllowSdkCountdown();
                        }
                    }

                })
    }

    private fun fetchBDSplashAD() { // 默认请求http广告，若需要请求https广告，请设置AdSettings.setSupportHttps为true
        skipView.visibility = View.VISIBLE
        isGDT = false
        splashBuildBD = SplashBuildBD(this@SplashActivity,
            ConstantTt.SPLASH_ADS_BAIDU_CODE_ID, adFrame, object : SplashBuildBD.BDSplashListener {
                override fun onAdClicked() {
                    // 设置开屏可接受点击时，该回调可用
                    isAdJump = true
                    skipView?.text = "点击跳过"
                    rxTimer?.finish()
                }

                override fun onSplashAdLoad() {}

                override fun onError(s: String) {
                    next()
                    TLog.i("splashActivityRes", "onError" + s)
                }

                override fun onSkip() {}

            })
    }

    /**
     * 广点通
     */
    private fun fetchSplashAD() {
        TLog.i("splashActivityRes", "fetchSplashAD")
        skipView.visibility = View.VISIBLE
        isGDT = true
        fetchSplashADTime = System.currentTimeMillis()
        val random = (0 until 100).random()
        splashBuildGDT = SplashBuildGDT(
            this,
            adFrame,
            skipView,
            ConstantTt.SPLASH_ADS_GDT_CODE_ID,
            0,
            object :
                SplashBuildGDT.GDTSplashListener {

                override fun onAdClicked() {
                    TLog.i("splashActivityRes", "onAdClicked")
                    rxTimer?.finish()
                }

                override fun onSplashAdLoad() {

                }

                override fun onError() {
                    if (isReLoadAd()) {
                        canJump = true
                        nextGDT()
                    } else {
                        TLog.i("isReLoadAd", "广点通拉广告失败11111111")
                        TLog.i("isReLoadAd", "广点通拉广告失败reLoadNumber=$reLoadNumber")
                        reLoadNumber++
                        fetchTTSplashAD()
                    }
                }

                override fun onADTick(millisUntilFinished: Long) {
                    TLog.i("splashActivityRes", "onADTick$random")
                    if (random <= CommonDef.ad_skip_page_probability) {
                        skipView.isClickable = false
                        skipView.isFocusable = false
                    }
                    skipView.text = String.format(
                        SKIP_TEXT,
                        (millisUntilFinished / 1000f).roundToInt()
                    )
                }

                override fun onSkip() {
                    TLog.i("splashActivityRes", "onSkip")
                    nextGDT()
                }

            })
    }

    /**
     * 是否要重新拉去广告
     */
    private fun isReLoadAd(): Boolean {
        //说明重新拉取过 1可动态配置，但不宜太多
        return reLoadNumber >= 1
    }


    private fun next() {
        //判断是否是首次启动
        val activityNumber = AppManager.instance.getStackNumber()

        var isStartMainActivity = false
        //首次打开app
        if (activityNumber == 1) {
            isStartMainActivity = true
        }
        if (isStartMainActivity) {
            ARouter.getInstance().build(RouterApi.MainLibrary.ROUTER_MAIN_URL).navigation()
        }
        finish()
    }

    private fun timer() {
        if (rxTimer == null) {
            rxTimer = RxTimer()
        }
        rxTimer?.countDown(5, object : TimerListener() {

            @SuppressLint("SetTextI18n")
            override fun onSuccess(value: Long) {
                skipView.text = "$value s"
            }

            override fun onCompleted() {
                if (isAdsReturnSucess) {
                    return
                }

                rxTimer?.finish()
                next()
            }
        })
    }

    /**
     * 倒计时结束直接跳过
     */
    private fun timerBottom() {
        if (rxTimerBottom == null) {
            rxTimerBottom = RxTimer()
        }
        rxTimerBottom?.countDown(8, object : TimerListener() {

            @SuppressLint("SetTextI18n")
            override fun onSuccess(value: Long) {
            }

            override fun onCompleted() {
                next()
            }
        })
    }

    /**
     * 请求开屏页接口
     */
    private fun appStatistics() {
        presenter?.getSplashDateApi(object : SplashContract.SplashResponseListener<SplashBean> {
            override fun onSuccess(splash: SplashBean) {
                if (splash.data?.isNotEmpty() == true) {
                    //生成一个随机数
                    val random = (0 until splash.data.size).random()
                    GlideUtils.instance.loadImageNoDefault(vBottom, splash.data[random].pageImage)
                    vBottom.visibility = View.VISIBLE
                    appLogoWithImage.visibility = View.VISIBLE
                    adFrame.visibility = View.INVISIBLE
                    appLogo.visibility = View.INVISIBLE
                    skipView.visibility = View.VISIBLE
                    dataType = splash.data[random].type
                    jumpUrl = splash.data[random].jumpUrl
                    timer()
                    //当时自己开平是才设置点击事件，原因是广点通跳过按钮不可以设置点击
                    skipView.clickWithTrigger {
                        next()
                    }
                } else {
                    appAdSplash()
                }
            }

            override fun onError() {
                appAdSplash()
            }
        })
    }

    override fun onDestroy() {
        splashBuildTt?.removeData()
        splashBuildGDT?.onDestrory()
        splashBuildBD?.destroy()
        rxTimer?.finish()
        rxTimer = null
        rxTimerBottom?.finish()
        rxTimerBottom = null
        mCommonPresenter?.destoryData()
        mCommonPresenter = null

        super.onDestroy()
    }

    //屏蔽返回键
    override fun onBackPressed() {
        // super.onBackPressed();
    }

    fun nextGDT() {
        if (canJump) {
            next()
        } else {
            canJump = true
        }
    }

    override fun onPause() {
        super.onPause()
        canJump = false
        TLog.i("splashActivityRes", "onPause")
    }
}
