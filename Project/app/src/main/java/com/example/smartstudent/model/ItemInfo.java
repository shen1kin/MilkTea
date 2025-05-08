package com.example.smartstudent.model;

import androidx.annotation.NonNull;

public class ItemInfo {
    private int item_id;
    private String  item_name;
    private String  item_num;
    private String  item_state;
    private String  item_price;

    public ItemInfo(int item_id, String item_name, String item_num, String item_state, String item_price) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_num = item_num;
        this.item_state = item_state;
        this.item_price = item_price;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getItem_state() {
        return item_state;
    }

    public void setItem_state(String item_state) {
        this.item_state = item_state;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemInfo{" +
                "item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", item_num='" + item_num + '\'' +
                ", item_state='" + item_state + '\'' +
                ", item_price='" + item_price + '\'' +
                '}';
    }
}
