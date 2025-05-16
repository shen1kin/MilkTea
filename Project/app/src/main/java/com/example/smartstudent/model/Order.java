package com.example.smartstudent.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {

    public String storeName;             // 门店名称
    public String orderTime;             // 下单时间
    public String status;                // 订单状态：制作中、配送中、退款中、已完成等
    public String totalPrice;            // 总价（字符串）
    public int totalCount;               // 商品总件数
    public List<ProductInfo> productList; //  商品列表（包含完整商品信息）

    private String orderId;
    private String userid;
    private String name;
    private String orderTimeEnd;
    private String pickupMethod;
    private String payMethod;
    private String address;
    private String orderNum;
    private String remark;
    private int milk_tae_id;
    private String clazz; // class 是关键字

    private String imageWay;
    private List<OrderAttribute> attributes;

    public Order(String storeName, String orderTime, String status,
                 String totalPrice, int totalCount, List<ProductInfo> productList) {
        this.storeName = storeName;
        this.orderTime = orderTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.productList = productList;
    }

    public Order() {

    }

    public String getImageWay() {
        return imageWay;
    }

    public void setImageWay(String imageWay) {
        this.imageWay = imageWay;
    }

    //订单从加入购物车返回的构造函数
    //没有门店信息 下单时间 订单结束时间 配送方式 支付方式 订单状态 地址 订单号码 备注
    public Order(String name, int totalCount, String totalPrice, int milk_tae_id, String clazz,List<OrderAttribute> attributes,String imageWay) {
        this.name = name;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
        this.milk_tae_id = milk_tae_id;
        this.clazz = clazz;
        this.attributes = attributes;
        this.imageWay = imageWay;
    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob) return true;
        if (ob == null || getClass() != ob.getClass()) return false;

        Order order = (Order) ob;

        Log.d("DiffentOrder1", "this id = " + milk_tae_id + " attrs = " + attributes);
        Log.d("DiffentOrder2", "order id = " + order.getMilk_tae_id() + " attrs = " + order.attributes);

        return Objects.equals(milk_tae_id, order.getMilk_tae_id())
                && Objects.equals(attributes, order.attributes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(milk_tae_id, attributes);
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ProductInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductInfo> productList) {
        this.productList = productList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(String orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getPickupMethod() {
        return pickupMethod;
    }

    public void setPickupMethod(String pickupMethod) {
        this.pickupMethod = pickupMethod;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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



}
