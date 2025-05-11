package com.example.Servlet;

import com.example.dao.MilkTeaAttributeDao;
import com.example.dao.MilkTeaAttributeValueDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/milk-tea-attribute")  // URL 以 / 开头
public class MilkTeaAttributeServlet extends HttpServlet {
    private MilkTeaAttributeValueDao MilkTeaAttributeValueDao = new MilkTeaAttributeValueDao();
    private MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();

    // 返回全部属性值
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应类型为 JSON，编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<String> attributeList = MilkTeaAttributeDao.getAttribute();
        System.out.print("attributes" + ": ");
        for(String s :attributeList) {
            System.out.print(s + ", ");
        }
        System.out.println();

        JSONObject result = new JSONObject();
        try {
            if (!attributeList.isEmpty()) {
                // 查询成功
                result.put("status", "success");

                // 返回属性值
                JSONObject attributeJson = new JSONObject();
                attributeJson.put("list", attributeList);

                // 返回 JSON 数据
                result.put("attribute", attributeJson);
            } else {
                // 查询失败，返回状态信息
                result.put("status", "fail");
                result.put("message", "未查询到该属性的值");
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

    // 查询单个奶茶属性（POST 请求）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
