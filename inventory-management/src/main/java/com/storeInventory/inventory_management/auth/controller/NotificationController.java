package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationEntity>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsByStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(notificationService.getNotificationsByStore(storeId));
    }

    @PostMapping
    public ResponseEntity<NotificationEntity> createNotification(@RequestBody NotificationEntity notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }
} 