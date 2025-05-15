package com.example.smartstudent.cart;

import static android.os.Build.VERSION_CODES.P;

import com.example.smartstudent.model.CartItem;
import com.example.smartstudent.model.ProductInfo;

import java.util.*;

public class CartManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    private static final Map<ProductInfo, Integer> cartMap = new LinkedHashMap<>();

    public static void add(ProductInfo product) {
        int count = cartMap.getOrDefault(product, 0);
        cartMap.put(product, count + 1);
    }

    public static void decrease(ProductInfo product) {
        int count = cartMap.getOrDefault(product, 0);
        if (count > 1) {
            cartMap.put(product, count - 1);
        } else {
            cartMap.remove(product);
        }
    }

    public static void remove(ProductInfo product) {
        cartMap.remove(product);
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
        for (Map.Entry<ProductInfo, Integer> entry : cartMap.entrySet()) {
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
        for (Map.Entry<ProductInfo, Integer> entry : cartMap.entrySet()) {
            list.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public static List<ProductInfo> getProductInfoListWithCount() {
        List<ProductInfo> list = new ArrayList<>();
        for (CartItem item : cartItems) {
            ProductInfo p = item.getProduct();
            if (p != null) {
                ProductInfo copy = new ProductInfo();
                copy.id = p.id;
                copy.setName(p.getName());
                copy.setPrice(p.getPrice());
                copy.setCategory(p.getCategory());
                copy.description = p.description;
                copy.clazz = p.clazz;
                copy.image = p.image;
                copy.attributes = p.attributes;
                copy.setCount(item.getCount());
                list.add(copy);
            }
        }
        return list;
    }






    public static Map<ProductInfo, Integer> getMap() {
        return cartMap;
    }
}
