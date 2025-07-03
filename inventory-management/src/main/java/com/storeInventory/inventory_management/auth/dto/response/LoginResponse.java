package com.storeInventory.inventory_management.auth.dto.response;

public class LoginResponse {
    public LoginResponse() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String token;
    private String role;
    private String storeId;
    private String location;

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
        this.storeId = null;
    }
}
