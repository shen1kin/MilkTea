package com.example.smartstudent.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {

    public String storeName;             // 门店名称
    public String orderTime;             // 下

    // 下单时间
    public String status;                // 订单状态：制作中、配送中、退款中、已完成等
    public String totalPrice;            // 总价（字符串）
    public int totalCount;               // 商品总件数
    public Map<OrderItem, Integer> orderItemInfos;     //传输方便前端

    public List<OrderItem> orderItemInfoList;  //接受方便后端

    private int userid;
    private String orderNum;//取餐码

    private String orderTimeEnd;
    private String pickupMethod;
    private String payMethod;
    private String address;
    private String remark;
    private int orderId;



    public Order(String storeName, String time, String status, String price, int totalCount, Map<OrderItem, Integer> orderItemInfos, int userid, String orderNum, String pickupMethod, String payMethod, String address, String remark) {
        this.storeName = storeName;
        this.orderTime = time;
        this.status = status;
        this.totalPrice = price;
        this.totalCount = totalCount;
        this.orderItemInfos = orderItemInfos;
        this.userid = userid;
        this.orderNum = orderNum;
        this.pickupMethod = pickupMethod;
        this.payMethod = payMethod;
        this.address = address;
        this.remark = remark;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItemInfoList() {
        return orderItemInfoList;
    }

    public void setOrderItemInfoList(List<OrderItem> orderItemInfoList) {
        this.orderItemInfoList = orderItemInfoList;
    }

    public Order() {

    }

    public Order(int orderId,int userid, String storeName, int totalCount, String totalPrice, String orderTime, String pickupMethod, String payMethod, String status, String address, String orderNum, String remark, String orderTimeEnd, List<OrderItem> itemList) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.orderTime = orderTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.orderItemInfoList = itemList;
        this.userid = userid;
        this.orderNum = orderNum;
        this.pickupMethod = pickupMethod;
        this.payMethod = payMethod;
        this.address = address;
        this.remark = remark;
        this.orderTimeEnd = orderTimeEnd;
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(String orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Map<OrderItem, Integer> getOrderItemInfos() {
        return orderItemInfos;
    }

    public void setOrderItemInfos(Map<OrderItem, Integer> orderItemInfos) {
        this.orderItemInfos = orderItemInfos;
    }
}
