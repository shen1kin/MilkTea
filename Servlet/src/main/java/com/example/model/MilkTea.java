package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MilkTea {
    private int id;
    private String name;
    private boolean isDeleted;  // 是否已删除
    private List<MilkTeaAttribute> attributes = new ArrayList<>();// 奶茶的属性
    private String clazz;
    private double price;
    private byte[] image;
    private String description;

    public MilkTea() {

    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // 构造方法
    public MilkTea(int id, String name, boolean isDeleted, List<Map<String, String>> attributes) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public MilkTea(String name, List<Map<String, String>> attributes) {
        this.name = name;
        this.isDeleted = false;  // 默认未删除
    }

    public List<MilkTeaAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MilkTeaAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    // Getter 和 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }



    @Override
    public String toString() {
        return "MilkTea{id=" + id + ", name='" + name + "', isDeleted=" + isDeleted + ", attributes=" + attributes + '}';
    }


}
