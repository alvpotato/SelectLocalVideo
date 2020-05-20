package com.example.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.domain.VideoItem;
import com.example.test.utils.PickConfig;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements PickConfig.OnVideosSelectedFinishedListener {
    private static final String TAG="VideoActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int MAX_SELECTED_COUNT =1;
    private VideoView mVideoView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initPickerConfig();
        initView();

    }

    private void initView() {
        mVideoView = this.findViewById(R.id.video);
    }

    private void initPickerConfig() {
        PickConfig pickConfig = PickConfig.getInstance();
        pickConfig.setMaxSelectedCount(MAX_SELECTED_COUNT);
        pickConfig.setOnVideosSelectedFinishedListener(this);
    }


    private void checkPermission() {
        int readExStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readExStoragePermission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //有权限

            }else {
                //没有权限，根据之后的交互去处理。
            }
        }
    }

    public void pickVideos(View view){
        //打开一个界面，选择视频页面。
        startActivity(new Intent(this, PickerActivity.class));
        Log.d(TAG, "startActivity==========11111111111111");
    }

    @Override
    public void onSelectedFinished(List<VideoItem> result) {
        //所选择的视频数据回来了,多条数据可以使用for循环。
        for (VideoItem videoItem : result) {
            Log.d(TAG, "选择的 video item  ========>" + videoItem);
            Log.d(TAG, "选择的 video item  ------->" + videoItem.getPath());
            Log.d(TAG, "选择的 video item  ------->" + videoItem.getDate());
            Log.d(TAG, "选择的 video item  ------->" + videoItem.getVideoLength());
        }
        //这里我限定选择一个视频。
        String path = result.get(0).getPath();
        Log.d(TAG, "选择的 path  ========>" + path);
        playVideo(path);

    }


    private void playVideo(String path) {
        //设置有进度条可以拖动快进
        MediaController localMediaController = new MediaController(this);
        mVideoView.setMediaController(localMediaController);
//        String url = "https://flv2.bn.netease.com/videolib1/1811/26/OqJAZ893T/HD/OqJAZ893T-mobile.mp4";
        mVideoView.setVideoPath(path);
        mVideoView.start();
    }



}
