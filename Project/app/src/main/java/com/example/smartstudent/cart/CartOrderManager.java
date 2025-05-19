package com.example.smartstudent.cart;

import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.OrderItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartOrderManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    private static final Map<OrderItem, Integer> cartMap = new LinkedHashMap<>();

    public static void add(OrderItem orderItemInfo) {
        int count = cartMap.getOrDefault(orderItemInfo, 0);
        cartMap.put(orderItemInfo, count + orderItemInfo.getCount());

    }
    // 减少订单数量
    public static void decrease(OrderItem orderItemInfo) {
        int count = cartMap.getOrDefault(orderItemInfo, 0);
        int newCount = count - orderItemInfo.getCount();

        if (newCount > 0) {
            cartMap.put(orderItemInfo, newCount);
        } else {
            cartMap.remove(orderItemInfo);
        }
    }

    // 移除整个订单（不管当前数量是多少）
    public static void remove(OrderItem orderItemInfo) {
        cartMap.remove(orderItemInfo);
    }

    // 清空购物车
    public static void clear() {
        cartMap.clear();
    }

    public static int getTotalCount() {
        int sum = 0;
        for (int count : cartMap.values()) {
            sum += count;
        }
        return sum;
    }

    public static String getTotalPrice() {
        double total = 0;
        for (Map.Entry<OrderItem, Integer> entry : cartMap.entrySet()) {
            try {
                String priceStr = entry.getKey().getPrice().replace("¥", "");
                total += Double.parseDouble(priceStr) * entry.getValue();
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        return "¥" + total;
    }

    public static List<CartItem> getItems() {
        List<CartItem> list = new ArrayList<>();
        for (Map.Entry<OrderItem, Integer> entry : cartMap.entrySet()) {
            list.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public static Map<OrderItem, Integer> getCartMap() {
        return cartMap;
    }


}
