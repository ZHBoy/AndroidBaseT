package com.ychd.ycwwz.adlibrary.ttad.reward;

import android.app.Activity;
import android.provider.Settings;
import com.bytedance.sdk.openadsdk.*;
import com.ychd.ycwwz.adlibrary.ttad.TTAdManagerHolder;

/**
 * @author ZhouH
 * @date 2019/10/25
 * @description: 激励视频初始化调用相关
 */

public class RewardBuildTt {
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private Activity activity;
    private TTReward.TTRewardListener listener;
    private String postId;

    public void initReward(Activity activity) {
        this.activity = activity;
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(activity.getApplicationContext());
    }

    public void releaseAd(){
        mTTAdNative = null;
        mttRewardVideoAd = null;
    }

    private String getAndroidId() {
        String androidId = Settings.System.getString(activity.getContentResolver(), Settings.System.ANDROID_ID);
        if (androidId == null || androidId.isEmpty()) {
            androidId = "YC_DEFAULT_ANDROID_ID";
        }
        return androidId;
    }

    private boolean mHasShowDownloadActive = false;

    public void loadAd() {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(postId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("")//非服务器回调模式或者不需要sdk透传可以传空字符串
                .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                //TToast.show(activity, message);
                if (activity.isDestroyed() || activity.isFinishing())
                    return;
                listener.onNoAD();
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                //TToast.show(activity, "rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {

                if (activity.isDestroyed() || activity.isFinishing())
                    return;

                //TToast.show(activity, "rewardVideoAd loaded");
                listener.onVideoDownloadSuccess();
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        // TToast.show(activity, "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        //TToast.show(activity, "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        //TToast.show(activity, "rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        // TToast.show(activity, "rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        // TToast.show(activity, "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        //TToast.show(activity, "verify:" + rewardVerify + " amount:" + rewardAmount +
                        //  " name:" + rewardName);
                        if (activity.isDestroyed() || activity.isFinishing())
                            return;

                        if (rewardVerify) {
                            listener.onVideoComplete();
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        //TToast.show(activity, "rewardVideoAd has onSkippedVideo");
                    }
                });
//                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
//                    @Override
//                    public void onIdle() {
//                        if (activity.isDestroyed() || activity.isFinishing())
//                            return;
//                        mHasShowDownloadActive = false;
//                    }
//
//                    @Override
//                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//
//                        if (activity.isDestroyed() || activity.isFinishing())
//                            return;
//
//                        if (!mHasShowDownloadActive) {
//                            mHasShowDownloadActive = true;
//                            //TToast.show(activity, "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
//                        }
//                    }
//
//                    @Override
//                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                        //TToast.show(activity, "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
//                    }
//
//                    @Override
//                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                        //TToast.show(activity, "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
//                    }
//
//                    @Override
//                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                        //TToast.show(activity, "下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
//                    }
//
//                    @Override
//                    public void onInstalled(String fileName, String appName) {
//                        //TToast.show(activity, "安装完成，点击下载区域打开", Toast.LENGTH_LONG);
//                    }
//                });
            }
        });
    }

    public void showReward() {
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttRewardVideoAd.showRewardVideoAd(activity);
            mttRewardVideoAd = null;
        }
    }

    public void setListener(TTReward.TTRewardListener listener) {
        this.listener = listener;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isReady() {
        return mttRewardVideoAd != null;
    }
}
