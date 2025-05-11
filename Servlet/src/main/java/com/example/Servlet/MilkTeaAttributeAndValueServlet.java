package com.example.Servlet;

import com.example.dao.MilkTeaAttributeDao;
import com.example.dao.MilkTeaAttributeValueDao;
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
import java.util.List;


@WebServlet("/milk-tea-attribute_and_value")
public class MilkTeaAttributeAndValueServlet extends HttpServlet {
    private com.example.dao.MilkTeaAttributeValueDao MilkTeaAttributeValueDao = new MilkTeaAttributeValueDao();
    private com.example.dao.MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();

    // 返回全部属性值
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // 添加属性与属性值
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

        String requestBody = sb.toString();
        System.out.println("接收到的 JSON 数据: " + requestBody); // ← 打印出来看看格式对不对

        try {
            // 解析请求体中的 JSON 数据
            JSONObject json = new JSONObject(sb.toString());
            // 获取属性
            String attribute = json.getString("attribute");
            List<String> attribute_value = new ArrayList<>();
            JSONArray jsonArray = json.getJSONArray("attribute_value");
            for (int i = 0; i < jsonArray.length(); i++) {
                attribute_value.add(jsonArray.getString(i));
            }


            System.out.println("attribute: " + attribute);  // 调试输出
            System.out.print("attribute_value" + ": ");
            for(String s :attribute_value) {
                System.out.print(s + ", ");
            }
            System.out.println();

            // 添加属性
            // 添加查询是否存在
            boolean addAttributeState = true;
            if (!MilkTeaAttributeDao.existsAttribute(attribute)) {
                addAttributeState = MilkTeaAttributeDao.addAttribute(attribute);
            }

            //去重
            List<String> already_attribute_valueList = MilkTeaAttributeValueDao.getAttributeValuesByAttributeName(attribute);

            // 去除重复值
            List<String> uniqueValuesToAdd = new ArrayList<>();
            for (String val : attribute_value) {
                if (!already_attribute_valueList.contains(val)) {
                    uniqueValuesToAdd.add(val);
                }
            }

            // 添加属性值（只添加不重复的）
            boolean addAttributeValueState = true;
            if (!uniqueValuesToAdd.isEmpty()) {
                addAttributeValueState = MilkTeaAttributeValueDao.addAttributeValueByAttributeName(attribute, uniqueValuesToAdd);
            }

            if (addAttributeState && addAttributeValueState) {
                // 查询成功
                result.put("status", "success");

            } else {
                String message = "添加属性值失败";
                if(!addAttributeState)
                {
                    result.put("status", "fail");
                    message = "添加属性失败， " + message;
                }

                result.put("message", message);
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
