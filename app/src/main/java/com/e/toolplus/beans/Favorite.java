
package com.e.toolplus.beans;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorite implements Serializable
{

    @SerializedName("favoriteId")
    @Expose
    private String favoriteId;
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
    private final static long serialVersionUID = -109612746194515397L;

    public Favorite() {
    }


    public Favorite(String favoriteId, String userId, String categoryId, String productId, String name, Long price, String brand, String imageUrl, String description, String shopKeeperId) {
        super();
        this.favoriteId = favoriteId;
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

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
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
