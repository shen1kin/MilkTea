package com.example.Servlet;


import com.example.dao.MilkTeaOrderDao;
import com.example.model.Order;
import com.example.model.OrderAttribute;
import com.example.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@WebServlet("/order_admin")
public class MilkTeaOrderAdminServlet extends HttpServlet {
    private MilkTeaOrderDao orderDao = new MilkTeaOrderDao();

    public MilkTeaOrderAdminServlet() throws SQLException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();
        try {
            List<Order> orders = orderDao.getAllOrders();

            JSONArray orderArray = new JSONArray();
            for (Order order : orders) {
                JSONObject orderJson = new JSONObject();
                orderJson.put("userid", order.getUserid());
                orderJson.put("store_name", order.getStore_name());
                orderJson.put("total_count", order.getTotal_count());
                orderJson.put("total_price", order.getTotal_price());
                orderJson.put("order_time", order.getOrder_time());
                orderJson.put("pickup_method", order.getPickup_method());
                orderJson.put("pay_method", order.getPay_method());
                orderJson.put("status", order.getStatus());
                orderJson.put("address", order.getAddress());
                orderJson.put("order_num", order.getOrder_num());
                orderJson.put("remark", order.getRemark());
                orderJson.put("order_time_end", order.getOrder_time_end());

                // 添加订单项
                JSONArray itemArray = new JSONArray();
                for (OrderItem item : order.getOrderItemInfos()) {
                    JSONObject itemJson = new JSONObject();
                    itemJson.put("milk_tea_id", item.getMilk_tea_id());
                    itemJson.put("name", item.getName());
                    itemJson.put("price", item.getPrice());
                    itemJson.put("count", item.getCount());
                    itemJson.put("class", item.getClazz());
//                    itemJson.put("imageWay", item.getImageWay());
                    // 获取图片的二进制数据并转为 Base64
                    String base64Image = Base64.getEncoder().encodeToString(item.getImage()); // 将二进制数据转换为 Base64 字符串
                    itemJson.put("image64", base64Image);

                    // 添加属性
                    JSONArray attrArray = new JSONArray();
                    for (OrderAttribute attr : item.getAttributes()) {
                        JSONObject attrJson = new JSONObject();
                        attrJson.put("attribute", attr.getAttribute());
                        attrJson.put("attribute_value", attr.getAttribute_value());
                        attrArray.put(attrJson);
                    }
                    itemJson.put("attributes", attrArray);

                    itemArray.put(itemJson);
                }

                orderJson.put("orderItemInfos", itemArray);
                orderArray.put(orderJson);
            }

            result.put("status", "success");
            result.put("orders", orderArray);
        } catch (Exception e) {
            try {
                result.put("status", "error");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            try {
                result.put("message", e.getMessage());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }

        PrintWriter out = response.getWriter();
        out.print(result.toString());
        out.flush();
    }




    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            String jsonStr = sb.toString();

            // 输出接收到的原始JSON字符串到控制台
            System.out.println("接收到的JSON数据：" + jsonStr);

            // 用 Jackson 反序列化 JSON
            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(jsonStr, Order.class);

            // 也可以把对象信息打印一下（需要Order实现toString）
//            System.out.println("反序列化得到的Order对象：" + order);

            // 调用 DAO 保存订单和商品属性
            int orderId = orderDao.insertOrder(order);

            // 遍历每个商品项插入属性
            for (OrderItem item : order.getOrderItemInfos()) {
                orderDao.insertOrderAttributes(orderId,item.getMilk_tea_id() ,item.getAttributes());
            }

            response.getWriter().write("订单提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "订单提交失败：" + e.getMessage());
        }
    }
}

