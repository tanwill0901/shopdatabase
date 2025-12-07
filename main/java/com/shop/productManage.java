package com.shop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productManage {
	public void addProduct(String name, double price, double sellprice) {
		String sql = "INSERT INTO products (pname, pbuy, psell) VALUES (?, ?, ?)";
		try (Connection conn = DataConn.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, name);
			st.setDouble(2, price);
			st.setDouble(3, sellprice);
			st.executeUpdate();
			System.out.println("Product added!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addProduct(products p) {
		String sql = "INSERT INTO products (pname, pbuy, psell) VALUES (?, ?, ?)";
		try (Connection conn = DataConn.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

			st.setString(1, p.getName());
			st.setDouble(2, p.getPrice());
			st.setDouble(3, p.getSellPrice());
			st.executeUpdate();
			System.out.println("Product Added!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeProduct(String name) {
		String sql = "DELETE FROM products WHERE pname = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, name);
			st.executeUpdate();
			System.out.println("Product Removed!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<products> listProducts() {
		List<products> productList = new ArrayList<>();
		String sql = "SELECT * FROM products";
		try (Connection conn = DataConn.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {
			while (rs.next()) {
				productList.add(new products(rs.getString("pname"), rs.getDouble("pbuy"), rs.getDouble("psell")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}
}
