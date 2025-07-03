package com.storeInventory.inventory_management.auth.exception;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException(String message) {
        super(message);
    }
    public InvalidTransferException(String fromStore, String toStore) {
        super(String.format("Invalid transfer: Cannot transfer from store '%s' to store '%s'", fromStore, toStore));
    }
} 