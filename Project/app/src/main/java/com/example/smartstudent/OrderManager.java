package com.example.smartstudent;

import com.example.smartstudent.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final List<OrderItem> ORDER_ITEMS = new ArrayList<>();

    public static void addOrder(OrderItem orderItem) {
        ORDER_ITEMS.add(0, orderItem); // 新订单放在最前
    }

    public static List<OrderItem> getOrders() {
        return ORDER_ITEMS;
    }
}
