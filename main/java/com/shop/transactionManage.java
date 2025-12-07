package com.shop;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class transactionManage {

	public static void addTransaction(TransactionForm transaction) throws SQLException {
		String insertTransaction = "INSERT INTO transactions (custname) VALUES (?) RETURNING id";
		String insertItem = "INSERT INTO transaction_items (transaction_id, pname, quantity) VALUES (?, ?, ?)";

		try (Connection conn = DataConn.getConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement psTrans = conn.prepareStatement(insertTransaction);
					PreparedStatement psItem = conn.prepareStatement(insertItem)) {
				psTrans.setString(1, transaction.getCustName());
				ResultSet rs = psTrans.executeQuery();
				if (rs.next()) {
					long transactionId = rs.getLong("id");

					for (TransactionItem item : transaction.getItems()) {
						psItem.setLong(1, transactionId);
						psItem.setString(2, item.getProductName());
						psItem.setInt(3, item.getQuantity());
						psItem.addBatch();
					}
					psItem.executeBatch();
				}

				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				throw ex;
			} finally {
				conn.setAutoCommit(true);
			}
		}
	}

	public static List<TransactionItem> getItemsByTransaction(long transactionId) throws SQLException {
		String sql = """
				    SELECT ti.pname, ti.quantity, p.pbuy, p.psell
				    FROM transaction_items ti
				    JOIN products p ON ti.pname = p.pname
				    WHERE ti.transaction_id = ?
				""";

		List<TransactionItem> items = new ArrayList<>();

		try (Connection conn = DataConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, transactionId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					TransactionItem item = new TransactionItem();
					item.setProductName(rs.getString("pname"));
					item.setQuantity(rs.getInt("quantity"));
					item.setProductBuy(rs.getDouble("pbuy"));
					item.setProductSell(rs.getDouble("psell"));
					items.add(item);
				}
			}
		}
		return items;
	}

	public static List<TransactionForm> getAllTransactions() throws SQLException {
		String sql = "SELECT id, custname, transdate FROM transactions ORDER BY transdate DESC";
		List<TransactionForm> list = new ArrayList<>();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				TransactionForm t = new TransactionForm();

				long id = rs.getLong("id");
				t.setId(id);
				t.setCustName(rs.getString("custname"));

				Timestamp ts = rs.getTimestamp("transdate");
				if (ts != null) {
					LocalDateTime ldt = ts.toLocalDateTime();
					t.setTransDate(ldt.format(fmt));
				} else {
					t.setTransDate(null);
				}

				t.setItems(getItemsByTransaction(id));

				list.add(t);
			}
		}
		return list;
	}

	public static boolean deleteTransaction(long transactionId) throws SQLException {
		String sql = "DELETE FROM transactions WHERE id = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, transactionId);
			int rows = ps.executeUpdate();
			return rows > 0;
		}
	}
}