package com.example.Servlet;

import com.example.dao.*;
import com.example.model.MilkTea;
import com.example.model.MilkTeaAttribute;
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

@WebServlet("/milk-tea-item")  // URL 以 / 开头
public class MilkTeaItem extends HttpServlet {
    private MilkTeaAttributeValueDao MilkTeaAttributeValueDao = new MilkTeaAttributeValueDao();
    private MilkTeaAttributeDao MilkTeaAttributeDao = new MilkTeaAttributeDao();
    private MilkTeaItemDao MilkTeaItemDao = new MilkTeaItemDao();
    private MilkTeaAttributeMapDao MilkTeaAttributeMapDao = new MilkTeaAttributeMapDao();
    private MilkTeaClassDao MilkTeaClassDao = new MilkTeaClassDao();

    // 查询单个奶茶属性（GET 请求）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应类型为 JSON，编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 从数据库获取所有奶茶信息
        List<MilkTea> milkTeaList = MilkTeaItemDao.getMilkTeaList();  // 假设返回的是一个包含所有奶茶信息的列表

        // 创建一个 JSON 对象来存放响应数据
        JSONObject result = new JSONObject();

        try {
            if (milkTeaList != null && !milkTeaList.isEmpty()) {
                // 查询成功
                result.put("status", "success");

                // 创建一个 JSON 数组来存放所有奶茶信息
                JSONArray milkTeaJsonArray = new JSONArray();
                for (MilkTea milkTea : milkTeaList) {
                    JSONObject milkTeaJson = new JSONObject();
                    milkTeaJson.put("id", milkTea.getId());
                    milkTeaJson.put("name", milkTea.getName());
                    milkTeaJson.put("price", milkTea.getPrice());
                    // 获取图片的二进制数据并转为 Base64
                    String base64Image = Base64.getEncoder().encodeToString(milkTea.getImage()); // 将二进制数据转换为 Base64 字符串
                    milkTeaJson.put("image", base64Image);
                    milkTeaJson.put("description", milkTea.getDescription());
                    milkTeaJson.put("class", milkTea.getClazz());

                    // 将奶茶的属性也加入 JSON
                    JSONArray attributesJsonArray = new JSONArray();
                    for (MilkTeaAttribute attribute : milkTea.getAttributes()) {
                        JSONObject attributeJson = new JSONObject();
                        attributeJson.put("attribute", attribute.getAttribute());
                        attributeJson.put("attribute_value", new JSONArray(attribute.getAttribute_value()));
                        attributesJsonArray.put(attributeJson);
                    }
                    milkTeaJson.put("attributes", attributesJsonArray);

                    // 将奶茶的 JSON 对象加入数组
                    milkTeaJsonArray.put(milkTeaJson);
                }

                // 将奶茶信息放入响应数据
                result.put("milkTeas", milkTeaJsonArray);
            } else {
                // 查询失败，返回状态信息
                result.put("status", "fail");
                result.put("message", "未查询到奶茶信息");
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

        // 输出响应数据
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
            // 获取 Base64 图片字符串并转为 byte[]
            String imageBase64 = json.getString("image");
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
            String description = json.getString("description");
            String classes = json.getString("class");


            // 添加奶茶分类 返回 ID or -1
            int classID = MilkTeaClassDao.addClass(classes);
            //添加奶茶基础信息失败
            if (classID == -1)
            {
                result.put("status", "fail");
                result.put("message", "添加分类"+",商品名称为" + classes);
                // 将响应写回给客户端
                PrintWriter out = response.getWriter();
                out.print(result.toString());  // 输出 JSON 字符串
                out.flush();  // 刷新输出流
            }

            // 添加奶茶基础信息 返回 ID or -1
            int addMilkTeaID = MilkTeaItemDao.addMilkTea(name,price,imageBytes,description,classID);
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

