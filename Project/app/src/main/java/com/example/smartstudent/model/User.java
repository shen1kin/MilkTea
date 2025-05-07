package com.example.smartstudent.model;

import androidx.annotation.NonNull;

public class User {
    private int id;          // 用户ID
    private String username; // 用户名
    private String account; // 用户名
    private String password; // 密码
    private String role; // 用户角色

    // 无参构造方法
    public User() {
    }



    // 带参数的构造方法
    public User(int id, String username, String account,String role) {
        this.id = id;
        this.username = username;
        this.account = account;
        this.role = role;
    }

    // 用于注册时没有id
    public User(String username, String account,String role) {
        this.username = username;
        this.account = account;
        this.role = role;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


}