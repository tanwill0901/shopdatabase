package com.shop.servlets;

import com.shop.analyticsManage;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/analytics/*")
public class AnalyticsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		String path = req.getPathInfo();

		try {
			if ("/sales-monthly".equals(path)) {
				resp.getWriter().write(analyticsManage.getSalesPerMonth());
			} else if ("/profit-monthly".equals(path)) {
				resp.getWriter().write(analyticsManage.getProfitPerMonth());
			} else if ("/weekly-sales".equals(path)) {
				resp.getWriter().write(analyticsManage.getWeeklySales());
			} else if ("/expense-income".equals(path)) {
				resp.getWriter().write(analyticsManage.getExpenseIncome());
			} else if ("/product-sales".equals(path)) {
				resp.getWriter().write(analyticsManage.getProductSalesCount());
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.getWriter().write("{\"error\":\"Unknown analytics route\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\":\"Server analytics error\"}");
		}
	}
}