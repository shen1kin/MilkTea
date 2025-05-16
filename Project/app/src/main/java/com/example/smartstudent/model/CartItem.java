package com.example.smartstudent.model;

import com.example.smartstudent.model.ProductInfo;

public class CartItem {

    private ProductInfo product;
    private int count;

    private Order orderInfo;

    public CartItem(ProductInfo product, int count) {
        this.product = product;
        this.count = count;
    }

    public CartItem(Order orderInfo, int count) {
        this.orderInfo = orderInfo;
        this.count = count;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public void setOrderInfo(Order orderInfo) {
        this.orderInfo = orderInfo;
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

    public Order getOrderInfo() {
        return orderInfo;
    }
}
