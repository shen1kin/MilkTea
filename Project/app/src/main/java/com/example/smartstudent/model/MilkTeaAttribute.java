package com.example.smartstudent.model;

import java.io.Serializable;
import java.util.List;

public class MilkTeaAttribute implements Serializable {
    public String attribute;
    public List<String> attribute_value;


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
