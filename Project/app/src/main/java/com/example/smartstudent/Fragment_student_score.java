package com.example.smartstudent;

import android.content.Intent;
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
import com.example.smartstudent.model.OrderRepository;

import java.util.List;

public class Fragment_student_score extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_score, container, false);
        recyclerView = view.findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 获取订单列表数据
        List<Order> orderList = OrderRepository.getOrders();

        // 设置适配器并传入点击事件
        orderAdapter = new OrderAdapter(orderList, order -> {
            // 点击跳转到订单详情页
            Intent intent = new Intent(getContext(), OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });

        recyclerView.setAdapter(orderAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次回到页面时刷新订单数据，确保刚刚支付生成的订单能显示
        if (orderAdapter != null) {
            orderAdapter.setOrderList(OrderRepository.getOrders());
            orderAdapter.notifyDataSetChanged();
        }
    }
}
