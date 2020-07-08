package com.ychd.ycwwz.adlibrary.ttad.video;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.ychd.ycwwz.adlibrary.ttad.video.DrawNativeVideoTt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouH
 * @date 2019/10/25
 * @description:视频广告
 */
public class TTDrawVideo {
    DrawNativeVideoTt dawVideo;
    Activity activity;

    public TTDrawVideo(Activity activity) {
//        TTAdManager var2 = TTAdManagerFactory.getInstance(activity, false);
//        var2.setAppId(ConstantTt.APP_ID);
        dawVideo = new DrawNativeVideoTt();
        dawVideo.init(activity);
    }

    public void showAd(TTDrawFeedAd ad, FrameLayout container, View clickView, View creativeView) {
        List<View> clickViews = new ArrayList<>();
        clickViews.add(clickView);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(creativeView);
        dawVideo.initAdViewAndAction(ad, container, clickViews, creativeViews);

    }

    public TTDrawFeedAd getAd() {
        return dawVideo.getAd();
    }

    public void loadAd(TTDrawListener listener) {
//        TTAdManager var2 = TTAdManagerFactory.getInstance(activity, false);
//        var2.setAppId(ConstantTt.APP_ID);
        dawVideo.loadDrawNativeAd(listener);
    }

    public boolean isReady() {
        return dawVideo.isReady();
    }

    public interface TTDrawListener {
        void onVideoComplete(TTDrawFeedAd ad);
    }
}
