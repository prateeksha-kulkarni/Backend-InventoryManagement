//package com.storeInventory.inventory_management.auth.controller;
//
//public class StoreController {
//}

package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<List<StoreEntity>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreEntity> getStoreById(@PathVariable UUID id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreEntity> createStore(@RequestBody StoreEntity store) {
        return ResponseEntity.ok(storeService.createStore(store));
    }

    @GetMapping("/check-name")
    public ResponseEntity<?> checkStoreName(@RequestParam String name) {
        boolean exists = storeService.storeNameExists(name);
        return ResponseEntity.ok().body(Collections.singletonMap("exists", exists));
    }


}

