package com.example.smartstudent.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartstudent.model.User;

public class UserDao {
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ACCOUNT = "account";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    private final SQLiteDatabase db;

    public UserDao(SQLiteDatabase db) {
        this.db = db;
        createTable();
    }

    // 添加 UNIQUE 约束到 account 字段
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_ACCOUNT + " TEXT UNIQUE, " + // 唯一约束
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(sql);
    }

    // 根据账号查询用户
    public User getUserByAccount(String account) {
        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_ACCOUNT + " = ?",
                new String[]{account},
                null, null, null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setAccount(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
        }
        cursor.close();
        return user;
    }

    // 注册用户
    public long addUser(String username, String account, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ACCOUNT, account);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        return db.insert(TABLE_USERS, null, values);
    }
}