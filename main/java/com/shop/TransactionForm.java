package com.shop;

import java.util.ArrayList;
import java.util.List;

public class TransactionForm {
	private Long id;
	private String custName;
	private String transDate;
	private List<TransactionItem> items = new ArrayList<>();

	public TransactionForm() {
	}

	public TransactionForm(String custName) {
		this.custName = custName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public List<TransactionItem> getItems() {
		return items;
	}

	public void setItems(List<TransactionItem> items) {
		this.items = items;
	}

	public double getTotalSales() {
		return items.stream().mapToDouble(TransactionItem::getTotalSale).sum();
	}
}
