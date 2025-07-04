package com.storeInventory.inventory_management.auth.dto.request;

import java.time.LocalDate;

public class InventoryTurnoverRequest {
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private LocalDate startDate;
    private LocalDate endDate;
}
