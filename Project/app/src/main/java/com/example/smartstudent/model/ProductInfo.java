package com.example.smartstudent.model;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Objects;

public class ProductInfo {
    public int id;
    private String name;
    private String price;
    private String category;


    public String description;
    public String clazz;
    public Bitmap image; // 用于展示的图片

    public ProductInfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public List<MilkTeaAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MilkTeaAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<MilkTeaAttribute> attributes;



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
