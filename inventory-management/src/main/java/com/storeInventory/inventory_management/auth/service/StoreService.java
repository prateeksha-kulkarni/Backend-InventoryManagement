//package com.storeInventory.inventory_management.auth.service;
//
//public class StoreService {
//}


package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public List<StoreEntity> getAllStores() {
        return storeRepository.findAll();
    }

    public StoreEntity getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    public StoreEntity createStore(StoreEntity store) {
        return storeRepository.save(store);
    }
}
