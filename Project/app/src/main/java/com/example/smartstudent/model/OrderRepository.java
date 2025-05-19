package com.example.smartstudent.model;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final List<Order> ORDER_ITEMS = new ArrayList<>();

    // 添加订单
    public static void addOrder(Order orderItem) {
        ORDER_ITEMS.add(0, orderItem); // 最新订单添加到前面
    }

    // 获取所有订单
    public static List<Order> getOrders() {
        return new ArrayList<>(ORDER_ITEMS); // 防止外部修改原始列表
    }
}
