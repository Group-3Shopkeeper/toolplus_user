
package com.e.toolplus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cart implements Serializable {
    @SerializedName("qtyInStock")
    @Expose
    private Long qtyInStock;

    @SerializedName("cartId")
    @Expose
    private String cartId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;

    public Cart() {
    }

    public Cart(Long qtyInStock, String cartId, String userId, String categoryId, String productId, String name, Long price, String brand, String imageUrl, String description, String shopKeeperId) {
        this.qtyInStock = qtyInStock;
        this.cartId = cartId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.description = description;
        this.shopKeeperId = shopKeeperId;
    }

    public Long getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Long qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }


}
