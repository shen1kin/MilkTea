package com.example.Servlet;

import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求体编码为 UTF-8，防止中文乱码
        request.setCharacterEncoding("UTF-8");

        // 设置响应类型为 JSON，编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 从请求体中读取客户端（Android端）发来的 JSON 数据
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // 构建用于响应给客户端的 JSON 对象，用于返回
        JSONObject result = new JSONObject();

        try {
            // 解析请求体中的 JSON 数据
            JSONObject json = new JSONObject(sb.toString());
            //获取数据(JSON)
            String username = json.getString("username"); // 获取用户名
            String account = json.getString("account"); // 获取用户名
            String password = json.getString("password"); // 获取密码
            String role = json.getString("role"); // 获取密码

            // 打印用户名和密码到控制台（调试用）
            System.out.println("register: "+ "username: " + username + " password: " + password);

            // 调用 DAO 中的校验方法进行用户验证
            Optional<User> userOpt = userDao.addUser(username,account,password,role);

            if (userOpt.isPresent()) {
                // 注册成功，返回成功状态
                result.put("status", "success");
                //返回用户信息
                JSONObject userJson = new JSONObject();
                User user = userOpt.get();
                userJson.put("id", user.getId());
                userJson.put("username", user.getUsername());
                userJson.put("account", user.getAccount());
                userJson.put("role", user.getRole());
                //返回JSON
                result.put("user", userJson);

            } else {
                // 注册失败，返回失败信息
                result.put("status", "fail");
                result.put("message", "注册失败");
            }

        } catch (JSONException e) {
            // 如果 JSON 格式不正确，返回 400 错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                result.put("status", "fail");
                result.put("message", "无效的 JSON 数据");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace(); // 打印异常信息
            }
        }

        // 将响应写回给客户端
        PrintWriter out = response.getWriter();
        out.print(result.toString()); // 输出 JSON 字符串
        out.flush(); // 刷新输出流
    }
}