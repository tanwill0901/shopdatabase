package com.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConn {
	private static final String URL = "jdbc:postgresql://localhost:5432/sitedatabase";
	//private static final String URL = "jdbc:database-1.cniysw6i0jov.us-east-2.rds.amazonaws.com:5432/shopdb";
	private static final String USER = "postgres";
	private static final String PASS = "password";

	public static Connection getConnection() {
		Connection conn = null;
		try {

			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL JDBC Driver not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Database connection failed!");
			e.printStackTrace();
		}
		return conn;
	}
}