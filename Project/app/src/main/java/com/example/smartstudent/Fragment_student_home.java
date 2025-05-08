package com.example.smartstudent;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.Toast;
import androidx.viewpager2.widget.ViewPager2;
public class Fragment_student_home extends Fragment {

    private ViewPager2 viewPager;

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    // 原始图片列表
    private final Integer[] imageList = {
            R.drawable.zs1,
            R.drawable.zs2,
            R.drawable.zs3
    };

    // 扩展后的“无限”图片列表
    private List<Integer> loopedImageList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        // 扩展图片列表（重复3次）
        loopedImageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            loopedImageList.addAll(Arrays.asList(imageList));
        }


        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(loopedImageList);
        viewPager.setAdapter(adapter);

        // 滚动到中段开始
        int startPosition = imageList.length;
        viewPager.setCurrentItem(startPosition, false);

        // 自动轮播逻辑
        sliderRunnable = () -> {
            int nextItem = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(nextItem, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // 页面滑动监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                int position = viewPager.getCurrentItem();
                int totalSize = loopedImageList.size();

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (position == 0) {
                        viewPager.setCurrentItem(imageList.length, false);
                    } else if (position == totalSize - 1) {
                        viewPager.setCurrentItem(imageList.length * 2 - 1, false);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        // 示例按钮
        Button btnStorePickup = view.findViewById(R.id.btnStorePickup);
        Button btnDelivery = view.findViewById(R.id.btnDelivery);

        btnStorePickup.setOnClickListener(v ->
                Toast.makeText(getContext(), "选择了到店取", Toast.LENGTH_SHORT).show());

        btnDelivery.setOnClickListener(v ->
                Toast.makeText(getContext(), "选择了喜外送", Toast.LENGTH_SHORT).show());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}







