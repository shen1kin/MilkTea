package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_student_main extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_main);

        //首次进来就显示首页
        loadFragment(new Fragment_student_home());

        // 初始化底部导航栏
        BottomNavigationView bottomNav = findViewById(R.id.activity_student_main_bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.student_home_navigation:
                    loadFragment(new Fragment_student_home());  // 切换到首页 Fragment
                    return true;
                case R.id.student_course_navigation:
                    loadFragment(new Fragment_student_course());  // 切换到课表查询
                    return true;
                case R.id.student_score_navigation:
                    loadFragment(new Fragment_student_score());  // 切换到成绩查询
                    return true;
                case R.id.student_profile_navigation:
                    loadFragment(new Fragment_student_profile());  // 切换到个人中心
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

        fragmentTransaction.replace(R.id.fragment_student_main_container, fragment);
        fragmentTransaction.commit();
    }

}