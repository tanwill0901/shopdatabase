package com.shop;

public class TransactionItem {
	private String productName;
	private int quantity;
	private double productBuy;
	private double productSell;
	private double totalSale;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getProductBuy() {
		return productBuy;
	}

	public void setProductBuy(double productBuy) {
		this.productBuy = productBuy;
	}

	public double getProductSell() {
		return productSell;
	}

	public void setProductSell(double productSell) {
		this.productSell = productSell;
	}

	public double getTotalSale() {
		return totalSale;
	}

	@Override
	public String toString() {
		return String.format("%s x%d = %.2f", productName, quantity, totalSale);
	}
}