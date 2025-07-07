package com.storeInventory.inventory_management.auth.model;//package com.storeInventory.inventory_management.auth.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
//import jakarta.persistence.*;
//import lombok.*;
//import java.util.UUID;
//
//@Entity
//@Table(name = "users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID userId;
//
//    @Column(nullable = false, unique = true)
//    private String username;
//
////    @Column(nullable = false)
////    private String passwordHash;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private UserRole role;
//
//    @Column(nullable = false)
//    private String name;
//

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

////    @Column(nullable = false)
////    private String password;
//
//    @Column(nullable = false)
//    private String password;
//
//
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    @JsonBackReference
//    private StoreEntity store;
//}
//


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)  // ✅ this stores the encrypted password
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private StoreEntity store;
}
