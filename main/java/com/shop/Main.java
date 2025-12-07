package com.shop;

import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		UserLogin login = new UserLogin();
		productManage newProManage = new productManage();

		System.out.print("Username: ");
		String username = sc.nextLine();
		System.out.print("Password: ");
		String userpass = sc.nextLine();

		users User = UserLogin.login(username, userpass);
		if (User == null) {
			System.out.println("Invalid Login!");
			sc.close();
			return;
		}

		System.out.println("Welcome " + User.getUsername() + "!");
		boolean running = true;

		while (running) {
			System.out.println("\n MENU ");
			System.out.println("1. View Products");
			System.out.println("2. Add Products");
			System.out.println("3. Remove Products");
			System.out.println("4. View Accounts");
			System.out.println("5. Logout");

			int command = sc.nextInt();
			sc.nextLine();

			switch (command) {
			case 1:
				List<products> printProducts = newProManage.listProducts();
				for (products p : printProducts) {
					System.out.println(p);
				}
				break;
			case 2:
				System.out.print("Product Name: ");
				String name = sc.nextLine();
				System.out.print("Buy Price: ");
				double bprice = sc.nextDouble();
				System.out.print("Sell Price: ");
				double sprice = sc.nextDouble();
				newProManage.addProduct(name, bprice, sprice);
				break;
			case 3:
				System.out.print("Enter Product Name to remove: ");
				String pname = sc.nextLine();
				newProManage.removeProduct(pname);
				break;
			case 4:
				List<users> printUsers = login.listUsers();
				for (users u : printUsers) {
					System.out.println(u);
				}
				break;
			case 5:
				System.out.println("Logged out. Bye!");
				running = false;
				break;
			default:
				System.out.println("Invalid command!");
			}
		}
	}
}
