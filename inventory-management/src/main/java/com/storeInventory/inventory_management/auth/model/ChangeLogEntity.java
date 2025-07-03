package com.storeInventory.inventory_management.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "change_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id")
    private UUID logId;
    
    @Column(name = "table_name", nullable = false)
    private String tableName;
    
    @Column(name = "record_id", nullable = false)
    private UUID recordId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "change_summary", columnDefinition = "TEXT")
    private String changeSummary;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
} 