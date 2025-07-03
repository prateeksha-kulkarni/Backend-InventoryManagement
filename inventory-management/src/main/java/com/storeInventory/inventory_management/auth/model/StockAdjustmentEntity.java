package com.storeInventory.inventory_management.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_adjustment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "adjustment_id")
    private UUID adjustmentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    @JsonBackReference
    private InventoryEntity inventory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private ChangeType changeType;
    
    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;
    
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
} 