package com.example.Servlet;

import com.example.dao.MilkTeaAttributeDao;
import com.example.dao.MilkTeaAttributeMapDao;
import com.example.dao.MilkTeaAttributeValueDao;
import com.example.dao.MilkTeaItemDao;
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

@WebServlet("/milk-tea-item")  // URL 以 / 开头
public class MilkTeaItem extends HttpServlet {
    private MilkTeaAttributeValueDao MilkTeaAttributeValueDao = new MilkTeaAttributeValueDao();
    private MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();
    private MilkTeaItemDao MilkTeaItemDao = new MilkTeaItemDao();
    private MilkTeaAttributeMapDao MilkTeaAttributeMapDao = new MilkTeaAttributeMapDao();

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
        String message = "";
        try {
            // 解析请求体中的 JSON 数据
            JSONObject json = new JSONObject(sb.toString());

            String name = json.getString("name");
            double price = json.getDouble("price");
            String image = json.getString("image");
            String description = json.getString("description");

            // 添加奶茶基础信息 返回 ID or -1
            int addMilkTeaID = MilkTeaItemDao.addMilkTea(name,price,image,description);
            //添加奶茶基础信息失败
            if (addMilkTeaID == -1)
            {
                result.put("status", "fail");
                result.put("message", "添加商品失败"+",商品名称为" + name);
                // 将响应写回给客户端
                PrintWriter out = response.getWriter();
                out.print(result.toString());  // 输出 JSON 字符串
                out.flush();  // 刷新输出流
            }

            // 解析属性数组
            JSONArray attributes = json.getJSONArray("attributes");
            for (int i = 0; i < attributes.length(); i++) {
                JSONObject attr = attributes.getJSONObject(i);
                String attrName = attr.getString("attribute");
                JSONArray values = attr.getJSONArray("attribute_value");

                //添加属性
                int addAttributeID =  MilkTeaAttributeDao.addAttribute(attrName);
                //添加属性失败
                if (addAttributeID == -1)
                {
                    result.put("status", "fail");
                    result.put("message", "添加属性失败"+",属性为" + attrName);
                    // 将响应写回给客户端
                    PrintWriter out = response.getWriter();
                    out.print(result.toString());  // 输出 JSON 字符串
                    out.flush();  // 刷新输出流
                }


                List<String> valueList = new ArrayList<>();
                for (int j = 0; j < values.length(); j++) {
                    valueList.add(values.getString(j));
                }

                List<Integer> addAttributeValueByAttributeNameList =  MilkTeaAttributeValueDao.addAttributeValueByAttributeName(attrName,valueList);
                // 添加属性值失败
                if (addAttributeValueByAttributeNameList.isEmpty())
                {
                    result.put("status", "fail");
                    result.put("message", "添加属性值失败");
                    // 将响应写回给客户端
                    PrintWriter out = response.getWriter();
                    out.print(result.toString());  // 输出 JSON 字符串
                    out.flush();  // 刷新输出流
                }

                //链接
                boolean linkMilkTeaWithAttributeValuesRet =  MilkTeaAttributeMapDao.linkMilkTeaWithAttributeValues(addMilkTeaID,addAttributeValueByAttributeNameList);
                if (!linkMilkTeaWithAttributeValuesRet)
                {
                    result.put("status", "fail");
                    result.put("message", "链接失败");
                    // 将响应写回给客户端
                    PrintWriter out = response.getWriter();
                    out.print(result.toString());  // 输出 JSON 字符串
                    out.flush();  // 刷新输出流
                }
            }

                // 添加成功
                result.put("status", "success");

            } catch(JSONException e){
                // 捕获 JSON 格式错误
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try {
                    result.put("status", "fail");
                    result.put("message", "无效的 JSON 数据");
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();  // 打印异常
                }
            } catch(Exception e){
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

