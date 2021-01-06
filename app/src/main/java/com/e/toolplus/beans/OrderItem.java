
package com.e.toolplus.beans;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem implements Serializable
{

    @SerializedName("orderItemId")
    @Expose
    private String orderItemId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("qty")
    @Expose
    private Long qty;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("amount")
    @Expose
    private Long amount;
    private final static long serialVersionUID = -9181345389305616642L;

    public OrderItem() {
    }

    public OrderItem(String orderItemId, String productId, String shopKeeperId, String name, Long qty, String imageUrl, Long price, Long amount) {
        super();
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.shopKeeperId = shopKeeperId;
        this.name = name;
        this.qty = qty;
        this.imageUrl = imageUrl;
        this.price = price;
        this.amount = amount;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
