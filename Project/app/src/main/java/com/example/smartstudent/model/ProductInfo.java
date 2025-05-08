package com.example.smartstudent.model;

public class ProductInfo {
    private String name;
    private String price;

    public ProductInfo(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
