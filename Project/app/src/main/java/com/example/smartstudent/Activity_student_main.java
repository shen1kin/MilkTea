package com.example.smartstudent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;

public class Activity_student_main extends AppCompatActivity {

    private final HashMap<Integer, Fragment> fragmentMap = new HashMap<>();
    private int currentItemId = -1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_main);

        BottomNavigationView bottomNav = findViewById(R.id.activity_student_main_bottomNavigationView);

        // 初始化 Fragment Map
        fragmentMap.put(R.id.student_home_navigation, new Fragment_student_home());
        fragmentMap.put(R.id.student_course_navigation, new Fragment_student_course());
        fragmentMap.put(R.id.student_score_navigation, new Fragment_student_score());
        fragmentMap.put(R.id.student_profile_navigation, new Fragment_student_profile());

        // 默认加载首页
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.student_home_navigation);
            switchFragment(R.id.student_home_navigation);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            switchFragment(item.getItemId());
            return true;
        });
    }

    private void switchFragment(int itemId) {
        if (itemId == currentItemId) return;
        currentItemId = itemId;

        Fragment fragment = fragmentMap.get(itemId);
        if (fragment == null) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out
        );
        transaction.replace(R.id.fragment_student_main_container, fragment);
        transaction.commit();
    }

    public void switchToCourseFragment() {
        BottomNavigationView bottomNav = findViewById(R.id.activity_student_main_bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.student_course_navigation);
        switchFragment(R.id.student_course_navigation);
    }
}
