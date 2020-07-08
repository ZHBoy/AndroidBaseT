package com.ychd.ycwwz.adlibrary.ttad.video;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.ychd.ycwwz.adlibrary.ttad.ConstantTt;
import com.ychd.ycwwz.adlibrary.ttad.TTAdManagerHolder;

import java.util.List;

/**
 * @author ZhouH
 * @date 2019/10/25
 * @description: 视频流广告基础配置类
 */
public class DrawNativeVideoTt {
    private TTAdNative mTTAdNative;
    private Activity activity;
    private TTDrawFeedAd currentAd;

    public void init(Activity activity) {
        this.activity = activity;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
        //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
        TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
    }

    public void loadDrawNativeAd(final TTDrawVideo.TTDrawListener listener) {
        //step3:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ConstantTt.VIDEO_SLOT_CODE_ID)//广告位Id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step4:请求广告,对请求回调的广告作渲染处理
        mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int code, String message) {

                if (activity.isDestroyed() || activity.isFinishing())
                    return;

                Toast.makeText(activity, "加载视频出错了,请上滑观看下一个吧！", Toast.LENGTH_LONG).show();
                Log.d("loadDrawFeedAd", "视频广告错误code:" + code + "  message:" + message);
            }

            //视频流广告加载完成，可以播放了
            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> ads) {
                if (activity.isDestroyed() || activity.isFinishing())
                    return;

                if (ads == null || ads.isEmpty()) {
                    Toast.makeText(activity, "加载视频失败了,请上滑观看下一个吧！", Toast.LENGTH_LONG).show();
                    return;
                }
                TTDrawFeedAd ad = ads.get(0);
                ad.setActivityForDownloadApp(activity);
                //点击监听器必须在getAdView之前调
                ad.setCanInterruptVideoPlay(true);
//                ad.setPauseIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.jz_click_play_selector), 60);
                listener.onVideoComplete(ad);
                currentAd = ad;
                // Bitmap adLogo = ad.getAdLogo();
            }
        });
    }

    /**
     * 广告与用户交互的回调
     *
     * @param ad
     * @param container
     * @param clickViews
     * @param creativeViews
     */
    public void initAdViewAndAction(TTDrawFeedAd ad, FrameLayout container, List<View> clickViews, List<View> creativeViews) {

        ad.registerViewForInteraction(container, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {

            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {

            }

            @Override
            public void onAdShow(TTNativeAd ad) {

            }
        });


    }

    public boolean isReady() {
        return currentAd != null;
    }

    public TTDrawFeedAd getAd() {
        return currentAd;
    }
}
