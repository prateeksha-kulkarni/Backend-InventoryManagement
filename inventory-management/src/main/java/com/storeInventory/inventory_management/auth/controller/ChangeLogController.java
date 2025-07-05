package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/changelog")
@RequiredArgsConstructor
public class ChangeLogController {
    private final ChangeLogService changeLogService;

    @GetMapping
    public ResponseEntity<List<ChangeLogEntity>> getAllChangeLogs() {
        return ResponseEntity.ok(changeLogService.getAllChangeLogs());
    }

    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<ChangeLogEntity>> getChangeLogsByRecordId(@PathVariable UUID recordId) {
        return ResponseEntity.ok(changeLogService.getChangeLogsByRecordId(recordId));
    }

    @PostMapping
    public ResponseEntity<ChangeLogEntity> createChangeLog(@RequestBody ChangeLogEntity changeLog) {
        return ResponseEntity.ok(changeLogService.createChangeLog(changeLog));
    }
} 