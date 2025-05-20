package com.example.model;

import java.util.List;

public class Order {
    private int orderid;
    private int userid;
    private String store_name;
    private int total_count;
    private String total_price;
    private String order_time;
    private String pickup_method;
    private String pay_method;
    private String status;
    private String address;
    private String order_num;
    private String remark;
    private String order_time_end;
    private List<OrderItem> orderItemInfos;

    public Order() {
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getPickup_method() {
        return pickup_method;
    }

    public void setPickup_method(String pickup_method) {
        this.pickup_method = pickup_method;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder_time_end() {
        return order_time_end;
    }

    public void setOrder_time_end(String order_time_end) {
        this.order_time_end = order_time_end;
    }

    public List<OrderItem> getOrderItemInfos() {
        return orderItemInfos;
    }

    public void setOrderItemInfos(List<OrderItem> orderItemInfos) {
        this.orderItemInfos = orderItemInfos;
    }
}

