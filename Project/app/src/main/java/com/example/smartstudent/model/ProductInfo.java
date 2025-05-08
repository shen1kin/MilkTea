package com.example.smartstudent.model;

public class ProductInfo {
    private String name;
    private String price;
    private String category;

    public ProductInfo(String name, String price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
