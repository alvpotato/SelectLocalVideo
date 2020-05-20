package com.example.test.utils;

import com.example.test.domain.VideoItem;

import java.util.List;

public class PickConfig {

    private PickConfig(){}

    private static PickConfig sPickConfig;

    public static PickConfig getInstance(){
        if (sPickConfig == null){
            sPickConfig = new PickConfig();
        }
        return sPickConfig;
    }

    private int maxSelectedCount = 1;
    private OnVideosSelectedFinishedListener mVideoSelectedFinishedListener = null;

    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    public OnVideosSelectedFinishedListener getOnVideosSelectedFinishedListener(){
        return mVideoSelectedFinishedListener;
    }

    public void setOnVideosSelectedFinishedListener(OnVideosSelectedFinishedListener listener){
        this.mVideoSelectedFinishedListener = listener;
    }

    public interface OnVideosSelectedFinishedListener{
        void onSelectedFinished(List<VideoItem> result);
    }
}
