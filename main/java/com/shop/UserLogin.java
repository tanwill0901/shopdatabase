package com.shop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserLogin {

	public static boolean validateLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE username = ? AND userpass = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static users login(String username, String password) {
		String sql = "SELECT * FROM users WHERE username = ? AND userpass = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

			st.setString(1, username);
			st.setString(2, password);

			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new users(rs.getString("username"), rs.getString("userpass"), rs.getString("userstatus"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<users> listUsers() {
		List<users> userList = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection conn = DataConn.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {
			while (rs.next()) {
				userList.add(new users(rs.getString("username"), rs.getString("userpass"), rs.getString("userstatus")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
}