package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MilkTeaItemDao {

    //添加奶茶基础信息
    public int addMilkTea(String name, double price, byte[] image, String description, int classID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO MilkTeaList (name, price, image, description, is_deleted, class_id) VALUES (?, ?, ?, ?, 0, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setBytes(3, image);
            stmt.setString(4, description);
            stmt.setInt(5, classID); // 索引改为 5（注意不是6）
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


}
