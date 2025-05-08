package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.ItemListAdapter;
import com.example.smartstudent.model.ItemInfo;

import java.util.ArrayList;
import java.util.List;

public class Fragment_admin_item_control extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_item_control,container,false);
        //初始化控件
        RecyclerView rv_item_list_view = view.findViewById(R.id.rvItemList);

        List<ItemInfo> itemInfoList = new ArrayList<>();

        //模拟数据
        itemInfoList.add(new ItemInfo(0,"第1个名字","10","上架","$100"));
        itemInfoList.add(new ItemInfo(1,"第2个名字","516","下架","$546456"));
        itemInfoList.add(new ItemInfo(2,"第3个名字","654968","上架","$6845"));
        //初始化适配器
        ItemListAdapter itemListAdapter = new ItemListAdapter(itemInfoList);

        //设置适配器
        rv_item_list_view.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_item_list_view.setAdapter(itemListAdapter);

        return view;
    }
}
