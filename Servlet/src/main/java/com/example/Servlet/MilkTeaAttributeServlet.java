package com.example.Servlet;

import com.example.dao.MilkTeaAttributeDao;
import com.example.dao.UserDao;
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
    private MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();

    // 查询单个奶茶属性（GET 请求）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();
        try {
            result.put("status", "fail");
            result.put("message", "请使用 POST 方法提交属性名");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.print(result.toString());
        out.flush();
    }

    // 查询单个奶茶属性（POST 请求）
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
        reader.close();
        // 构建用于响应给客户端的 JSON 对象
        JSONObject result = new JSONObject();

        try {
            // 解析请求体中的 JSON 数据
            JSONObject json = new JSONObject(sb.toString());
            // 获取属性
            String attribute = json.getString("attribute");

            System.out.println("attribute: " + attribute);  // 调试输出

            // 调用 DAO 中的方法进行属性查询
            List<String> attribute_valueList = MilkTeaAttributeDao.getAttributeValuesByAttributeName(attribute);
            System.out.print("attribute_valueList: { ");  // 正确输出每个属性值
            for (String s : attribute_valueList) {
                System.out.print(s+", ");  // 正确输出每个属性值
            }
            System.out.println("};");  // 正确输出每个属性值



            if (!attribute_valueList.isEmpty()) {
                // 查询成功
                result.put("status", "founded");

                // 返回属性值
                JSONObject attribute_valueJson = new JSONObject();
                attribute_valueJson.put("attribute_value", attribute_valueList);

                // 返回 JSON 数据
                result.put("attribute_value", attribute_valueJson);
            } else {
                // 查询失败，返回状态信息
                result.put("status", "unfounded");
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

        // 将响应写回给客户端
        PrintWriter out = response.getWriter();
        out.print(result.toString());  // 输出 JSON 字符串
        out.flush();  // 刷新输出流
    }
}
