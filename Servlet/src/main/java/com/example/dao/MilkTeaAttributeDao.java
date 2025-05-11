package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilkTeaAttributeDao {

    // 添加属性
    public boolean addAttribute(String name) {
        String query = "INSERT INTO Attribute (name) VALUES (?)";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查询属性 属性值
    public  List<String> getAttributeValuesByAttributeName(String attributeName) {
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



    // 软删除属性
    public boolean softDeleteAttribute(int attributeId) {
        String query = "UPDATE Attribute SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, attributeId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
