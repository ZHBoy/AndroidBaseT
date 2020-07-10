package com.zhboy.ycwwz.adlibrary.baiduad.splash;

import android.app.Activity;
import android.widget.FrameLayout;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashLpCloseListener;

/**
 * @author: YMH
 * @date: 2020/02/11
 * @description: 开屏广告创建基础类
 **/
public class SplashBuildBD {

    private Activity activity;
    private String codeId;
    private FrameLayout frameLayout;
    private SplashAd bgSplashAd;
    private BDSplashListener listener;

    public SplashBuildBD(Activity activity, String codeId, FrameLayout frameLayout, BDSplashListener listener) {
        this.listener = listener;
        this.codeId = codeId;
        this.activity = activity;
        this.frameLayout=frameLayout;
        if (activity != null) {
            loadBDSplashAd();
        }

    }

    public void destroy() {
        if (bgSplashAd != null) {
            bgSplashAd.destroy();
        }
    }

    /**
     * 加载开屏广告
     */
    private void loadBDSplashAd() {

        // 增加lp页面关闭回调，不需要该回调的继续使用原来接口就可以
        SplashLpCloseListener splashLpCloseListener= new SplashLpCloseListener() {

            @Override
            public void onAdPresent() {
                listener.onSplashAdLoad();
            }

            @Override
            public void onAdDismissed() {
                listener.onSkip();
            }

            @Override
            public void onAdFailed(String s) {
                listener.onError(s);
            }

            @Override
            public void onAdClick() {
                listener.onAdClicked();
            }

            @Override
            public void onLpClosed() {

            }

        };

        AdSettings.setSupportHttps(false);

        bgSplashAd = new SplashAd(activity, frameLayout, splashLpCloseListener, codeId, true);
    }

    public interface BDSplashListener {
        void onSkip();//点击跳过

        void onError(String message);//没有广告

        void onAdClicked();//开屏广告被点击

        void onSplashAdLoad();//开屏广告请求成功

    }
}
