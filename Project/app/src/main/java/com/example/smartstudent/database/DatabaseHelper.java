package com.example.smartstudent.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MilkTea.db";
    private static final int DATABASE_VERSION = 1;

    // 构造函数（不包含具体表逻辑）
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 不直接建表，由 DAO 类负责
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 由 DAO 类处理各自的表升级
    }

    // 提供获取数据库的方法
    public SQLiteDatabase getDb() {
        return this.getWritableDatabase();
    }
}