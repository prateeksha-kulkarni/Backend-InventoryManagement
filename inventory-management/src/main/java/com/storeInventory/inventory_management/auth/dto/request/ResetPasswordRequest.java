package com.storeInventory.inventory_management.auth.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;
    private String newPassword;
}
