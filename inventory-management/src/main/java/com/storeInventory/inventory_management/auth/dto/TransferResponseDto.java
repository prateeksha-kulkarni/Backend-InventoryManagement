package com.storeInventory.inventory_management.auth.dto;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransferResponseDto {
    private UUID transferId;
    private ProductInfo product;
    private StoreInfo fromStore;
    private StoreInfo toStore;
    private Integer quantity;
    private TransferStatus status;
    private UserInfo requestedBy;
    private UserInfo approvedBy;
    private LocalDateTime timestamp;

    @Data
    public static class StoreInfo {
        private UUID storeId;
        private String name;
        private String location;
    }

    @Data
    public static class ProductInfo {
        private UUID productId;
        private String name;
        private String sku;
        private String category;
    }

    @Data
    public static class UserInfo {
        private UUID userId;
        private String name;
        private String username;
        private String email;
    }

    public static TransferResponseDto fromEntity(InterStoreTransferEntity entity) {
        TransferResponseDto dto = new TransferResponseDto();
        dto.setTransferId(entity.getTransferId());
        dto.setQuantity(entity.getQuantity());
        dto.setStatus(entity.getStatus());
        dto.setTimestamp(entity.getTimestamp());

        // Set product info
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(entity.getProduct().getProductId());
        productInfo.setName(entity.getProduct().getName());
        productInfo.setSku(entity.getProduct().getSku());
        productInfo.setCategory(entity.getProduct().getCategory());
        dto.setProduct(productInfo);

        // Set from store info
        StoreInfo fromStoreInfo = new StoreInfo();
        fromStoreInfo.setStoreId(entity.getFromStore().getStoreId());
        fromStoreInfo.setName(entity.getFromStore().getName());
        fromStoreInfo.setLocation(entity.getFromStore().getLocation());
        dto.setFromStore(fromStoreInfo);

        // Set to store info
        StoreInfo toStoreInfo = new StoreInfo();
        toStoreInfo.setStoreId(entity.getToStore().getStoreId());
        toStoreInfo.setName(entity.getToStore().getName());
        toStoreInfo.setLocation(entity.getToStore().getLocation());
        dto.setToStore(toStoreInfo);

        // Set requested by info
        UserInfo requestedByInfo = new UserInfo();
        requestedByInfo.setUserId(entity.getRequestedBy().getUserId());
        requestedByInfo.setName(entity.getRequestedBy().getName());
        requestedByInfo.setUsername(entity.getRequestedBy().getUsername());
        requestedByInfo.setEmail(entity.getRequestedBy().getEmail());
        dto.setRequestedBy(requestedByInfo);

        // Set approved by info if exists
        if (entity.getApprovedBy() != null) {
            UserInfo approvedByInfo = new UserInfo();
            approvedByInfo.setUserId(entity.getApprovedBy().getUserId());
            approvedByInfo.setName(entity.getApprovedBy().getName());
            approvedByInfo.setUsername(entity.getApprovedBy().getUsername());
            approvedByInfo.setEmail(entity.getApprovedBy().getEmail());
            dto.setApprovedBy(approvedByInfo);
        }

        return dto;
    }
} 