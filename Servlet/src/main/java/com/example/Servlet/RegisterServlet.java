//package com.example.Servlet;
//
//import com.example.model.User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import com.example.dao.UserDao;
//import jakarta.servlet.http.HttpSession;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebServlet("/register")
//public class RegisterServlet extends HttpServlet {
//    private UserDao userDao = new UserDao();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        request.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        BufferedReader reader = request.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//
//        JSONObject result = new JSONObject();
//        try {
//            JSONObject json = new JSONObject(sb.toString());
//            String username = json.getString("username");
//            String password = json.getString("password");
//
//            System.out.println("username" + username + "password" + password);
//
////            boolean isValid = userDao.checkLogin(username, password);
//            if (isValid) {
//                result.put("status", "success");
//
//                HttpSession session = request.getSession();
//                session.setAttribute("username", username);
//            } else {
//                result.put("status", "fail");
//                result.put("message", "用户名或密码错误");
//            }
//
//        } catch (JSONException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            try {
//                result.put("status", "fail");
//                result.put("message", "无效的 JSON 数据");
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//            }
//
//        }
//
//        PrintWriter out = response.getWriter();
//        out.print(result.toString());
//        out.flush();
//    }
//
//    }
//}