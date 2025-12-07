package com.shop.servlets;

import com.shop.UserManage;
import com.shop.users;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/users")
public class UserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		try {
			List<users> userList = UserManage.getAllUsers();
			JSONArray arr = new JSONArray();

			for (users u : userList) {
				JSONObject obj = new JSONObject();
				obj.put("username", u.getUsername());
				obj.put("userpass", u.getPassword());
				obj.put("userstatus", u.getUserStat());
				arr.put(obj);
			}

			res.getWriter().print(arr.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"error\":\"Database error\"}");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		try (BufferedReader reader = req.getReader()) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject json = new JSONObject(sb.toString());
			String action = json.optString("action", "register");

			if ("login".equalsIgnoreCase(action)) {
				// LOGIN LOGIC
				String username = json.getString("username");
				String password = json.getString("userpass");

				users u = UserManage.getUserByUsername(username);

				if (u != null && u.getPassword().equals(password)) {
					JSONObject responseJson = new JSONObject();
					responseJson.put("success", true);
					responseJson.put("username", u.getUsername());
					responseJson.put("userstatus", u.getUserStat());
					res.getWriter().print(responseJson.toString());
				} else {
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					res.getWriter().print("{\"success\":false, \"message\":\"Invalid username or password\"}");
				}

			} else if ("register".equalsIgnoreCase(action)) {
				// REGISTER LOGIC
				users u = new users(json.getString("username"), json.getString("userpass"),
						json.getString("userstatus"));

				UserManage.addUser(u);
				res.getWriter().print("{\"success\":true}");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"error\":\"Database error\"}");
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.getWriter().print("{\"error\":\"Invalid data\"}");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		String username = req.getParameter("username");
		if (username == null || username.isEmpty()) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			res.getWriter().print("{\"error\":\"Username required\"}");
			return;
		}

		try {
			UserManage.deleteUser(username);
			res.getWriter().print("{\"success\":true}");
		} catch (SQLException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res.getWriter().print("{\"error\":\"Database error\"}");
		}
	}
}