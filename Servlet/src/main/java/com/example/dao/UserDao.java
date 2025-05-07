package com.example.dao;


import com.example.model.User;
import com.example.util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDao {
    // 根据用户名查找用户
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("account"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // 验证用户名和密码
    public boolean validateUser(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        //判断是否存在
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return password.equals(user.getPassword());
        }
        return false;
    }
    // 验证密码是否正确
    public Optional<User> checkLogin(String username, String password) {
        // 调用 UserDao 查询数据库中是否存在该用户
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(password)? userOpt : Optional.empty();
        }
        return Optional.empty();
    }

}