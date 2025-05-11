//package com.example.dao;
//
//import com.example.util.DBUtil;
//
//import java.sql.*;
//
//public class MilkTeaAttributeServletDao {
//
//    // 添加奶茶
//    public boolean addMilkTea(String name) {
//        String query = "INSERT INTO MilkTea (name) VALUES (?)";
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, name);
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // 软删除奶茶
//    public boolean softDeleteMilkTea(int milkTeaId) {
//        String query = "UPDATE MilkTea SET is_deleted = TRUE WHERE id = ?";
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, milkTeaId);
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
