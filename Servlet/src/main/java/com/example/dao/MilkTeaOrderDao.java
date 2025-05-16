package com.example.dao;

import com.example.model.Order;
import com.example.model.OrderAttribute;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.List;

public class MilkTeaOrderDao {

    private final Connection conn = DBUtil.getConnection();

    public MilkTeaOrderDao() throws SQLException {
    }


    // 插入订单，返回生成的订单ID
    public int insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO MilkTeaOrder (userid, name, total_count, total_price, store_name, order_time, order_time_end, pickup_method, pay_method, status, address, order_num, remark, milk_tea_id, class) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getUserid());
            ps.setString(2, order.getName());
            ps.setInt(3, order.getTotalCount());
            ps.setString(4, order.getTotalPrice());
            ps.setString(5, order.getStoreName());
            ps.setString(6, order.getOrderTime());
            ps.setString(7, order.getOrderTimeEnd());
            ps.setString(8, order.getPickupMethod());
            ps.setString(9, order.getPayMethod());
            ps.setString(10, order.getStatus());
            ps.setString(11, order.getAddress());
            ps.setString(12, order.getOrderNum());
            ps.setString(13, order.getRemark());
            ps.setInt(14, order.getMilk_tae_id());
            ps.setString(15, order.getClazz()); // 如果用class字段建议改名clazz等，避免关键字冲突

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("插入订单失败，未影响任何行");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // 返回数据库自动生成的orderId
                } else {
                    throw new SQLException("插入订单失败，未获得生成的订单ID");
                }
            }
        }
    }

    // 插入订单属性关联表
    public void insertOrderAttributes(int orderId, List<OrderAttribute> attributes) throws SQLException {
        String sql = "INSERT INTO MilkTeaOrderAttributeMap (order_id, attribute_id, attribute_value_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderAttribute attr : attributes) {
                int attributeId = findAttributeId(attr.getAttribute());
                int attributeValueId = findAttributeValueId(attr.getAttribute(), attr.getAttribute_value());
                ps.setInt(1, orderId);
                ps.setInt(2, attributeId);
                ps.setInt(3, attributeValueId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private int findAttributeId(String attributeName) throws SQLException {
        String sql = "SELECT id FROM MilkTeaAttribute WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, attributeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("找不到属性名: " + attributeName);
                }
            }
        }
    }


    // 根据属性名和值查ID (示例方法，需要你根据表结构实现)
    private int findAttributeValueId(String attributeName, String attributeValue) throws SQLException {
        String sql = "SELECT av.id FROM MilkTeaAttributeValue av JOIN MilkTeaAttribute a ON av.attribute_id = a.id WHERE a.name = ? AND av.value = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, attributeName);
            ps.setString(2, attributeValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("找不到对应的属性值ID: " + attributeName + "=" + attributeValue);
                }
            }
        }
    }
}
