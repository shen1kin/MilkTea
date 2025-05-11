package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MilkTeaAttributeValueDao {

    // 查询属性 属性值
    public List<String> getAttributeValuesByAttributeName(String attributeName) {
        List<String> values = new ArrayList<>();
        //查找属性值MilkTeaAttributeValue，从表MilkTeaAttribute中关联的ID用查找，并且他们都处于上架
        String sql = "SELECT MilkTeaAttributeValue.value " +
                "FROM MilkTeaAttributeValue " +
                "JOIN MilkTeaAttribute ON MilkTeaAttributeValue.attribute_id = MilkTeaAttribute.id " +
                "WHERE MilkTeaAttribute.name = ? AND MilkTeaAttributeValue.is_deleted = 0 AND MilkTeaAttribute.is_deleted = 0";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attributeName); // 设置属性名称作为查询条件
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                values.add(rs.getString("value")); // 获取查询结果中的属性值
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }

    // 添加属性值（根据属性名称）
    public boolean addAttributeValueByAttributeName(String attributeName, List<String> values) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // 第一步：查属性id
            String queryIdSql = "SELECT id FROM MilkTeaAttribute WHERE name = ? AND is_deleted = 0";
            stmt = conn.prepareStatement(queryIdSql);
            stmt.setString(1, attributeName);
            rs = stmt.executeQuery();

            int attributeId = -1;
            if (rs.next()) {
                attributeId = rs.getInt("id");
            } else {
                // 没查到该属性，无法添加
                return false;
            }

            // 第二步：插入属性值
            // 插入属性值
            String insertSql = "INSERT INTO MilkTeaAttributeValue (attribute_id, value, is_deleted) VALUES (?, ?, 0)";
            stmt = conn.prepareStatement(insertSql);

            for (String val : values) {
                stmt.setInt(1, attributeId);
                stmt.setString(2, val);
                stmt.addBatch(); // 批量添加
            }

            int[] results = stmt.executeBatch(); // 批量执行
            return results.length == values.size();

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}
