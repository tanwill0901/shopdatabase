package com.shop;

public class products {
	private Long id;
	private String pname;
	private double pbuy;
	private double psell;

	public products() {
	}

	public products(String pname, double pbuy, double psell) {
		this.pname = pname;
		this.pbuy = pbuy;
		this.psell = psell;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return pname;
	}

	public void setName(String name) {
		this.pname = name;
	}

	public double getPrice() {
		return pbuy;
	}

	public void setPrice(double price) {
		this.pbuy = price;
	}

	public double getSellPrice() {
		return psell;
	}

	public void setSellPrice(double sellprice) {
		this.psell = sellprice;
	}

	@Override
	public String toString() {
		return String.format("Product{name='%s', pbuy=%.2f, psell=%.2f}", pname, pbuy, psell);
	}
}
