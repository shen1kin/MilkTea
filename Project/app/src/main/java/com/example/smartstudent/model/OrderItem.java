package com.example.smartstudent.model;

import android.util.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OrderItem implements Serializable {

    private String orderId;

    private String name;

    private String price;
    private int count;


    private int milk_tae_id;
    private String clazz; // class 是关键字

    private String imageWay;
    private List<OrderAttribute> attributes;

    public OrderItem() {

    }


    //订单从加入购物车返回的构造函数
    //没有门店信息 下单时间 订单结束时间 配送方式 支付方式 订单状态 地址 订单号码 备注
    public OrderItem(String name, int count, String price, int milk_tae_id,
                     String clazz, List<OrderAttribute> attributes, String imageWay) {

        this.name = name;
        this.price = price;
        this.count = count;
        this.milk_tae_id = milk_tae_id;
        this.clazz = clazz;
        this.attributes = attributes;
        this.imageWay = imageWay;
    }

    public OrderItem(int milkTeaId, String name, String price, int count, String clazz, String imageWay, List<OrderAttribute> attrList) {
        this.milk_tae_id = milkTeaId;
        this.name = name;
        this.price = price;
        this.count = count;
        this.clazz = clazz;
        this.attributes = attrList;
        this.imageWay = imageWay;

    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob) return true;
        if (ob == null || getClass() != ob.getClass()) return false;

        OrderItem orderItem = (OrderItem) ob;

        Log.d("DiffentOrder1", "this id = " + milk_tae_id + " attrs = " + attributes);
        Log.d("DiffentOrder2", "orderItem id = " + orderItem.getMilk_tae_id() + " attrs = " + orderItem.attributes);

        return Objects.equals(milk_tae_id, orderItem.getMilk_tae_id())
                && Objects.equals(attributes, orderItem.attributes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(milk_tae_id, attributes);
    }

    public String getImageWay() {
        return imageWay;
    }

    public void setImageWay(String imageWay) {
        this.imageWay = imageWay;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


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



    public int getMilk_tae_id() {
        return milk_tae_id;
    }

    public void setMilk_tae_id(int milk_tae_id) {
        this.milk_tae_id = milk_tae_id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<OrderAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OrderAttribute> attributes) {
        this.attributes = attributes;
    }

    // 获取规格字符串（例如：热/大杯/无糖）
    public String getFormattedAttributes() {
        if (attributes == null || attributes.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (OrderAttribute attr : attributes) {
            String value = attr.getAttribute_value();
            if (value != null && !value.isEmpty()) {
                sb.append(value).append("/");
            }
        }

        // 去除末尾多余的“/”
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

}
