package com.example.Servlet;

import com.example.dao.MilkTeaAttributeDao;
import com.example.dao.MilkTeaAttributeValueDao;
import com.example.dao.MilkTeaClassDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet("/milk-tea-class")  // URL 以 / 开头
public class MilkTeaClassServlet extends HttpServlet {
    private MilkTeaAttributeValueDao MilkTeaAttributeValueDao = new MilkTeaAttributeValueDao();
    private MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();
    private MilkTeaClassDao MilkTeaClassDao = new MilkTeaClassDao();

    // 返回全部属性值
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应类型为 JSON，编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<String> classes = MilkTeaClassDao.getClasses();
        System.out.print("class" + ": ");
        for(String s :classes) {
            System.out.print(s + ", ");
        }
        System.out.println();

        JSONObject result = new JSONObject();
        try {
            if (!classes.isEmpty()) {
                // 查询成功
                result.put("status", "success");

                // 返回属性值
                JSONObject classJson = new JSONObject();
                classJson.put("list", classes);

                // 返回 JSON 数据
                result.put("class", classJson);
            } else {
                // 查询失败，返回状态信息
                result.put("status", "fail");
                result.put("message", "未查询到分类");
                }

            } catch (JSONException e) {
            // 捕获 JSON 格式错误
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                result.put("status", "fail");
                result.put("message", "无效的 JSON 数据");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();  // 打印异常
        }
        } catch (Exception e) {
            // 捕获其他未知异常
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                result.put("status", "error");
                result.put("message", "服务器内部错误");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();  // 打印异常
            }
        }

        PrintWriter out = response.getWriter();
        out.print(result.toString());
        out.flush();
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();

        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            // 只处理 className
            String className = json.getString("class_name");

            int classId = MilkTeaClassDao.addClass(className);

            if (classId > 0) {
                // 插入或查询成功
                result.put("status", "success");
                result.put("message", "分类处理成功");
                result.put("class_id", classId);
            } else {
                // 插入失败
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                result.put("status", "fail");
                result.put("message", "分类处理失败");
            }

        } catch (JSONException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                result.put("status", "fail");
                result.put("message", "无效的 JSON 数据");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                result.put("status", "error");
                result.put("message", "服务器内部错误: " + e.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        PrintWriter out = response.getWriter();
        out.print(result.toString());
        out.flush();
    }



}
