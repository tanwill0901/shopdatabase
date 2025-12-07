package com.shop;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class analyticsManage {

	public static String getSalesPerMonth() throws SQLException {
		String sql = """
				    SELECT
				        TO_CHAR(transdate, 'YYYY-MM') AS month,
				        SUM(ti.quantity * p.psell) AS total_sales
				    FROM transaction_items ti
				    JOIN transactions t ON ti.transaction_id = t.id
				    JOIN products p ON ti.pname = p.pname
				    GROUP BY month
				    ORDER BY month;
				""";

		JSONArray arr = new JSONArray();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("month", rs.getString("month"));
				obj.put("totalSales", rs.getDouble("total_sales"));
				arr.put(obj);
			}
		}
		return arr.toString();
	}

	public static String getProfitPerMonth() throws SQLException {
		String sql = """
				    SELECT
				        TO_CHAR(transdate, 'YYYY-MM') AS month,
				        SUM((ti.quantity * p.psell) - (ti.quantity * p.pbuy)) AS profit
				    FROM transaction_items ti
				    JOIN transactions t ON ti.transaction_id = t.id
				    JOIN products p ON ti.pname = p.pname
				    GROUP BY month
				    ORDER BY month;
				""";

		JSONArray arr = new JSONArray();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("month", rs.getString("month"));
				obj.put("profit", rs.getDouble("profit"));
				arr.put(obj);
			}
		}
		return arr.toString();
	}

	public static String getWeeklySales() throws SQLException {
		String sql = """
				    SELECT
				        EXTRACT(YEAR FROM t.transdate) AS year,
				        EXTRACT(WEEK FROM t.transdate) AS week,
				        SUM(ti.quantity * p.psell) AS total_sales
				    FROM transaction_items ti
				    JOIN transactions t ON ti.transaction_id = t.id
				    JOIN products p ON ti.pname = p.pname
				    GROUP BY year, week
				    ORDER BY year, week;
				""";

		JSONArray arr = new JSONArray();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("week", rs.getInt("week"));
				obj.put("year", rs.getInt("year"));
				obj.put("totalSales", rs.getDouble("total_sales"));
				arr.put(obj);
			}
		}
		return arr.toString();
	}

	public static String getExpenseIncome() throws SQLException {
		String sql = """
				    SELECT
				        TO_CHAR(t.transdate, 'YYYY-MM') AS month,
				        SUM(ti.quantity * p.psell) AS income,
				        SUM(ti.quantity * p.pbuy) AS expense
				    FROM transaction_items ti
				    JOIN transactions t ON ti.transaction_id = t.id
				    JOIN products p ON ti.pname = p.pname
				    GROUP BY month
				    ORDER BY month;
				""";

		JSONArray arr = new JSONArray();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("month", rs.getString("month"));
				obj.put("income", rs.getDouble("income"));
				obj.put("expense", rs.getDouble("expense"));
				arr.put(obj);
			}
		}
		return arr.toString();
	}

	public static String getProductSalesCount() throws SQLException {
		String sql = """
				    SELECT
				        ti.pname AS product,
				        SUM(ti.quantity) AS quantity_sold
				    FROM transaction_items ti
				    GROUP BY product
				    ORDER BY quantity_sold DESC;
				""";

		JSONArray arr = new JSONArray();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("product", rs.getString("product"));
				obj.put("quantitySold", rs.getInt("quantity_sold"));
				arr.put(obj);
			}
		}
		return arr.toString();
	}

}