//package com.storeInventory.inventory_management.auth.repository;
//
//public interface StoreRepository {
//}

package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {
    Optional<StoreEntity> findByName(String name);
}
