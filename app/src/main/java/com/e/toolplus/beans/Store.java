
package com.e.toolplus.beans;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Store implements Serializable
{

    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;
    private final static long serialVersionUID = -8944884139352207646L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Store() {
    }

    /**
     * 
     * @param address
     * @param imageUrl
     * @param contactNumber
     * @param shopName
     * @param shopKeeperId
     * @param email
     * @param token
     */
    public Store(String shopKeeperId, String shopName, String contactNumber, String address, String imageUrl, String email, String token) {
        super();
        this.shopKeeperId = shopKeeperId;
        this.shopName = shopName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.email = email;
        this.token = token;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
