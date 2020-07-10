package com.zhboy.ycwwz.adlibrary.baiduad.reward;

import android.app.Activity;

/**
 * @author YMH
 * @date 2020/02/06
 * @description: 百度的激励视频
 */
public class BDReward {
    public RewardBuildBD rewardBuildBD;
    private boolean isShowing=false;

    public BDReward(Activity activity, String postId, BDRewardListener listener) {
        rewardBuildBD = new RewardBuildBD();
        rewardBuildBD.setListener(listener);
        rewardBuildBD.setPostId(postId);
        rewardBuildBD.initReward(activity);
        rewardBuildBD.loadAd();
    }


    public void showAd() {
        isShowing=true;
        rewardBuildBD.showReward();
    }

    public boolean isShowing(){
        return isShowing;
    }

    public void loadAd() {
        rewardBuildBD.loadAd();
    }

    /**
     * 销毁激励视频()
     */

    public void releaseAd(){
        rewardBuildBD.releaseAd();
    }

    public boolean isReady() {
        return rewardBuildBD.isReady();
    }

    public interface BDRewardListener {
        void onVideoDownloadSuccess();//激励视频缓存完毕，可以播放了

        void onNoAD();//没有广告

        void onVideoComplete();//激励视频观看完毕，有奖励
    }
}
