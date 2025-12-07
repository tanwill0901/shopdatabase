package com.shop.servlets;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shop.TransactionForm;
import com.shop.TransactionItem;
import com.shop.transactionManage;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/api/transactions/*")
public class TransactionServlet extends HttpServlet {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		try {
			List<TransactionForm> transactions = transactionManage.getAllTransactions();
			JSONArray arr = new JSONArray();

			for (TransactionForm t : transactions) {
				JSONObject obj = new JSONObject();
				obj.put("id", t.getId());
				obj.put("custName", t.getCustName());

				String formattedDate = t.getTransDate();
				if (formattedDate != null && formattedDate.contains(".")) {
					// trim off any fractional seconds if present
					formattedDate = formattedDate.split("\\.")[0];
				}
				obj.put("transDate", formattedDate);

				JSONArray itemsArr = new JSONArray();
				double totalSales = 0.0;

				for (TransactionItem i : t.getItems()) {
					JSONObject itemJson = new JSONObject();
					itemJson.put("productName", i.getProductName());
					itemJson.put("quantity", i.getQuantity());
					itemJson.put("productBuy", i.getProductBuy());
					itemJson.put("productSell", i.getProductSell());
					itemJson.put("totalSale", i.getTotalSale());

					totalSales += i.getTotalSale();
					itemsArr.put(itemJson);
				}

				obj.put("items", itemsArr);
				obj.put("totalSales", totalSales);
				arr.put(obj);
			}

			res.getWriter().print(arr.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"error\":\"Database error: " + e.getMessage() + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"error\":\"Failed to load transactions\"}");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		try {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = req.getReader()) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}

			JSONObject json = new JSONObject(sb.toString());
			TransactionForm transaction = new TransactionForm();
			transaction.setCustName(json.getString("custName"));

			JSONArray itemsArr = json.getJSONArray("items");
			for (int i = 0; i < itemsArr.length(); i++) {
				JSONObject itemJson = itemsArr.getJSONObject(i);

				TransactionItem item = new TransactionItem();
				item.setProductName(itemJson.getString("productName"));
				item.setQuantity(itemJson.getInt("quantity"));
				item.setProductBuy(itemJson.optDouble("productBuy", 0.0));
				item.setProductSell(itemJson.optDouble("productSell", 0.0));

				transaction.getItems().add(item);
			}

			transactionManage.addTransaction(transaction);

			res.getWriter().print("{\"success\":true}");
		} catch (SQLException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"success\":false,\"error\":\"Database error: " + e.getMessage() + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\":\"Transaction ID missing\"}");
			return;
		}

		try {
			int id = Integer.parseInt(pathInfo.substring(1)); // remove leading "/"
			boolean deleted = transactionManage.deleteTransaction(id);

			if (deleted) {
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().write("{\"success\":true}");
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.getWriter().write("{\"success\":false, \"message\":\"Transaction not found\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"success\":false, \"error\":\"Server error\"}");
		}
	}
}