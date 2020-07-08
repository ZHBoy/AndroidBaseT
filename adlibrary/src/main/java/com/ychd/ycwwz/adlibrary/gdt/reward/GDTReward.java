package com.ychd.ycwwz.adlibrary.gdt.reward;

import android.app.Activity;

/**
 * @author YMH
 * @date 2020/03/25
 * @description: 广点通的激励视频
 */
public class GDTReward {
    public RewardBuildGDT rewardBuildGDT;
    private boolean isShowing=false;

    public GDTReward(Activity activity, String postId, GTDRewardListener listener) {
        rewardBuildGDT = new RewardBuildGDT();
        rewardBuildGDT.setListener(listener);
        rewardBuildGDT.setPostId(postId);
        rewardBuildGDT.initReward(activity);
        rewardBuildGDT.loadAd();
    }


    public void showAd() {
        isShowing=true;
        rewardBuildGDT.showReward();
    }

    public boolean isShowing(){
        return isShowing;
    }

    public void loadAd() {
        rewardBuildGDT.loadAd();
    }

    /**
     * 销毁激励视频()
     */

    public void releaseAd(){
        rewardBuildGDT.releaseAd();
    }

    public boolean isReady() {
        return rewardBuildGDT.isReady();
    }

    public interface GTDRewardListener {
        void onVideoDownloadSuccess();//激励视频缓存完毕，可以播放了

        void onNoAD(String message);//没有广告

        void onVideoComplete();//激励视频观看完毕，有奖励
    }
}
