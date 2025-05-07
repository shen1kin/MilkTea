package com.example.smartstudent.database;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlUtil {
    private static final String TAG = "MysqlUtil";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://120.79.22.223:3306/MilkTea";
    private static final String USER = "root";
    private static final String PASSWORD = "Y2527y2527.";

    static {
        try {
            Class.forName(DRIVER);
            Log.d(TAG, "MySQL JDBC 驱动加载成功");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "加载MySQL JDBC驱动失败", e);
            throw new ExceptionInInitializerError("加载MySQL JDBC驱动失败");
        }
    }

    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.d(TAG, "数据库连接成功");
        } catch (SQLException e) {
            Log.e(TAG, "获取数据库连接失败", e);
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    Log.d(TAG, "数据库连接已关闭");
                }
            } catch (SQLException e) {
                Log.e(TAG, "关闭数据库连接时出错", e);
            }
        }
    }



}