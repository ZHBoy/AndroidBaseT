package com.ychd.ycwwz.adlibrary.gdt.splash;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.ychd.ycwwz.base_library.utils.TLog;

/**
 * @author: YMH
 * @date: 2020/03/25
 * @description: 开屏广告创建基础类
 **/
public class SplashBuildGDT {

    private Activity activity;
    private ViewGroup adContainer;
    private SplashAD splashAD;
    private GDTSplashListener listener;

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param posId         广告位ID
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    public SplashBuildGDT(Activity activity, ViewGroup adContainer, View skipContainer,
                          String posId, int fetchDelay, GDTSplashListener listener) {
        this.listener = listener;
        this.activity = activity;
        this.adContainer = adContainer;
        if (activity != null) {
            loadGDTSplashAd(skipContainer, posId, fetchDelay);
        }

    }

    private void loadGDTSplashAd(View skipContainer, String posId, int fetchDelay) {

        splashAD = new SplashAD(activity, skipContainer, posId, new SplashADListener() {

            @Override
            public void onADDismissed() {

                TLog.INSTANCE.i("splashBuidGDT", "onADDismissed");
                listener.onSkip();
            }

            @Override
            public void onNoAD(AdError adError) {
                TLog.INSTANCE.i("splashBuidGDT", adError.getErrorMsg() + adError.getErrorCode());
                listener.onError();
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {
                TLog.INSTANCE.i("splashBuidGDT", "onADClicked");
                listener.onAdClicked();
            }

            @Override
            public void onADTick(long millisUntilFinished) {
                TLog.INSTANCE.i("splashBuidGDT", "onADTick");
                listener.onADTick(millisUntilFinished);
            }

            @Override
            public void onADExposure() {
                TLog.INSTANCE.i("splashBuidGDT", "onADExposure");
            }

            @Override
            public void onADLoaded(long millisUntilFinished) {
                listener.onSplashAdLoad();
            }
        }, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }

    public void onDestrory() {
        if (splashAD != null) {
            splashAD = null;
        }

    }

    public interface GDTSplashListener {
        void onSkip();//点击跳过

        void onError();//没有广告

        void onAdClicked();//开屏广告被点击

        void onSplashAdLoad();//开屏广告请求成功

        void onADTick(long millisUntilFinished);

    }
}
