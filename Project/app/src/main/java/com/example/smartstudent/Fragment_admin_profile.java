package com.example.smartstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_admin_profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile,container,false);
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

}


