package com.example.smartstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 管理员主页面 Fragment
 * 展示：欢迎语、数据卡片、快捷功能入口（订单管理、商品管理、用户反馈）
 */
public class Fragment_admin_home extends Fragment {

    private TextView tvTodayOrders, tvTodayRevenue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // 加载布局文件 fragment_admin_home.xml
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // 快捷入口卡片点击跳转
        LinearLayout layoutOrderManage = view.findViewById(R.id.layoutOrderManage);      // 订单管理
        LinearLayout layoutProductManage = view.findViewById(R.id.layoutProductManage);  // 商品管理

        layoutOrderManage.setOnClickListener(v -> replaceFragment(new Fragment_admin_item_control()));
        layoutProductManage.setOnClickListener(v -> replaceFragment(new Fragment_admin_commodity()));


        // 获取 TextView 控件引用
        tvTodayOrders = view.findViewById(R.id.tvTodayOrders);
        tvTodayRevenue = view.findViewById(R.id.tvTodayRevenue);

        // 发起请求获取统计数据
        fetchTodayStats();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 退出登录按钮逻辑
        Button exitLogin = view.findViewById(R.id.butExitLogin);
        exitLogin.setOnClickListener(v -> {
            // 清除登录信息
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user");
            editor.apply();

            // 跳转到登录页面
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            // 关闭当前 Activity
            requireActivity().finish();
        });
    }

    /**
     * 从服务器获取今日订单数和收入
     */
    private void fetchTodayStats() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8083/Servlet_war_exploded/today_summary");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    // 解析 JSON 数据
                    JSONObject json = new JSONObject(result.toString());
                    int orderCount = json.getInt("today_total_count");
                    double totalRevenue = json.getDouble("today_total_price");

                    // 回到主线程更新 UI
                    new Handler(Looper.getMainLooper()).post(() -> {
                        tvTodayOrders.setText(String.valueOf(orderCount));
                        tvTodayRevenue.setText("¥" + totalRevenue);
                    });

                } else {
                    showToast("获取统计数据失败，状态码：" + code);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.e("AdminHome", "获取统计数据异常: ", e);
                showToast("服务器连接失败：" + e.getMessage());
            }
        }).start();
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Fragment 页面切换 + 同步底部导航栏选中状态
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_admin_main_container, fragment);
        transaction.commit();

        // 同步更新底部导航栏选中项
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.activity_admin_main_bottomNavigationView);
        if (bottomNav != null) {
            if (fragment instanceof Fragment_admin_home) {
                bottomNav.setSelectedItemId(R.id.admin_home_navigation);
            } else if (fragment instanceof Fragment_admin_item_control) {
                bottomNav.setSelectedItemId(R.id.admin_item_control_navigation);
            } else if (fragment instanceof Fragment_admin_commodity) {
                bottomNav.setSelectedItemId(R.id.admin_score_navigation);
            }
        }
    }
}
