
package com.e.toolplus.beans;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Serializable
{

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("deliveryAddress")
    @Expose
    private String deliveryAddress;
    @SerializedName("totalAmount")
    @Expose
    private Long totalAmount;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("deliveryOption")
    @Expose
    private String deliveryOption;
    @SerializedName("shippingStatus")
    @Expose
    private String shippingStatus;
    @SerializedName("orderItem")
    @Expose
    private List<OrderItem> orderItem;
    private final static long serialVersionUID = -8183449698344537026L;

    public Order() {
    }

    public Order(String orderId, String userId, String name, String date, String deliveryAddress, Long totalAmount, String contactNumber, String deliveryOption, String shippingStatus, List<OrderItem> orderItem) {
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
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

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

}
