package com.example.smartstudent.model;

import java.io.Serializable;

public class OrderAttribute implements Serializable {
    private String attribute;
    private String attribute_value;

    public OrderAttribute(String attribute, String attribute_value) {
        this.attribute = attribute;
        this.attribute_value = attribute_value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderAttribute that = (OrderAttribute) o;

        return attribute.equals(that.attribute)
                && attribute_value.equals(that.attribute_value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(attribute, attribute_value);
    }



    // Getters and setters

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }
}
