package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.VipServiceAdapter;
import com.example.smartstudent.model.VipserveItem;

import java.util.ArrayList;
import java.util.List;

public class Fragment_student_profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile,container,false);

        // 准备数据
        List<VipserveItem> services = new ArrayList<>();
        services.add(new VipserveItem("会员码", R.drawable.vip_code, hello.class));
        services.add(new VipserveItem("任务中心", R.drawable.task_centres, hello.class));
        services.add(new VipserveItem("发票助手", R.drawable.receipt, hello.class));
        services.add(new VipserveItem("联系客服", R.drawable.service, hello.class));
        services.add(new VipserveItem("设置", R.drawable.set, hello.class));
        services.add(new VipserveItem("更多", R.drawable.more, hello.class));

        // 设置RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.menu_recycler);
        //显示列数
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        //适配器实现 参数是 界面 数据
        recyclerView.setAdapter(new VipServiceAdapter(requireContext(), services));



        return view;
    }
}
