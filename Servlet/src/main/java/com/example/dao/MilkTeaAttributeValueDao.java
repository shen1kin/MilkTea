//package com.example.dao;
//
//import com.example.util.DBUtil;
//
//import java.sql.*;
//
//public class MilkTeaAttributeValueDao {
//
//    // 添加属性值
//    public boolean addAttributeValue(int attributeId, String value) {
//        String query = "INSERT INTO AttributeValue (attribute_id, value) VALUES (?, ?)";
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, attributeId);
//            statement.setString(2, value);
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // 软删除属性值
//    public boolean softDeleteAttributeValue(int attributeId, String value) {
//        String query = "UPDATE AttributeValue SET is_deleted = TRUE WHERE attribute_id = ? AND value = ?";
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, attributeId);
//            statement.setString(2, value);
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
