package com.example.Servlet;

import com.example.dao.MilkTeaOrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet("/today_summary")  // 确保路径一致
public class TodaySummaryServlet extends HttpServlet {
    private MilkTeaOrderDao orderDao=new MilkTeaOrderDao();

    public TodaySummaryServlet() throws SQLException {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject result = new JSONObject();

        try {
            int todayCount = orderDao.getTodayOrderCount();
            double todayTotal = orderDao.getTodayTotalRevenue();

            result.put("status", "success");
            result.put("today_total_count", todayCount);
            result.put("today_total_price", todayTotal);

        } catch (Exception e) {
            try {
                result.put("status", "error");
                result.put("message", e.getMessage());
            } catch (org.json.JSONException jsonException) {
                jsonException.printStackTrace(); // 最好打印日志
            }
        }

        PrintWriter out = resp.getWriter();
        out.print(result.toString());
        out.flush();
    }

}
