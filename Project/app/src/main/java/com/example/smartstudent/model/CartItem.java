package com.example.smartstudent.model;

public class CartItem {
    public ProductInfo product;
    public int count;

    public CartItem(ProductInfo product, int count) {
        this.product = product;
        this.count = count;
    }
}
