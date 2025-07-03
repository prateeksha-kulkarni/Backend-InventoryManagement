package com.storeInventory.inventory_management.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id")
    private UUID notificationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference
    private StoreEntity store;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private ProductEntity product;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
    
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    @CreationTimestamp
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
    
    public enum NotificationType {
        LOW_STOCK,
        STOCKOUT,
        TRANSFER_REQUEST,
        TRANSFER_COMPLETED
    }
} 