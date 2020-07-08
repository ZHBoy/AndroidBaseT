package com.ychd.ycwwz.adlibrary.baiduad.reward;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.baidu.mobads.rewardvideo.RewardVideoAd;
import com.ychd.ycwwz.adlibrary.baiduad.reward.BDReward;
import com.ychd.ycwwz.base_library.utils.TLog;

/**
 * @author YMH
 * @date 2020/02/06
 * @description: 百度激励视频初始化调用相关
 */

public class RewardBuildBD implements RewardVideoAd.RewardVideoAdListener{
    private RewardVideoAd mRewardVideoAd;
    private BDReward.BDRewardListener listener;
    private String postId;

    public void initReward(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRewardVideoAd = new RewardVideoAd(activity,
                postId, RewardBuildBD.this, false);

    }


    public void loadAd() {
        mRewardVideoAd.load();
    }

    public boolean isReady() {
        return mRewardVideoAd != null && mRewardVideoAd.isReady();
    }

    public void showReward() {
        if (mRewardVideoAd != null) {
            mRewardVideoAd.show();
        }
    }

    public void releaseAd(){
        mRewardVideoAd = null;
    }


    public void setListener(BDReward.BDRewardListener listener) {
        this.listener = listener;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }



    @Override
    public void onAdShow() {
        TLog.INSTANCE.i("rewardadserror","onAdShow");

    }

    @Override
    public void onAdClick() {
        TLog.INSTANCE.i("rewardadserror","onAdClick");
    }

    @Override
    public void onAdClose(float v) {
        TLog.INSTANCE.i("rewardadserror","onAdClose");
    }

    @Override
    public void onAdFailed(String s) {
        TLog.INSTANCE.i("rewardadserror","onAdFailed"+s);
        listener.onNoAD();
    }

    @Override
    public void onVideoDownloadSuccess() {
        TLog.INSTANCE.i("rewardadserror","onVideoDownloadSuccess");
        listener.onVideoDownloadSuccess();
    }

    @Override
    public void onVideoDownloadFailed() {
        TLog.INSTANCE.i("rewardadserror","onVideoDownloadFailed");
    }

    @Override
    public void playCompletion() {
        TLog.INSTANCE.i("rewardadserror","playCompletion");
        listener.onVideoComplete();
    }
}
