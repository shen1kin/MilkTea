package com.example.model;

import java.util.List;
import java.util.Map;

public class MilkTea {
    private int id;
    private String name;
    private boolean isDeleted;  // 是否已删除
    private List<Map<String, String>> attributes;  // 奶茶的属性

    // 构造方法
    public MilkTea(int id, String name, boolean isDeleted, List<Map<String, String>> attributes) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.attributes = attributes;
    }

    public MilkTea(String name, List<Map<String, String>> attributes) {
        this.name = name;
        this.isDeleted = false;  // 默认未删除
        this.attributes = attributes;
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

    public List<Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Map<String, String>> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "MilkTea{id=" + id + ", name='" + name + "', isDeleted=" + isDeleted + ", attributes=" + attributes + '}';
    }
}
