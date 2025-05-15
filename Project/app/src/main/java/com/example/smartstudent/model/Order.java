package com.example.smartstudent.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    public String storeName;           // 门店名称
    public String orderTime;           // 下单时间
    public String status;              // 订单状态：制作中、配送中、退款中、已完成等
    public String totalPrice;          // 总价（字符串）
    public int totalCount;             // 商品总件数
    public List<Integer> productImageResIds; // 商品图片资源ID列表（如 R.drawable.xxx）

    public Order(String storeName, String orderTime, String status,
                 String totalPrice, int totalCount, List<Integer> productImageResIds) {
        this.storeName = storeName;
        this.orderTime = orderTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.productImageResIds = productImageResIds;
    }
}
