package com.storeInventory.inventory_management.auth.dto;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.ProductStatus;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InventoryResponseDto {
    private UUID inventoryId;
    private StoreInfo store;
    private ProductInfo product;
    private Integer quantity;
    private Integer minThreshold;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime updatedAt;

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
        private ProductCategory category;
    }

    public static InventoryResponseDto fromEntity(InventoryEntity entity) {
        InventoryResponseDto dto = new InventoryResponseDto();
        dto.setInventoryId(entity.getInventoryId());
        dto.setQuantity(entity.getQuantity());
        dto.setMinThreshold(entity.getMinThreshold());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Set store info
        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreId(entity.getStore().getStoreId());
        storeInfo.setName(entity.getStore().getName());
        storeInfo.setLocation(entity.getStore().getLocation());
        dto.setStore(storeInfo);

        // Set product info
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(entity.getProduct().getProductId());
        productInfo.setName(entity.getProduct().getName());
        productInfo.setSku(entity.getProduct().getSku());
        productInfo.setCategory(entity.getProduct().getCategory());
        dto.setProduct(productInfo);

        // Add stock status logic
        int quantity = entity.getQuantity();
        int threshold = entity.getMinThreshold();
        if (quantity <= threshold) dto.setStatus(ProductStatus.LOW_STOCK);
        else if (quantity < 2 * threshold) dto.setStatus(ProductStatus.REORDER_SOON);
        else dto.setStatus(ProductStatus.IN_STOCK);

        return dto;
    }
}
