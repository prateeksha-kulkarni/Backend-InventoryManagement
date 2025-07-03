package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<NotificationEntity> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<NotificationEntity> getNotificationsByStore(UUID storeId) {
        return notificationRepository.findAll().stream()
                .filter(n -> n.getStore().getStoreId().equals(storeId))
                .toList();
    }

    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }
} 