package com.example.smartstudent.model;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final List<Order> orders = new ArrayList<>();

    // 添加订单
    public static void addOrder(Order order) {
        orders.add(0, order); // 最新订单添加到前面
    }

    // 获取所有订单
    public static List<Order> getOrders() {
        return new ArrayList<>(orders); // 防止外部修改原始列表
    }
}
