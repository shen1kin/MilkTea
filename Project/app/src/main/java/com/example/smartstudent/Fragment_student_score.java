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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        initFakeOrders();
        orderAdapter = new OrderAdapter(orderList);
        recyclerOrders.setAdapter(orderAdapter);

        return view;
    }

    private void initFakeOrders() {
        orderList.clear();

        orderList.add(new Order(
                "广州软件学院店",
                "2025-05-14 10:25",
                "制作中",
                "¥18.00",
                2,
                Arrays.asList(R.drawable.zs1, R.drawable.zs2)
        ));

        orderList.add(new Order(
                "广州软件学院宿舍",
                "2025-05-13 21:18",
                "配送中",
                "¥32.00",
                3,
                Arrays.asList(R.drawable.zs3, R.drawable.zs4, R.drawable.zs5)
        ));

        orderList.add(new Order(
                "广州花都商达街店",
                "2025-05-12 14:08",
                "退款中",
                "¥14.00",
                1,
                Arrays.asList(R.drawable.zs1)
        ));

        orderList.add(new Order(
                "广州白云新市店",
                "2025-05-10 18:43",
                "已完成",
                "¥25.00",
                2,
                Arrays.asList(R.drawable.zs3, R.drawable.zs4)
        ));
    }
}
