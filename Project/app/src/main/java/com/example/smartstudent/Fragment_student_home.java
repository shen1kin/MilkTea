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
    private final Integer[] imageList = {
            R.drawable.zs1,  // 请确保资源名称正确
            R.drawable.zs2,
            R.drawable.zs3
    };
    private Handler sliderHandler;
    private Runnable sliderRunnable;

    // 图片按钮相关
    private ImageView btnStore, btnGroupMeal, btnGiftCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        // 初始化轮播图
        initViewPager(view);

        // 初始化图片按钮
        initImageButtons(view);

        return view;
    }

    private void initViewPager(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        List<Integer> imageData = Arrays.asList(imageList);

        ImageSliderAdapter adapter = new ImageSliderAdapter(imageData);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        // 自动轮播逻辑
        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int current = viewPager.getCurrentItem();
                viewPager.setCurrentItem(current + 1, true);
                sliderHandler.postDelayed(this, 3000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // 滑动监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void initImageButtons(View view) {
        // 绑定控件
        btnStore = view.findViewById(R.id.btn_store);
        btnGroupMeal = view.findViewById(R.id.btn_group_meal);
        btnGiftCard = view.findViewById(R.id.btn_gift_card);

        // 设置点击事件
        btnStore.setOnClickListener(v -> showToast("进入百货商城"));
        btnGroupMeal.setOnClickListener(v -> showToast("团餐服务"));
        btnGiftCard.setOnClickListener(v -> showToast("喜卡兑换"));
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}






