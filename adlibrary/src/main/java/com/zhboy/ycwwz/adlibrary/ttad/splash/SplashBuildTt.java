package com.zhboy.ycwwz.adlibrary.ttad.splash;

import android.app.Activity;
import android.view.View;

import androidx.annotation.MainThread;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.zhboy.ycwwz.adlibrary.ttad.TTAdManagerHolder;

/**
 * @author: ZhouH
 * @date: 2019/10/26
 * @description: 开屏广告创建基础类
 **/
public class SplashBuildTt {

    private TTAdNative mTTAdNative;
    private static final int AD_TIME_OUT = 3000;
    private Activity activity;
    private String codeId;
    private TTAdManager ttAdManager;
    private TTSplashListener listener;


    public SplashBuildTt(Activity activity, String codeId, TTSplashListener listener) {
        this.listener = listener;
        this.codeId = codeId;
        this.activity = activity;
        if (activity != null) {
//            TTAdManager var2 = TTAdManagerFactory.getInstance(activity, false);
//            var2.setAppId(ConstantTt.APP_ID);

            ttAdManager = TTAdManagerHolder.get();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            ttAdManager.requestPermissionIfNecessary(activity);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            mTTAdNative = ttAdManager.createAdNative(activity.getApplicationContext());
            loadTTSplashAd();
        }
    }

    public void removeData() {
        if (mTTAdNative != null) {
            mTTAdNative = null;
        }
        if (ttAdManager != null) {
            ttAdManager = null;
        }
    }

    /**
     * 加载开屏广告
     */
    private void loadTTSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();

        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                listener.onError();
            }

            @Override
            @MainThread
            public void onTimeout() {

                if (activity.isDestroyed() || activity.isFinishing())
                    return;

//                showToast("开屏广告加载超时");
                listener.onError();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {

                if (activity.isDestroyed() || activity.isFinishing())
                    return;

                listener.onSplashAdLoad(ad);
//                Log.d(TAG, "开屏广告请求成功");

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
//                        showToast("开屏广告点击");
                        listener.onAdClicked();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
//                        showToast("开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
//                        showToast("开屏广告跳过");
                        listener.onSkip();
                    }

                    @Override
                    public void onAdTimeOver() {
//                        showToast("开屏广告倒计时结束");
                        listener.onAdTimeOver();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                                showToast("下载中...");
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                            showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                            showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);

    }

    public interface TTSplashListener {
        void onSkip();//点击跳过

        void onError();//没有广告

        void onAdTimeOver();//开屏广告倒计时结束

        void onAdClicked();//开屏广告被点击

        void onSplashAdLoad(TTSplashAd ad);//开屏广告请求成功

    }
}
