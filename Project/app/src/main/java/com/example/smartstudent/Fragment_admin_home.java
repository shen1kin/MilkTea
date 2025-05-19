package com.example.smartstudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_admin_home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    // 跳转到订单管理（如未实现可暂时 Toast 提示）
//    public void onClickOrderManage(View view) {
//        Toast.makeText(getContext(), "跳转到订单管理页面", Toast.LENGTH_SHORT).show();
//
//        // 示例跳转：Activity 或 Fragment（二选一）
//        // startActivity(new Intent(getContext(), Activity_admin_OrderManage.class));
//        // 或跳转 Fragment
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_admin_main_container, new Fragment_admin_order()) // 示例 Fragment
//                .addToBackStack(null)
//                .commit();
//    }

    // 跳转到商品管理
    public void onClickProductManage(View view) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_admin_main_container, new Fragment_admin_item_control())
                .addToBackStack(null)
                .commit();
    }

    // 跳转到用户反馈
    public void onClickFeedback(View view) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_admin_main_container, new Fragment_admin_commodity()) // 或你实际的反馈 Fragment
                .addToBackStack(null)
                .commit();
    }
}
