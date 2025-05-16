package com.example.smartstudent.cart;

import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.Order;
import com.example.smartstudent.model.ProductInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartOrderManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    private static final Map<Order, Integer> cartMap = new LinkedHashMap<>();

    public static void add(Order orderInfo) {
        int count = cartMap.getOrDefault(orderInfo, orderInfo.getTotalCount());
        cartMap.put(orderInfo, count + 1);
    }

    public static void decrease(Order orderInfo) {
        int count = cartMap.getOrDefault(orderInfo, orderInfo.getTotalCount());
        if (count > 1) {
            cartMap.put(orderInfo, count - 1);
        } else {
            cartMap.remove(orderInfo);
        }
    }

    public static void remove(Order orderInfo) {
        cartMap.remove(orderInfo);
    }

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
        for (Map.Entry<Order, Integer> entry : cartMap.entrySet()) {
            try {
                String priceStr = entry.getKey().getTotalPrice().replace("¥", "");
                total += Double.parseDouble(priceStr) * entry.getValue();
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        return "¥" + total;
    }

    public static List<CartItem> getItems() {
        List<CartItem> list = new ArrayList<>();
        for (Map.Entry<Order, Integer> entry : cartMap.entrySet()) {
            list.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return list;
    }

//    public static List<Order> getOrderInfoListWithCount() {
//        List<Order> list = new ArrayList<>();
//        for (CartItem item : cartItems) {
//            Order p = item.getProduct();
//            if (p != null) {
//                Order copy = new Order();
//                copy.id = p.id;
//                copy.setName(p.getName());
//                copy.setPrice(p.getPrice());
//                copy.setCategory(p.getCategory());
//                copy.description = p.description;
//                copy.clazz = p.clazz;
//                copy.image = p.image;
//                copy.attributes = p.attributes;
//                copy.setCount(item.getCount());
//                list.add(copy);
//            }
//        }
//        return list;
//    }

    public static Map<Order, Integer> getMap() {
        return cartMap;
    }
}
