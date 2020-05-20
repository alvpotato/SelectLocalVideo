package com.example.test;


import android.database.Cursor;

import android.os.Bundle;

import android.provider.MediaStore;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.adapter.VideoListAdapter;
import com.example.test.domain.VideoItem;
import com.example.test.utils.PickConfig;

import java.util.ArrayList;
import java.util.List;

/**
 *   这里只是列出了一些视频信息，还有比较多。这里我只使用了 _data，_size，duration，三个。
 */

//_id ====== 14813
//_data ====== /storage/emulated/0/DCIM/Camera/video_20200518_102259.mp4
//_display_name ====== video_20200518_102259.mp4
//_size ====== 7520033
//mime_type ====== video/mp4
//date_added ====== 1589768587
//date_modified ====== 1589768579
//title ====== video_20200518_102259
//duration ====== 6379
//resolution ====== 720x1280
//latitude ====== 34.784
//longitude ====== 113.416
//datetaken ====== 1589768587737
//mini_thumb_magic ====== -7937670052952072253
//bucket_id ====== -1739773001
//bucket_display_name ====== Camera
//width ====== 1280
//height ====== 720
//shoupin ====== VIDEO_20200518_102259.MP4
//quanpin ====== VIDEO_20200518_102259.MP4
//----------------------------


public class PickerActivity extends AppCompatActivity implements VideoListAdapter.OnItemSelectedChangeListener {
    private static final String TAG = "PickerActivity";
    public static final int LOADER_ID = 1;

    private List<VideoItem> mVideoItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private VideoListAdapter mVideoListAdapter;
    private TextView mFinishView;
    private PickConfig mPickConfig;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        initLoaderManager();
        initView();
        initEvent();
        initConfig();

        
    }

    private void initConfig() {
        mPickConfig = PickConfig.getInstance();
        int maxSelectedCount = mPickConfig.getMaxSelectedCount();
        mVideoListAdapter.setMaxSelectedCount(maxSelectedCount);
    }

    private void initEvent() {
        mVideoListAdapter.setOnItemSelectedChangeListener(this);
        mFinishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取选择的视频数据
                List<VideoItem> result = mVideoListAdapter.getSelectedItems();
                //通知其他地方
                PickConfig.OnVideosSelectedFinishedListener videosSelectedFinishedListener = mPickConfig.getOnVideosSelectedFinishedListener();
                if (videosSelectedFinishedListener != null) {
                    videosSelectedFinishedListener.onSelectedFinished(result);
                }
                //结束picker界面
                finish();
            }
        });

    }

    private void initView() {
        mRecyclerView = this.findViewById(R.id.video_list_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //设置适配器
        mVideoListAdapter = new VideoListAdapter();
        mRecyclerView.setAdapter(mVideoListAdapter);
        mFinishView = this.findViewById(R.id.finish_tv);


    }

    private void initLoaderManager() {
        mVideoItems.clear();
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (id == LOADER_ID) {
                    return new CursorLoader(PickerActivity.this,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            new String[]{"_data", "_size", "duration"},
                            null,null,"date_added DESC");
                }

                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                if (cursor != null) {


                    while (cursor.moveToNext()){
                        String path = cursor.getString(0);
                        long size = cursor.getLong(1);
                        int duration = cursor.getInt(2);

                        VideoItem videoItem = new VideoItem(path,size,duration);
                        mVideoItems.add(videoItem);
                    }
                    cursor.close();
                    mVideoListAdapter.setData(mVideoItems);

//                    for (VideoItem videoItem : mVideoItems) {
//                        Log.d(TAG, "videos === >" + videoItem);
//                    }
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    @Override
    public void onItemSelectedChange(List<VideoItem> selectedItems) {
        //所选择的数据发生变化
        mFinishView.setText("(" + selectedItems.size() + "/" + mVideoListAdapter.getMaxSelectedCount() + "完成)");
    }
}
