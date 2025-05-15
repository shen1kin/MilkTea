package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class MilkTeaAttribute {
    private String attribute;
    private List<String> attribute_value = new ArrayList<>();

    public MilkTeaAttribute() {}

    public MilkTeaAttribute(String attribute) {
        this.attribute = attribute;
    }

    // Getter、Setter 方法
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public List<String> getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(List<String> attribute_value) {
        this.attribute_value = attribute_value;
    }
}
