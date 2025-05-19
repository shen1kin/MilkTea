package com.example.smartstudent.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private ProductInfo product;
    private int count;

    private OrderItem orderItemInfo;

    public CartItem(ProductInfo product, int count) {
        this.product = product;
        this.count = count;
    }

    public CartItem(OrderItem orderItemInfo, int count) {
        this.orderItemInfo = orderItemInfo;
        this.count = count;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public void setOrderInfo(OrderItem orderItemInfo) {
        this.orderItemInfo = orderItemInfo;
    }

    // ✅ 添加 getter 方法
    public ProductInfo getProduct() {
        return product;
    }


    public int getCount() {
        return count;
    }

    // 如果有必要，也可以添加 setter 方法
    public void setCount(int count) {
        this.count = count;
    }

    public OrderItem getOrderInfo() {
        return orderItemInfo;
    }
}
