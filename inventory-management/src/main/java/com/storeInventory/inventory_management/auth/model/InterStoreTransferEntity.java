package com.storeInventory.inventory_management.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inter_store_transfer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterStoreTransferEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transfer_id")
    private UUID transferId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-transfer")
    private ProductEntity product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_store_id", nullable = false)
    @JsonBackReference("from-store-transfer")
    private StoreEntity fromStore;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_store_id", nullable = false)
    @JsonBackReference("to-store-transfer")
    private StoreEntity toStore;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status = TransferStatus.REQUESTED;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    @JsonBackReference("requested-by-transfer")
    private UserEntity requestedBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    @JsonBackReference("approved-by-transfer")
    private UserEntity approvedBy;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
} 