package com.example.test.adapter;

import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.domain.VideoItem;
import com.example.test.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.InnerHolder> {

    private static final String TAG = "VideoListAdapter";
    private List<VideoItem> mVideoItems = new ArrayList<>();
    private List<VideoItem> mSelectedItems = new ArrayList<>();
    private OnItemSelectedChangeListener mItemSelectedChangeListener = null;
    public static final int MAX_SELECTED_COUNT =1;
    private int maxSelectedCount = MAX_SELECTED_COUNT;

    public List<VideoItem> getSelectedItems() {
        return mSelectedItems;
    }

    public void setSelectedItems(List<VideoItem> selectedItems) {
        mSelectedItems = selectedItems;
    }

    public int getMaxSelectedCount() {
        return maxSelectedCount;
    }

    public void setMaxSelectedCount(int maxSelectedCount) {
        this.maxSelectedCount = maxSelectedCount;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载ItemView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        Point point = SizeUtils.getScreenSize(itemView.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(point.x/3,point.x/3);
        itemView.setLayoutParams(layoutParams);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //绑定数据
        //这里设置成了视频的缩略图
        final View itemView = holder.itemView;
        final ImageView ImageView = itemView.findViewById(R.id.video_thumbnail_iv);
        final VideoItem videoItem = mVideoItems.get(position);
        final CheckBox checkBox = itemView.findViewById(R.id.image_check_box);
        final View cover = itemView.findViewById(R.id.image_cover);
        Glide.with(ImageView.getContext()).load(videoItem.getPath()).into(ImageView);

        //根据数据状态显示内容
        //没有选择上，应该选上
        if(mSelectedItems.contains(videoItem)){
            mSelectedItems.add(videoItem);
            //修改ui
            checkBox.setChecked(false);
            checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.drawable.duihao));
            cover.setVisibility(View.VISIBLE);


        }else {

            //已经选择上了，应该取消选择
            mSelectedItems.remove(videoItem);
            //修改ui
            checkBox.setChecked(true);
            checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.drawable.select));
            cover.setVisibility(View.GONE);

        }



        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断选择上没有
                //如果选择上就变成取消
                //如果没有选择上，就将它选择上
                if(mSelectedItems.contains(videoItem)){
                    //已经选择上了，应该取消选择
                    mSelectedItems.remove(videoItem);
                    //修改ui
                    checkBox.setChecked(true);
                    checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.drawable.select));
                    cover.setVisibility(View.GONE);

                }else {
                    if(mSelectedItems.size() >= maxSelectedCount){
                        //给一个提示
                        Toast toast = Toast.makeText(checkBox.getContext(), null, Toast.LENGTH_SHORT);
                        toast.setText("最多可以选择" + maxSelectedCount + "个视频");
                        toast.show();
                        return;
                    }

                    //没有选择上，应该选上
                    mSelectedItems.add(videoItem);
                    //修改ui
                    checkBox.setChecked(false);
                    checkBox.setButtonDrawable(itemView.getContext().getDrawable(R.drawable.duihao));
                    cover.setVisibility(View.VISIBLE);
                    Log.d(TAG, "没有选择上，点击选择。");
                    Log.d(TAG, "选中的个数" + mSelectedItems.size());
                }
                if (mItemSelectedChangeListener != null) {
                    mItemSelectedChangeListener.onItemSelectedChange(mSelectedItems);
                }

            }
        });

    }

    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener listener){
        this.mItemSelectedChangeListener = listener;
    }

    public interface OnItemSelectedChangeListener{
        void onItemSelectedChange(List<VideoItem> selectedItems);
    }

    @Override
    public int getItemCount() {
        return mVideoItems.size();
    }

    public void setData(List<VideoItem> videoItems) {
        mVideoItems.clear();
        mVideoItems.addAll(videoItems);
        notifyDataSetChanged();

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
