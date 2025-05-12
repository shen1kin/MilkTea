package com.example.smartstudent.model;

import java.util.List;

//存储奶茶类属性与属性值，用于传递到后端
public class MilkTeaAttributeAndValue {
    private String attribute;
    private List<String> attribute_value;

    public MilkTeaAttributeAndValue(String attribute, List<String> attribute_value) {
        this.attribute = attribute;
        this.attribute_value = attribute_value;
    }

    public String getAttribute() {
        return attribute;
    }

    public List<String> getAttribute_value() {
        return attribute_value;
    }
}
