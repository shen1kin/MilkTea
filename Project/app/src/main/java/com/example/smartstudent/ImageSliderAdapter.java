package com.example.smartstudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smartstudent.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {
    private final List<Integer> imageResources; // 图片资源ID列表
    private static final int MULTIPLIER = 1000; // 数据放大系数

    public ImageSliderAdapter(List<Integer> imageResources) {
        this.imageResources = imageResources;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 通过取模运算获取实际位置
        int actualPosition = position % imageResources.size();
        holder.imageView.setImageResource(imageResources.get(actualPosition));
    }

    @Override
    public int getItemCount() {
        // 返回足够大的数量实现"无限"效果
        return imageResources.size() * MULTIPLIER;
    }

    // 初始化时设置中间位置
    public void initAutoScroll(ViewPager2 viewPager) {
        // 从中间位置开始，支持双向滑动
        int initialPosition = imageResources.size() * MULTIPLIER / 2;
        viewPager.setCurrentItem(initialPosition, false);
    }

    // ViewHolder内部类
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_image);
        }
    }
}