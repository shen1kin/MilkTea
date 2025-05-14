package com.example.dao;

import com.example.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MilkTeaClassDao {

    //添加属性
// 添加属性
    public int addClass(String name) {
        // 1. 先查找是否存在该属性
        String selectSql = "SELECT id FROM MilkTeaClass WHERE name = ?";
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
        String insertSql = "INSERT INTO MilkTeaClass (name) VALUES (?)";
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

    public List<String> getClasses() {
        List<String> values = new ArrayList<>();

        String sql = "SELECT MilkTeaClass.name " +
                "FROM MilkTeaClass " +
                "WHERE MilkTeaClass.is_deleted = 0";


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


}
