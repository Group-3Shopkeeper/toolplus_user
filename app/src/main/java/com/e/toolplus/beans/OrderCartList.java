package com.e.toolplus.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderCartList implements Serializable {
	private String orderId;
	private String userId;
	private String name;
	private String date;
	private String deliveryAddress;
	private double totalAmount;
	private String contactNumber;
	private String deliveryOption;
	private String shippingStatus;
	private ArrayList<Cart> orderItem;
	

	public OrderCartList() {

	}

	public OrderCartList(String orderId, String userId, String name, String date, String deliveryAddress, double totalAmount,
			String contactNumber, String deliveryOption, String shippingStatus, ArrayList<Cart> orderItem) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.name = name;
		this.date = date;
		this.deliveryAddress = deliveryAddress;
		this.totalAmount = totalAmount;
		this.contactNumber = contactNumber;
		this.deliveryOption = deliveryOption;
		this.shippingStatus = shippingStatus;
		this.orderItem = orderItem;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public ArrayList<Cart> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(ArrayList<Cart> orderItem) {
		this.orderItem = orderItem;
	}

}
