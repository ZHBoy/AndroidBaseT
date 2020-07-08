package com.ychd.ycwwz.adlibrary.ttad.reward;

import android.app.Activity;

/**
 * @author ZhouH
 * @date 2019/10/25
 * @description: 头条的激励视频
 */
public class TTReward {
    private RewardBuildTt rewardBuildTt;
    private Activity activity;
    private boolean isShowing=false;

    public TTReward(Activity activity, String postId, TTRewardListener listener) {
        this.activity = activity;
//        TTAdManager var2 = TTAdManagerFactory.getInstance(activity, false);
//        var2.setAppId(ConstantTt.APP_ID);
        rewardBuildTt = new RewardBuildTt();
        rewardBuildTt.initReward(activity);
        rewardBuildTt.setListener(listener);
        rewardBuildTt.setPostId(postId);
        rewardBuildTt.loadAd();
    }


    public void showAd() {
        isShowing=true;
        rewardBuildTt.showReward();
    }

    public boolean isShowing(){
        return isShowing;
    }

    public void loadAd() {
//        TTAdManager var2 = TTAdManagerFactory.getInstance(activity, false);
//        var2.setAppId(ConstantTt.APP_ID);
        rewardBuildTt.loadAd();
    }

    /**
     * 销毁激励视频()
     */

    public void releaseAd(){
        rewardBuildTt.releaseAd();
    }

    public boolean isReady() {
        return rewardBuildTt.isReady();
    }

    public interface TTRewardListener {
        void onVideoDownloadSuccess();//激励视频缓存完毕，可以播放了

        void onNoAD();//没有广告

        void onVideoComplete();//激励视频观看完毕，有奖励
    }
}
