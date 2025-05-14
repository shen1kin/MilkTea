package com.example.smartstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudent.adapter.OrderAdapter;
import com.example.smartstudent.model.Order;

import java.util.ArrayList;import java.util.List;

public class Fragment_student_score extends Fragment {

    private RecyclerView recyclerOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score, container, false);

        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        initDummyOrders();
        orderAdapter = new OrderAdapter(orderList);
        recyclerOrders.setAdapter(orderAdapter);

        return view;
    }

    private void initDummyOrders() {
        orderList.clear();
        orderList.add(new Order("广州软件学院店", "鲜果茶 x1", "¥14.00", "已完成"));
        orderList.add(new Order("广州软件学院宿舍", "奶茶 x2", "¥28.00", "配送中"));
        orderList.add(new Order("广州花都商达街店", "抹茶拿铁 x1", "¥16.00", "已取消"));
    }
}
