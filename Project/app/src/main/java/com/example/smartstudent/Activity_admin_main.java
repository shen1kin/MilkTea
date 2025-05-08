package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_admin_main extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);


        //首次进来就显示首页
        loadFragment(new Fragment_admin_home());


        // 初始化底部导航栏
        BottomNavigationView bottomNav = findViewById(R.id.activity_admin_main_bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.admin_home_navigation:
                    loadFragment(new Fragment_admin_home());  // 切换到首页 Fragment
                    return true;
                case R.id.admin_item_control_navigation:
                    loadFragment(new Fragment_admin_item_control());  // 切换到商品管理
                    return true;
                case R.id.admin_score_navigation:
                    loadFragment(new Fragment_admin_commodity());  // 切换到成绩设计
                    return true;
                case R.id.admin_profile_navigation:
                    loadFragment(new Fragment_admin_profile());  // 切换到个人中心
                    return true;
            }
            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        //1、在系统中原生的Fragment是通过getFragmentManager获得的。
        //2.开启一个事务，通过调用beginTransaction方法开启。
        //3.向容器内加入Fragment，一般使用add或者replace方法实现，需//要传入容器的id和Fragment的实例。
        //4.提交事务，调用commit方法提交。

        FragmentManager fragmentManager = getSupportFragmentManager(); // 注意是 getSupportFragmentManager

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_admin_main_container, fragment);
        fragmentTransaction.commit();
    }

}