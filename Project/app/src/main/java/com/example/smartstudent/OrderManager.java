package com.example.smartstudent;

import com.example.smartstudent.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final List<Order> orders = new ArrayList<>();

    public static void addOrder(Order order) {
        orders.add(0, order); // 新订单放在最前
    }

    public static List<Order> getOrders() {
        return orders;
    }
}
