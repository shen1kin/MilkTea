package com.example.Servlet;


import com.example.dao.MilkTeaOrderDao;
import com.example.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/submitOrder")
public class MilkTeaOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(sb.toString(), Order.class);

            MilkTeaOrderDao orderDao = new MilkTeaOrderDao();
            int orderId = orderDao.insertOrder(order); // 插入订单，拿到订单ID
            orderDao.insertOrderAttributes(orderId, order.getAttributes()); // 插入属性关联


            response.getWriter().write("订单提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "订单提交失败：" + e.getMessage());
        }
    }
}
