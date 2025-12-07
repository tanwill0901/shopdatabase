package com.shop;

import java.sql.*;
import java.util.*;

public class UserManage {

	public static List<users> getAllUsers() throws SQLException {
		String sql = "SELECT username, userpass, userstatus FROM users ORDER BY username ASC";
		List<users> list = new ArrayList<>();

		try (Connection conn = DataConn.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new users(rs.getString("username"), rs.getString("userpass"), rs.getString("userstatus")));
			}
		}
		return list;
	}

	public static void addUser(users u) throws SQLException {
		String sql = "INSERT INTO users (username, userpass, userstatus) VALUES (?, ?, ?)";
		try (Connection conn = DataConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getUserStat());
			ps.executeUpdate();
		}
	}

	public static void deleteUser(String username) throws SQLException {
		String sql = "DELETE FROM users WHERE username = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username);
			ps.executeUpdate();
		}
	}

	public static users validateLogin(String username, String userpass) throws SQLException {
		String sql = "SELECT username, userpass, userstatus FROM users WHERE username = ? AND userpass = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username);
			ps.setString(2, userpass);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new users(rs.getString("username"), rs.getString("userpass"), rs.getString("userstatus"));
				}
			}
		}
		return null;
	}

	public static users getUserByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DataConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new users(rs.getString("username"), rs.getString("userpass"), rs.getString("userstatus"));
				}
			}
		}
		return null;
	}
}