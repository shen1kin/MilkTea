# 题目三：主要开发出一款基于Android

平台的学生信息管理系统。

本软件从功能上进行划分以下模块，用户角色分为管理员和学生。管理员功能主要包括课表管理模块、成绩管理模块(成绩的增删改、导出等)、学生信息管理模块、密码修改模块。学生登录包括个人基本信息查看，课表查询，成绩查看和密码修改等功能。

注：以上功能点还可以根据实际需要进行扩展。同学也可以将系分为统客户端和服务端来完成。





## 文件结构

├── 管理员功能
│ ├── 学生信息管理
│ │ ├── 添加学生 Add Student
│ │ ├── 修改学生 Modify Student
│ │ ├── 删除学生 Delete Student
│ │ └── 查询学生 Query Student
│ ├── 课表管理
│ │ ├── 添加课程 Add Course
│ │ ├── 修改课程 Modify Course
│ │ ├── 删除课程 Delete Course
│ │ └── 课程安排 Course Arrangement
│ ├── 成绩管理
│ │ ├── 添加成绩 Add Grade
│ │ ├── 修改成绩 Modify Grade
│ │ ├── 删除成绩 Delete Grade
│ │ ├── 查询成绩 Query Grade
│ │ └── 成绩导出 Grade Export
│ └── 密码修改 Password Modification
│
└── 学生功能
├── 查看个人基本信息 View Personal Basic Information
├── 查询课表 Query Class Schedule
├── 查询成绩 Query Grade
└── 密码修改 Password Modification





项目根目录
├── app
│   ├── build
│   ├── libs
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │       └── yourpackage
│   │   │           ├── activity       // 页面Activity
│   │   │           │   ├── MainActivity.java      // 主界面（带底部导航栏）
│   │   │           │   ├── HomeActivity.java      // 首页
│   │   │           │   ├── CourseActivity.java    // 课表查询
│   │   │           │   ├── ScoreActivity.java     // 成绩查询
│   │   │           │   ├── ProfileActivity.java   // 我的（个人信息）
│   │   │           │   └── LoginActivity.java     // 登录页
│   │   │           ├── fragment       // 各功能Fragment（如果用Fragment的话）
│   │   │           │   ├── HomeFragment.java
│   │   │           │   ├── CourseFragment.java
│   │   │           │   ├── ScoreFragment.java
│   │   │           │   └── ProfileFragment.java
│   │   │           ├── adapter        // RecyclerView适配器
│   │   │           │   ├── CourseAdapter.java
│   │   │           │   └── ScoreAdapter.java
│   │   │           ├── model          // 数据模型
│   │   │           │   ├── Student.java
│   │   │           │   ├── Course.java
│   │   │           │   └── Score.java
│   │   │           ├── network        // 网络模块（如果有客户端服务器）
│   │   │           │   ├── ApiService.java
│   │   │           │   └── RetrofitClient.java
│   │   │           └── util           // 工具类
│   │   │               ├── PasswordUtils.java
│   │   │               └── SharedPreferencesUtil.java
│   │   ├── res
│   │   │   ├── drawable
│   │   │   ├── layout
│   │   │   │   ├── activity_main.xml           // 主界面布局（含底部导航栏）
│   │   │   │   ├── activity_home.xml           // 首页布局
│   │   │   │   ├── activity_course.xml         // 课表布局
│   │   │   │   ├── activity_score.xml          // 成绩布局
│   │   │   │   ├── activity_profile.xml        // 我的布局
│   │   │   │   └── activity_login.xml          // 登录布局
│   │   │   ├── menu
│   │   │   │   └── bottom_nav_menu.xml         // 底部导航栏菜单项
│   │   │   ├── mipmap
│   │   │   ├── values
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   ├── styles.xml
│   │   │   │   └── dimens.xml
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle（模块）
│   └── proguard-rules.pro
├── build.gradle（项目）
├── settings.gradle
└── gradle/





## SmartStudent  学生管理系统



### 1. 数据库管理

[博客]: https://blog.csdn.net/qq_29293605/article/details/121335426?ops_request_misc=%257B%2522request%255Fid%2522%253A%25226e296df94ef16a85882be9813d1ae86b%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&amp;request_id=6e296df94ef16a85882be9813d1ae86b&amp;biz_id=0&amp;utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_click~default-1-121335426-null-null.142^v102^pc_search_result_base3&amp;utm_term=android%20mysql&amp;spm=1018.2226.3001.4187



# 使用 try-with-resources 语法完整实现

以下是使用 try-with-resources 语法完整实现 MySQL 数据库操作的示例代码：

## 1. 基础查询示例

```java
public static void queryUsers() {
    String sql = "SELECT id, username, email FROM users";
    
    try (Connection conn = MysqlUtil.getConn();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            
            System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

## 2. 带参数的查询（使用 PreparedStatement）

java

java

复制

```java
public static User getUserById(int userId) {
    String sql = "SELECT id, username, email FROM users WHERE id = ?";
    
    try (Connection conn = MysqlUtil.getConn();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, userId);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
```

## 3. 插入数据示例

java

java

复制

```java
public static boolean insertUser(User user) {
    String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    
    try (Connection conn = MysqlUtil.getConn();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getEmail());
        
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

## 4. 事务处理示例

java

java

复制

```java
public static boolean transferMoney(int fromId, int toId, double amount) {
    String sql1 = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
    String sql2 = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
    
    try (Connection conn = MysqlUtil.getConn()) {
        // 关闭自动提交
        conn.setAutoCommit(false);
        
        try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
            
            // 扣款
            pstmt1.setDouble(1, amount);
            pstmt1.setInt(2, fromId);
            pstmt1.executeUpdate();
            
            // 入账
            pstmt2.setDouble(1, amount);
            pstmt2.setInt(2, toId);
            pstmt2.executeUpdate();
            
            // 提交事务
            conn.commit();
            return true;
        } catch (SQLException e) {
            // 回滚事务
            conn.rollback();
            e.printStackTrace();
            return false;
        } finally {
            // 恢复自动提交
            conn.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

## 5. 批量操作示例

java

java

复制

```java
public static int[] batchInsertUsers(List<User> users) {
    String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    
    try (Connection conn = MysqlUtil.getConn();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        for (User user : users) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.addBatch();
        }
        
        return pstmt.executeBatch();
    } catch (SQLException e) {
        e.printStackTrace();
        return new int[0];
    }
}
```

## 关键说明

1. **资源自动关闭**：
   - 所有实现了 `AutoCloseable` 的资源（Connection, Statement, ResultSet）都会在 try 块结束时自动关闭
   - 关闭顺序与声明顺序相反（ResultSet → Statement → Connection）
2. **嵌套 try-with-resources**：
   - 可以嵌套使用，如事务处理示例所示
   - 内层资源会先于外层资源关闭
3. **异常处理**：
   - 仍然需要捕获 SQLException
   - 关闭操作抛出的异常会被抑制（可通过 Throwable.getSuppressed() 获取）
4. **最佳实践**：
   - 每个资源单独声明在 try 子句中
   - 避免在 try 块外部保留资源引用
   - 复杂的操作可以拆分为多个方法

这种写法比手动调用 close() 更安全可靠，能确保在任何情况下（包括异常发生时）资源都会被正确释放。

