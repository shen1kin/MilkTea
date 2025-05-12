package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Integer> addAttributeValueByAttributeName(String attributeName, List<String> values) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Integer> valueIds = new ArrayList<>();

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
                return valueIds; // 属性不存在
            }

            // 第二步：查已有的属性值（value -> id 映射）
            String selectExistingSql = "SELECT id, value FROM MilkTeaAttributeValue WHERE attribute_id = ? AND is_deleted = 0";
            stmt = conn.prepareStatement(selectExistingSql);
            stmt.setInt(1, attributeId);
            rs = stmt.executeQuery();

            Map<String, Integer> existingMap = new HashMap<>();
            while (rs.next()) {
                existingMap.put(rs.getString("value"), rs.getInt("id"));
            }

            // 筛选出需要插入的值
            List<String> newValues = new ArrayList<>();
            for (String val : values) {
                if (existingMap.containsKey(val)) {
                    valueIds.add(existingMap.get(val)); // 已有的直接加入返回列表
                } else {
                    newValues.add(val); // 新的待插入
                }
            }

            // 第三步：插入新的属性值
            if (!newValues.isEmpty()) {
                String insertSql = "INSERT INTO MilkTeaAttributeValue (attribute_id, value, is_deleted) VALUES (?, ?, 0)";
                stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

                for (String val : newValues) {
                    stmt.setInt(1, attributeId);
                    stmt.setString(2, val);
                    stmt.addBatch();
                }

                stmt.executeBatch();

                rs = stmt.getGeneratedKeys();
                while (rs.next()) {
                    valueIds.add(rs.getInt(1)); // 收集新插入的 ID
                }
            }

            return valueIds;

        } catch (Exception e) {
            e.printStackTrace();
            return valueIds;
        }
    }


}
