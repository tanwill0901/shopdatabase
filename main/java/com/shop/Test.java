package com.shop;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {
	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/sitedatabase";
		String user = "postgres";
		String password = "password";

		System.out.println("Testing connection...");

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			if (conn != null) {
				System.out.println("✅ Connected to PostgreSQL successfully!");
			} else {
				System.out.println("❌ Connection failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}