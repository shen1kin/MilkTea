package com.example.dao;

import com.example.model.Order;
import com.example.model.Order11;
import com.example.model.OrderAttribute;
import com.example.model.OrderItem;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilkTeaOrderDao {

    private final Connection conn = DBUtil.getConnection();

    public MilkTeaOrderDao() throws SQLException {
    }

    // 查询的所有订单及其明细
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();

        String orderSql = "SELECT * FROM MilkTeaOrder";
        try (PreparedStatement ps = conn.prepareStatement(orderSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                int orderId = rs.getInt("order_id");

                order.setOrderid(orderId);
                order.setUserid(rs.getInt("userid"));
                order.setStore_name(rs.getString("store_name"));
                order.setTotal_count(rs.getInt("total_count"));
                order.setTotal_price(rs.getString("total_price"));
                order.setOrder_time(rs.getString("order_time"));
                order.setPickup_method(rs.getString("pickup_method"));
                order.setPay_method(rs.getString("pay_method"));
                order.setStatus(rs.getString("status"));
                order.setAddress(rs.getString("address"));
                order.setOrder_num(rs.getString("order_num"));
                order.setRemark(rs.getString("remark"));

                String orderTimeEnd = rs.getString("order_time_end");
                if (orderTimeEnd == null) {
                    orderTimeEnd = "未完成";
                }
                order.setOrder_time_end(orderTimeEnd);

                // 查询该订单的订单项
                order.setOrderItemInfos(getOrderItemsByOrderId(orderId));
                orders.add(order);
            }
        }

        return orders;
    }



    // 查询某个用户的所有订单及其明细
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();

        // 1. 查询订单主表
        String orderSql = "SELECT * FROM MilkTeaOrder WHERE userid = ?";
        try (PreparedStatement ps = conn.prepareStatement(orderSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    int orderId = rs.getInt("order_id"); // 记得主键叫id
                    order.setOrderid(rs.getInt(orderId));
                    order.setUserid(rs.getInt("userid"));
                    order.setStore_name(rs.getString("store_name"));
                    order.setTotal_count(rs.getInt("total_count"));
                    order.setTotal_price(rs.getString("total_price"));
                    order.setOrder_time(rs.getString("order_time"));
                    order.setPickup_method(rs.getString("pickup_method"));
                    order.setPay_method(rs.getString("pay_method"));
                    order.setStatus(rs.getString("status"));
                    order.setAddress(rs.getString("address"));
                    order.setOrder_num(rs.getString("order_num"));
                    order.setRemark(rs.getString("remark"));
                    String orderTimeEnd = rs.getString("order_time_end");
                    if (orderTimeEnd == null) {
                        orderTimeEnd = "未完成"; // 或 ""
                    }
                    order.setOrder_time_end(orderTimeEnd);


                    // 查询该订单的订单项
                    order.setOrderItemInfos(getOrderItemsByOrderId(orderId));
                    orders.add(order);
                }
            }
        }

        return orders;
    }

    // 获取某个订单的所有订单项
    private List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();

        // 按 milk_tea_id 查询该订单下的所有奶茶及其属性
        String sql = "SELECT map.milk_tea_id, mt.name, mt.price, mt.class_id, mt.image, " +
                "attr.name AS attribute_name, val.value AS attribute_value " +
                "FROM MilkTeaOrderAttributeMap map " +
                "JOIN MilkTeaList mt ON map.milk_tea_id = mt.id " +
                "JOIN MilkTeaAttribute attr ON map.attribute_id = attr.id " +
                "JOIN MilkTeaAttributeValue val ON map.attribute_value_id = val.id " +
                "WHERE map.order_id = ?";

        Map<Integer, OrderItem> itemMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int milkTeaId = rs.getInt("milk_tea_id");

                    // 如果该奶茶还没出现，构造新的 OrderItem
                    OrderItem item = itemMap.get(milkTeaId);
                    if (item == null) {
                        item = new OrderItem();
                        item.setMilk_tae_id(milkTeaId);
                        item.setName(rs.getString("name"));
                        item.setPrice(rs.getString("price"));
                        item.setClazz(String.valueOf(rs.getInt("class_id"))); // 你看是否需要转成 class 名称
                        item.setImage(rs.getBytes("image")); // Byte[]
                        item.setAttributes(new ArrayList<>());
                        itemMap.put(milkTeaId, item);
                    }

                    // 添加属性
                    OrderAttribute attr = new OrderAttribute();
                    attr.setAttribute(rs.getString("attribute_name"));
                    attr.setAttribute_value(rs.getString("attribute_value"));
                    item.getAttributes().add(attr);
                }
            }
        }

        // 添加到结果列表
        items.addAll(itemMap.values());
        return items;
    }


    // 获取某个订单项的所有属性信息
    private List<OrderAttribute> getAttributesByOrderIdAndMilkTeaId(int orderId, int milkTeaId) throws SQLException {
        List<OrderAttribute> attributes = new ArrayList<>();

        String sql = "SELECT a.name AS attribute_name, av.value AS attribute_value " +
                "FROM MilkTeaOrderAttributeMap map " +
                "JOIN MilkTeaAttribute a ON map.attribute_id = a.id " +
                "JOIN MilkTeaAttributeValue av ON map.attribute_value_id = av.id " +
                "WHERE map.order_id = ? AND map.milk_tea_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, milkTeaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderAttribute attr = new OrderAttribute();
                    attr.setAttribute(rs.getString("attribute_name"));
                    attr.setAttribute_value(rs.getString("attribute_value"));
                    attributes.add(attr);
                }
            }
        }

        return attributes;
    }






    public int insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO MilkTeaOrder (userid, total_count, total_price, store_name, order_time, pickup_method, pay_method, status, address, order_num, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserid());
            ps.setInt(2, order.getTotal_count());
            ps.setString(3, order.getTotal_price());
            ps.setString(4, order.getStore_name());
            ps.setString(5, order.getOrder_time());
            ps.setString(6, order.getPickup_method());
            ps.setString(7, order.getPay_method());
            ps.setString(8, order.getStatus());
            ps.setString(9, order.getAddress());
            ps.setString(10, order.getOrder_num());
            ps.setString(11, order.getRemark());

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





//    // 插入订单，返回生成的订单ID
//    public int insertOrder(Order order) throws SQLException {
//        String sql = "INSERT INTO MilkTeaOrder (userid, name, total_count, total_price, store_name, order_time, order_time_end, pickup_method,
//        pay_method, status, address, order_num, remark, milk_tea_id, class) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, order.getUserid());
//            ps.setString(2, order.getName());
//            ps.setInt(3, order.getTotalCount());
//            ps.setString(4, order.getTotalPrice());
//            ps.setString(5, order.getStoreName());
//            ps.setString(6, order.getOrderTime());
//            ps.setString(7, order.getOrderTimeEnd());
//            ps.setString(8, order.getPickupMethod());
//            ps.setString(9, order.getPayMethod());
//            ps.setString(10, order.getStatus());
//            ps.setString(11, order.getAddress());
//            ps.setString(12, order.getOrderNum());
//            ps.setString(13, order.getRemark());
//            ps.setInt(14, order.getMilk_tae_id());
//            ps.setString(15, order.getClazz()); // 如果用class字段建议改名clazz等，避免关键字冲突
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("插入订单失败，未影响任何行");
//            }
//
//            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    return generatedKeys.getInt(1); // 返回数据库自动生成的orderId
//                } else {
//                    throw new SQLException("插入订单失败，未获得生成的订单ID");
//                }
//            }
//        }
//    }

    // 插入订单属性关联表
// 修改后的插入订单属性关联表方法，新增milk_tea_id参数
    public void insertOrderAttributes(int orderId, int milkTeaId, List<OrderAttribute> attributes) throws SQLException {
        String sql = "INSERT INTO MilkTeaOrderAttributeMap (order_id, attribute_id, attribute_value_id, milk_tea_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderAttribute attr : attributes) {
                int attributeId = findAttributeId(attr.getAttribute());
                int attributeValueId = findAttributeValueId(attr.getAttribute(), attr.getAttribute_value());
                ps.setInt(1, orderId);
                ps.setInt(2, attributeId);
                ps.setInt(3, attributeValueId);
                ps.setInt(4, milkTeaId); // 新增字段
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


    public boolean updateOrderStatusAndOrderTimeEnd(int orderId, String status, String orderTimeEnd) {
        String sql = "UPDATE MilkTeaOrder SET status = ?, order_time_end = ? WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, orderTimeEnd);
            ps.setInt(3, orderId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 今日订单总数
    public int getTodayOrderCount() throws SQLException {
        String sql = "SELECT SUM(total_count) FROM MilkTeaOrder WHERE TO_DAYS(order_time) = TO_DAYS(NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // 今日总收入
    public double getTodayTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(CAST(REPLACE(total_price, '¥', '') AS DECIMAL(10,2))) FROM MilkTeaOrder WHERE TO_DAYS(order_time) = TO_DAYS(NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        }
    }


}
