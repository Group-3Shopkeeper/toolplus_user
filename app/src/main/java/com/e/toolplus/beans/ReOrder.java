package com.e.toolplus.beans;

public class ReOrder {
	private OrderItem orderItems;
	private int qtyInStock;
	private double price;
	
	public ReOrder() {

	}

	public ReOrder(OrderItem orderItems, int qtyInStock, double price) {
		super();
		this.orderItems = orderItems;
		this.qtyInStock = qtyInStock;
		this.price = price;
	}

	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public OrderItem getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(OrderItem orderItems) {
		this.orderItems = orderItems;
	}

	public int getQtyInStock() {
		return qtyInStock;
	}

	public void setQtyInStock(int qtyInStock) {
		this.qtyInStock = qtyInStock;
	}

}
