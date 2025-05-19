package com.example.model;

import java.util.List;

public class OrderItem {
    private String name;
    private String price;
    private int count;
    private int milk_tea_id;
    private String clazz;
    private String imageWay; //路径
    private byte[] image; //直接存储
    private List<OrderAttribute> attributes;

    // getter 和 setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMilk_tea_id(int milk_tea_id) {
        this.milk_tea_id = milk_tea_id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getMilk_tea_id() {
        return milk_tea_id;
    }

    public void setMilk_tae_id(int milk_tae_id) {
        this.milk_tea_id = milk_tae_id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getImageWay() {
        return imageWay;
    }

    public void setImageWay(String imageWay) {
        this.imageWay = imageWay;
    }

    public List<OrderAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OrderAttribute> attributes) {
        this.attributes = attributes;
    }

}
