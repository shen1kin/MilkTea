package com.example.smartstudent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_student_home extends Fragment {

    private ViewPager2 viewPager;
    private Handler sliderHandler;
    private Runnable sliderRunnable;
    private final Integer[] imageList = {
            R.drawable.zs1,
            R.drawable.zs2,
            R.drawable.zs3,
            R.drawable.zs4,
            R.drawable.zs5
    };

    private List<Integer> loopedImageList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        setupViewPager(view);
        setupButtonClicks(view);
        setupImageShortcuts(view);

        return view;
    }

    private void setupViewPager(View view) {
        loopedImageList = new ArrayList<>();
        for (int i = 0; i < 5; i++) loopedImageList.addAll(Arrays.asList(imageList));

        viewPager = view.findViewById(R.id.viewPager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(loopedImageList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imageList.length, false);

        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = () -> {
            int next = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(next, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                int pos = viewPager.getCurrentItem();
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (pos == 0) viewPager.setCurrentItem(imageList.length, false);
                    else if (pos == loopedImageList.size() - 1)
                        viewPager.setCurrentItem(imageList.length * 2 - 1, false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void setupButtonClicks(View view) {
        Button btnStorePickup = view.findViewById(R.id.btnStorePickup);
        Button btnDelivery = view.findViewById(R.id.btnDelivery);

        btnStorePickup.setOnClickListener(v -> {
            OrderModeManager.setCurrentMode(OrderModeManager.PICKUP); // ✅ 设置为自取
            if (getActivity() instanceof Activity_student_main) {
                ((Activity_student_main) getActivity()).switchToCourseFragment();
            }
        });

        btnDelivery.setOnClickListener(v -> {
            OrderModeManager.setCurrentMode(OrderModeManager.DELIVERY); // ✅ 设置为喜外送
            if (getActivity() instanceof Activity_student_main) {
                ((Activity_student_main) getActivity()).switchToCourseFragment();
            }
        });
    }

    private void setupImageShortcuts(View view) {
        ImageView btnStore = view.findViewById(R.id.btn_store);
        ImageView btnGroupMeal = view.findViewById(R.id.btn_group_meal);
        ImageView btnGiftCard = view.findViewById(R.id.btn_gift_card);

        View.OnClickListener intentLauncher = v -> {
            Intent intent = new Intent(getActivity(), hello.class);
            startActivity(intent);
        };

        btnStore.setOnClickListener(intentLauncher);
        btnGroupMeal.setOnClickListener(intentLauncher);
        btnGiftCard.setOnClickListener(intentLauncher);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sliderHandler != null) {
            sliderHandler.removeCallbacks(sliderRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // ✅ 可选：每次回到首页时默认重置为自取
        OrderModeManager.setCurrentMode(OrderModeManager.PICKUP);
    }
}
