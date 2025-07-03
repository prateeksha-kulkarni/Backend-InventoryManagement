package com.storeInventory.inventory_management.auth.exception;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
    public UnauthorizedOperationException(String operation, String userRole) {
        super(String.format("User with role '%s' is not authorized to perform operation: %s", userRole, operation));
    }
} 