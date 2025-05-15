package com.example.smartstudent.model;

import com.example.smartstudent.model.ProductInfo;

public class CartItem {

    private ProductInfo product;
    private int count;

    public CartItem(ProductInfo product, int count) {
        this.product = product;
        this.count = count;
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
}
