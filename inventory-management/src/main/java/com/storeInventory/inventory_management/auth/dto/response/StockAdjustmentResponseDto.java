
package com.storeInventory.inventory_management.auth.dto.response;

import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockAdjustmentResponseDto {
    private UUID adjustmentId;
    private ChangeType changeType;
    private Integer quantityChange;
    private String reason;
    private LocalDateTime timestamp;

    private ProductInfo product;
    private StoreInfo store;
    private UserInfo user;

    @Data
    public static class ProductInfo {
        private UUID productId;
        private String name;
        private String sku;
        private String category;
    }

    @Data
    public static class StoreInfo {
        private UUID storeId;
        private String name;
        private String location;
    }

    @Data
    public static class UserInfo {
        private UUID userId;
        private String name;
        private String username;
        private String email;
    }

    public static StockAdjustmentResponseDto fromEntity(com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity entity) {
        StockAdjustmentResponseDto dto = new StockAdjustmentResponseDto();

        dto.setAdjustmentId(entity.getAdjustmentId());
        dto.setChangeType(entity.getChangeType());
        dto.setQuantityChange(entity.getQuantityChange());
        dto.setReason(entity.getReason());
        dto.setTimestamp(entity.getTimestamp());

        // Populate product
        ProductInfo product = new ProductInfo();
        product.setProductId(entity.getInventory().getProduct().getProductId());
        product.setName(entity.getInventory().getProduct().getName());
        product.setSku(entity.getInventory().getProduct().getSku());
        product.setCategory(entity.getInventory().getProduct().getCategory().name());
        dto.setProduct(product);

        // Populate store
        StoreInfo store = new StoreInfo();
        store.setStoreId(entity.getInventory().getStore().getStoreId());
        store.setName(entity.getInventory().getStore().getName());
        store.setLocation(entity.getInventory().getStore().getLocation());
        dto.setStore(store);

        // Populate user
        UserInfo user = new UserInfo();
        user.setUserId(entity.getUser().getUserId());
        user.setName(entity.getUser().getName());
        user.setUsername(entity.getUser().getUsername());
        user.setEmail(entity.getUser().getEmail());
        dto.setUser(user);

        return dto;
    }
}