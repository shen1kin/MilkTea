package com.example.smartstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Fragment_admin_home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        GridLayout gridLayout = view.findViewById(R.id.gridFunctions);// 强制转换 GridLayout
        LinearLayout layoutProduct = (LinearLayout) gridLayout.getChildAt(0);
        LinearLayout layoutOrder = (LinearLayout) gridLayout.getChildAt(1);
        LinearLayout layoutFeedback = (LinearLayout) gridLayout.getChildAt(2);

        layoutOrder.setOnClickListener(v -> {
            replaceFragment(new Fragment_admin_commodity()); // 订单管理
        });

        layoutProduct.setOnClickListener(v -> {
            replaceFragment(new Fragment_admin_item_control()); // 商品管理
        });

//        layoutFeedback.setOnClickListener(v -> {
//            replaceFragment(new Fragment_admin_feedback()); // 用户反馈
//        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //退出登录
        Button exitLogin = view.findViewById(R.id.butExitLogin);
        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 删除本地存储信息
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("user");  // 删除键为 "user" 的数据
                editor.apply();

                // 跳转回登录页
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                // 关闭当前页面
                requireActivity().finish();
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_admin_main_container, fragment); // 和 Activity_admin_main 中一致
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
