package com.example.dao;//package com.example.dao;
//
//import com.example.model.MilkTeaImage;
//import com.example.util.DBUtil;
//
//import java.awt.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class MilkTeaImageDao {
//    // 使用 DBUtil 工具类获取连接
//    public void insert(Image image) {
//        String sql = "INSERT INTO Image (milk_tea_id, image_data) VALUES (?, ?)";
//
//        // 使用 try-with-resources 自动管理连接和 PreparedStatement 的关闭
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//
//            stmt.setInt(1, image.getMilkTeaId());
//            stmt.setBytes(2, image.getImageData());  // 设置二进制图片数据
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 根据奶茶ID查询图片
//    public List<MilkTeaImage> getImagesByMilkTeaId(int milkTeaId) {
//        List<MilkTeaImage> images = new ArrayList<>();
//        String sql = "SELECT * FROM Image WHERE milk_tea_id = ?";
//
//        // 使用 try-with-resources 自动管理连接和 PreparedStatement 的关闭
//        try (Connection connection = DBUtil.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//
//            stmt.setInt(1, milkTeaId);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                MilkTeaImage image = new MilkTeaImage();
//                image.setId(rs.getInt("id"));
//                image.setMilkTeaId(rs.getInt("milk_tea_id"));
//                image.setImageData(rs.getBytes("image_data"));  // 获取二进制数据
//                image.setCreatedAt(rs.getTimestamp("created_at"));
//                images.add(image);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return images;
//    }
//}
//
