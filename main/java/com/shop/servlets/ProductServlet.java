package com.shop.servlets;

import com.shop.products;
import com.shop.productManage;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {
	private productManage man = new productManage();
	private Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<products> products = man.listProducts();
		resp.setContentType("application/json");
		resp.getWriter().write(gson.toJson(products));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		products p = gson.fromJson(new InputStreamReader(req.getInputStream()), products.class);
		man.addProduct(p);
		resp.getWriter().write("{\"success\": true}");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String name = req.getParameter("pname");
		man.removeProduct(name);
		resp.getWriter().write("{\"success\": true}");
	}
}