package com.zhboy.ycwwz.adlibrary.gdt.reward;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author YMH
 * @date 2020/03/25
 * @description: 广点通激励视频初始化调用相关
 */

public class RewardBuildGDT implements RewardVideoADListener {
    private RewardVideoAD rewardVideoAD;
    private GDTReward.GTDRewardListener listener;
    private String postId;
    private Activity activity;
    private boolean isReady=false;

    public void initReward(Activity activity) {
        this.activity=activity;
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 1.加载广告，先设置加载上下文环境和条件
        // 如果想静音播放，请使用5个参数的构造函数，且volumeOn传false即可
        rewardVideoAD = new RewardVideoAD(activity,postId, this); // 有声播放
        rewardVideoAD.loadAD();

    }


    public void loadAd() {
        isReady=false;
        rewardVideoAD.loadAD();
    }

    public boolean isReady() {
        return rewardVideoAD != null && isReady;
    }

    public void showReward() {
        if (rewardVideoAD != null) {
            rewardVideoAD.showAD(activity);
        }
    }

    public void releaseAd(){
        rewardVideoAD = null;
    }


    public void setListener(GDTReward.GTDRewardListener listener) {
        this.listener = listener;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public void onADLoad() {
        isReady=true;
        listener.onVideoDownloadSuccess();
    }

    @Override
    public void onVideoCached() {

    }

    @Override
    public void onADShow() {

    }

    @Override
    public void onADExpose() {

    }

    @Override
    public void onReward() {
        listener.onVideoComplete();
    }

    @Override
    public void onADClick() {

    }

    @Override
    public void onVideoComplete() {

    }

    @Override
    public void onADClose() {

    }

    @Override
    public void onError(AdError adError) {
        listener.onNoAD(adError.getErrorMsg()+adError.getErrorCode());
    }
}
