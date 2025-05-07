package com.example.model;

public class User {
    private int id;
    private String username;
    private String account;   // 姓名
    private String password;
    private String role;      // 用户角色（如 "admin", "user"）
    private String created_at; // 账户创建时间


    // 构造方法
    public User() {
    }
    //登录
    public User(int id, String username, String account,String password, String role, String created_at) {
        this.id = id;
        this.username = username;
        this.account = account;
        this.password = password;
        this.role = role;
        this.created_at = created_at;

    }
    //注册
    public User(String username, String account, String password) {
        this.username = username;
        this.password = password;
        this.account = account;
    }

    // Getter 和 Setter 方法
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", created_at=" + created_at +
                ", account='" + account + '\'' +
                '}';
    }
}