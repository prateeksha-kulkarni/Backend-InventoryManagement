package com.storeInventory.inventory_management.auth.dto.response;

import java.util.UUID;

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

    public String getUserId(){ return  userId; }

    public void setUserId(String userId){ this.userId = userId; }

    private String token;
    private String role;
    private String storeId;
    private String location;
    private String userId;

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
        this.storeId = null;
        this.userId = null;
    }
}
