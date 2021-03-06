
package com.e.toolplus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("discount")
    @Expose
    private Long discount;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("qtyInStock")
    @Expose
    private Long qtyInStock;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("secondImageUrl")
    @Expose
    private String secondImageUrl;
    @SerializedName("thirdImageurl")
    @Expose
    private String thirdImageurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    public Product() {
    }

    public Product(String secondImageUrl,String thirdImageurl,String productId, String categoryId, String shopKeeperId, String name, Long price, Long discount, String brand, Long qtyInStock, String imageUrl, String description, Long timestamp) {
        this.secondImageUrl=secondImageUrl;
        this.thirdImageurl=thirdImageurl;
        this.productId = productId;
        this.categoryId = categoryId;
        this.shopKeeperId = shopKeeperId;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.brand = brand;
        this.qtyInStock = qtyInStock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }

    public String getThirdImageurl() {
        return thirdImageurl;
    }

    public void setThirdImageurl(String thirdImageurl) {
        this.thirdImageurl = thirdImageurl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
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

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Long qtyInStock) {
        this.qtyInStock = qtyInStock;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
