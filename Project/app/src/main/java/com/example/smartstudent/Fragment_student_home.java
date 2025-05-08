package com.example.smartstudent;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class Fragment_student_home extends Fragment {

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    private Integer[] imageList = {
            R.drawable.zs1,
            R.drawable.zs2,
            R.drawable.zs3
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        // 设置 ViewPager2 和适配器
        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(imageList);
        viewPager.setAdapter(adapter);

        // 自动轮播逻辑
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % imageList.length;
                viewPager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000); // 每 3 秒轮播
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // 用户滑动时重置定时器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        // 设置按钮点击事件
        Button btnStorePickup = view.findViewById(R.id.btnStorePickup);
        Button btnDelivery = view.findViewById(R.id.btnDelivery);

        btnStorePickup.setOnClickListener(v ->
                Toast.makeText(getContext(), "选择了到店取", Toast.LENGTH_SHORT).show()
        );

        btnDelivery.setOnClickListener(v ->
                Toast.makeText(getContext(), "选择了喜外送", Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}






