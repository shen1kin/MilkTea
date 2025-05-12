package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilkTeaAttributeDao {

    //添加属性
// 添加属性
    public int addAttribute(String name) {
        // 1. 先查找是否存在该属性
        String selectSql = "SELECT id FROM MilkTeaAttribute WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 如果属性已存在，返回已有的ID
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. 如果属性不存在，就插入新属性
        String insertSql = "INSERT INTO MilkTeaAttribute (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // 获取自动生成的主键 ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // 返回新插入的ID
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // 如果发生异常或者没有插入成功，返回 -1
    }




    // 软删除属性
    public boolean softDeleteAttribute(int attributeId) {
        String query = "UPDATE MilkTeaAttribute SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, attributeId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAttribute() {
        List<String> values = new ArrayList<>();

        String sql = "SELECT MilkTeaAttribute.name " +
                "FROM MilkTeaAttribute " +
                "WHERE MilkTeaAttribute.is_deleted = 0";


        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                values.add(rs.getString("name")); // 正确获取列名
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }

    //是否存在
    public boolean existsAttribute(String attributeName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM MilkTeaAttribute WHERE name = ? AND is_deleted = 0";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, attributeName);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
