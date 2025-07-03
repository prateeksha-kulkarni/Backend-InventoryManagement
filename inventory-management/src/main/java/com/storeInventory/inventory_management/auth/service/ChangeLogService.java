package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.repository.ChangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeLogService {
    private final ChangeLogRepository changeLogRepository;

    public List<ChangeLogEntity> getAllChangeLogs() {
        return changeLogRepository.findAll();
    }

    public List<ChangeLogEntity> getChangeLogsByRecordId(UUID recordId) {
        // This assumes you want to filter by recordId
        return changeLogRepository.findAll().stream()
                .filter(log -> log.getRecordId().equals(recordId))
                .toList();
    }

    public ChangeLogEntity createChangeLog(ChangeLogEntity changeLog) {
        return changeLogRepository.save(changeLog);
    }
} 