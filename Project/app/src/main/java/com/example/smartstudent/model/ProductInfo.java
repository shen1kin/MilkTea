package com.example.smartstudent.model;

import java.util.Objects;

public class ProductInfo {
    private String name;
    private String price;
    private String category;

    public ProductInfo(String name, String price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // 👉 添加 getter 方法
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductInfo)) return false;
        ProductInfo other = (ProductInfo) obj;
        return name.equals(other.name) && price.equals(other.price) && category.equals(other.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }

}
