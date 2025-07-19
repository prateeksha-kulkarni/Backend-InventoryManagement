package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InterStoreTransferController;
import com.storeInventory.inventory_management.auth.dto.TransferResponseDto;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.service.InterStoreTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = InterStoreTransferController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class InterStoreTransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterStoreTransferService interStoreTransferService;

    @Autowired
    private ObjectMapper objectMapper;

    private InterStoreTransferEntity getSampleTransfer() {
        InterStoreTransferEntity transfer = new InterStoreTransferEntity();
        transfer.setTransferId(UUID.randomUUID());
        transfer.setQuantity(10);
        transfer.setStatus(TransferStatus.REQUESTED);
        return transfer;
    }

    private TransferResponseDto getSampleResponseDto(InterStoreTransferEntity transfer) {
        TransferResponseDto dto = new TransferResponseDto();
        dto.setTransferId(transfer.getTransferId());
        dto.setQuantity(transfer.getQuantity());
        dto.setStatus(transfer.getStatus());
        return dto;
    }

    @Test
    void testGetAllTransfers() throws Exception {
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getAllTransfers()).thenReturn(Collections.singletonList(transfer));
        mockMvc.perform(get("/api/transfers/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    void testGetTransfersToStore() throws Exception {
        UUID storeId = UUID.randomUUID();
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getTransfersToStore(eq(storeId), any(TransferStatus.class))).thenReturn(Collections.singletonList(transfer));
        mockMvc.perform(get("/api/transfers/to/" + storeId + "?status=PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    void testGetTransfersFromStore() throws Exception {
        UUID storeId = UUID.randomUUID();
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getTransfersFromStore(eq(storeId))).thenReturn(Collections.singletonList(transfer));
        mockMvc.perform(get("/api/transfers/from/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    void testCreateTransfer() throws Exception {
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.createTransfer(any(InterStoreTransferEntity.class))).thenReturn(transfer);
        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void testUpdateTransfer() throws Exception {
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.updateTransfer(any(InterStoreTransferEntity.class))).thenReturn(transfer);
        mockMvc.perform(put("/api/transfers/" + transfer.getTransferId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void testGetTransferById() throws Exception {
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getTransferById(eq(transfer.getTransferId()))).thenReturn(transfer);
        mockMvc.perform(get("/api/transfers/" + transfer.getTransferId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void testGetTransferHistory() throws Exception {
        UUID storeId = UUID.randomUUID();
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getTransferHistoryForStore(eq(storeId))).thenReturn(Collections.singletonList(transfer));
        mockMvc.perform(get("/api/transfers/history/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    void testRejectTransfer_success() throws Exception {
        UUID transferId = UUID.randomUUID();
        mockMvc.perform(put("/api/transfers/" + transferId + "/reject"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer rejected successfully."));
    }

    @Test
    void testGetPendingTransfers() throws Exception {
        UUID storeId = UUID.randomUUID();
        InterStoreTransferEntity transfer = getSampleTransfer();
        when(interStoreTransferService.getTransfersToStoreByStatus(eq(storeId), any(TransferStatus.class))).thenReturn(Collections.singletonList(transfer));
        mockMvc.perform(get("/api/transfers/to/" + storeId + "?status=PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }
} 