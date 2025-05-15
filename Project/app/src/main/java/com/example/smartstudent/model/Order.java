package com.example.smartstudent.model;

public class Order {
    public String storeName;
    public String productDesc;
    public String totalPrice;
    public String status;

    public Order(String storeName, String productDesc, String totalPrice, String status) {
        this.storeName = storeName;
        this.productDesc = productDesc;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
