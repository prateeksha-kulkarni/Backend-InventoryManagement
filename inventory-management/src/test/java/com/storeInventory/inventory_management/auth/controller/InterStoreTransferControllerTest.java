package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.TransferResponseDto;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.service.InterStoreTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import java.time.LocalDateTime;

@Timeout(10)
public class InterStoreTransferControllerTest {

    @Mock
    private InterStoreTransferService transferService;

    private InterStoreTransferController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new InterStoreTransferController(transferService);
    }

    @Test
    public void testGetAllTransfers() {
        InterStoreTransferEntity transfer1 = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity transfer2 = mock(InterStoreTransferEntity.class);
        ProductEntity product1 = mock(ProductEntity.class);
        ProductEntity product2 = mock(ProductEntity.class);
        StoreEntity fromStore1 = mock(StoreEntity.class);
        StoreEntity fromStore2 = mock(StoreEntity.class);
        StoreEntity toStore1 = mock(StoreEntity.class);
        StoreEntity toStore2 = mock(StoreEntity.class);
        UserEntity requestedBy1 = mock(UserEntity.class);
        UserEntity requestedBy2 = mock(UserEntity.class);
        doReturn(product1).when(transfer1).getProduct();
        doReturn(fromStore1).when(transfer1).getFromStore();
        doReturn(toStore1).when(transfer1).getToStore();
        doReturn(requestedBy1).when(transfer1).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer1).getTransferId();
        doReturn(10).when(transfer1).getQuantity();
        doReturn(TransferStatus.REQUESTED).when(transfer1).getStatus();
        doReturn(LocalDateTime.now()).when(transfer1).getTimestamp();
        doReturn(product2).when(transfer2).getProduct();
        doReturn(fromStore2).when(transfer2).getFromStore();
        doReturn(toStore2).when(transfer2).getToStore();
        doReturn(requestedBy2).when(transfer2).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer2).getTransferId();
        doReturn(20).when(transfer2).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(transfer2).getStatus();
        doReturn(LocalDateTime.now()).when(transfer2).getTimestamp();
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer1, transfer2);
        doReturn(transfers).when(transferService).getAllTransfers();
        ResponseEntity<List<TransferResponseDto>> response = controller.getAllTransfers();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(transferService, atLeast(1)).getAllTransfers();
    }

    @Test
    public void testGetTransfersToStore() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        TransferStatus status = TransferStatus.REQUESTED;
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer);
        doReturn(transfers).when(transferService).getTransfersToStore(eq(storeId), eq(status));
        ResponseEntity<List<InterStoreTransferEntity>> response = controller.getTransfersToStore(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersToStore(eq(storeId), eq(status));
    }

    @Test
    public void testGetTransfersFromStore() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer);
        doReturn(transfers).when(transferService).getTransfersFromStore(eq(storeId));
        ResponseEntity<List<InterStoreTransferEntity>> response = controller.getTransfersFromStore(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersFromStore(eq(storeId));
    }

    @Test
    public void testCreateTransfer() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity createdTransfer = mock(InterStoreTransferEntity.class);
        doReturn(createdTransfer).when(transferService).createTransfer(eq(transfer));
        ResponseEntity<InterStoreTransferEntity> response = controller.createTransfer(transfer);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(createdTransfer, response.getBody());
        verify(transferService, atLeast(1)).createTransfer(eq(transfer));
    }

    @Test
    public void testUpdateTransfer() {
        UUID transferId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity updatedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(updatedTransfer).when(transferService).updateTransfer(eq(transfer));
        ResponseEntity<InterStoreTransferEntity> response = controller.updateTransfer(transferId, transfer);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(updatedTransfer, response.getBody());
        verify(transfer, atLeast(1)).setTransferId(eq(transferId));
        verify(transferService, atLeast(1)).updateTransfer(eq(transfer));
    }

    @Test
    public void testGetTransferById() {
        UUID transferId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(transfer).when(transferService).getTransferById(eq(transferId));
        ResponseEntity<InterStoreTransferEntity> response = controller.getTransferById(transferId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(transfer, response.getBody());
        verify(transferService, atLeast(1)).getTransferById(eq(transferId));
    }

    @Test
    public void testGetTransferHistory() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        InterStoreTransferEntity transfer1 = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity transfer2 = mock(InterStoreTransferEntity.class);
        ProductEntity product1 = mock(ProductEntity.class);
        ProductEntity product2 = mock(ProductEntity.class);
        StoreEntity fromStore1 = mock(StoreEntity.class);
        StoreEntity fromStore2 = mock(StoreEntity.class);
        StoreEntity toStore1 = mock(StoreEntity.class);
        StoreEntity toStore2 = mock(StoreEntity.class);
        UserEntity requestedBy1 = mock(UserEntity.class);
        UserEntity requestedBy2 = mock(UserEntity.class);
        doReturn(product1).when(transfer1).getProduct();
        doReturn(fromStore1).when(transfer1).getFromStore();
        doReturn(toStore1).when(transfer1).getToStore();
        doReturn(requestedBy1).when(transfer1).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer1).getTransferId();
        doReturn(15).when(transfer1).getQuantity();
        doReturn(TransferStatus.REQUESTED).when(transfer1).getStatus();
        doReturn(LocalDateTime.now()).when(transfer1).getTimestamp();
        doReturn(product2).when(transfer2).getProduct();
        doReturn(fromStore2).when(transfer2).getFromStore();
        doReturn(toStore2).when(transfer2).getToStore();
        doReturn(requestedBy2).when(transfer2).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer2).getTransferId();
        doReturn(25).when(transfer2).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(transfer2).getStatus();
        doReturn(LocalDateTime.now()).when(transfer2).getTimestamp();
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer1, transfer2);
        doReturn(transfers).when(transferService).getTransferHistoryForStore(eq(storeId));
        ResponseEntity<List<TransferResponseDto>> response = controller.getTransferHistory(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(transferService, atLeast(1)).getTransferHistoryForStore(eq(storeId));
    }

    @Test
    public void testRejectTransferSuccess() {
        UUID transferId = UUID.fromString("550e8400-e29b-41d4-a716-446655440005");
        ResponseEntity<String> response = controller.rejectTransfer(transferId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody(), equalTo("Transfer rejected successfully."));
        verify(transferService, atLeast(1)).rejectTransfer(eq(transferId));
    }

    @Test
    public void testRejectTransferIllegalStateException() {
        UUID transferId = UUID.fromString("550e8400-e29b-41d4-a716-446655440006");
        String errorMessage = "Transfer cannot be rejected in current state";
        doThrow(new IllegalStateException(errorMessage)).when(transferService).rejectTransfer(eq(transferId));
        ResponseEntity<String> response = controller.rejectTransfer(transferId);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertThat(response.getBody(), equalTo(errorMessage));
        verify(transferService, atLeast(1)).rejectTransfer(eq(transferId));
    }

    @Test
    public void testRejectTransferGeneralException() {
        UUID transferId = UUID.fromString("550e8400-e29b-41d4-a716-446655440007");
        doThrow(new RuntimeException("General error")).when(transferService).rejectTransfer(eq(transferId));
        ResponseEntity<String> response = controller.rejectTransfer(transferId);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(transferService, atLeast(1)).rejectTransfer(eq(transferId));
    }

    @Test
    public void testGetPendingTransfers() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440008");
        TransferStatus status = TransferStatus.REQUESTED;
        InterStoreTransferEntity transfer1 = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity transfer2 = mock(InterStoreTransferEntity.class);
        ProductEntity product1 = mock(ProductEntity.class);
        ProductEntity product2 = mock(ProductEntity.class);
        StoreEntity fromStore1 = mock(StoreEntity.class);
        StoreEntity fromStore2 = mock(StoreEntity.class);
        StoreEntity toStore1 = mock(StoreEntity.class);
        StoreEntity toStore2 = mock(StoreEntity.class);
        UserEntity requestedBy1 = mock(UserEntity.class);
        UserEntity requestedBy2 = mock(UserEntity.class);
        doReturn(product1).when(transfer1).getProduct();
        doReturn(fromStore1).when(transfer1).getFromStore();
        doReturn(toStore1).when(transfer1).getToStore();
        doReturn(requestedBy1).when(transfer1).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer1).getTransferId();
        doReturn(5).when(transfer1).getQuantity();
        doReturn(TransferStatus.REQUESTED).when(transfer1).getStatus();
        doReturn(LocalDateTime.now()).when(transfer1).getTimestamp();
        doReturn(product2).when(transfer2).getProduct();
        doReturn(fromStore2).when(transfer2).getFromStore();
        doReturn(toStore2).when(transfer2).getToStore();
        doReturn(requestedBy2).when(transfer2).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer2).getTransferId();
        doReturn(8).when(transfer2).getQuantity();
        doReturn(TransferStatus.REQUESTED).when(transfer2).getStatus();
        doReturn(LocalDateTime.now()).when(transfer2).getTimestamp();
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer1, transfer2);
        doReturn(transfers).when(transferService).getTransfersToStoreByStatus(eq(storeId), eq(status));
        ResponseEntity<List<TransferResponseDto>> response = controller.getPendingTransfers(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(transferService, atLeast(1)).getTransfersToStoreByStatus(eq(storeId), eq(status));
    }

    @Test
    public void testGetTransfersToStoreWithCompletedStatus() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440009");
        TransferStatus status = TransferStatus.COMPLETED;
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer);
        doReturn(transfers).when(transferService).getTransfersToStore(eq(storeId), eq(status));
        ResponseEntity<List<InterStoreTransferEntity>> response = controller.getTransfersToStore(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersToStore(eq(storeId), eq(status));
    }

    @Test
    public void testGetTransfersToStoreWithRejectedStatus() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440010");
        TransferStatus status = TransferStatus.REJECTED;
        List<InterStoreTransferEntity> transfers = Arrays.asList();
        doReturn(transfers).when(transferService).getTransfersToStore(eq(storeId), eq(status));
        ResponseEntity<List<InterStoreTransferEntity>> response = controller.getTransfersToStore(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersToStore(eq(storeId), eq(status));
    }

    @Test
    public void testGetPendingTransfersWithCompletedStatus() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");
        TransferStatus status = TransferStatus.COMPLETED;
        List<InterStoreTransferEntity> transfers = Arrays.asList();
        doReturn(transfers).when(transferService).getTransfersToStoreByStatus(eq(storeId), eq(status));
        ResponseEntity<List<TransferResponseDto>> response = controller.getPendingTransfers(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersToStoreByStatus(eq(storeId), eq(status));
    }

    @Test
    public void testGetPendingTransfersWithRejectedStatus() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
        TransferStatus status = TransferStatus.REJECTED;
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        UserEntity requestedBy = mock(UserEntity.class);
        doReturn(product).when(transfer).getProduct();
        doReturn(fromStore).when(transfer).getFromStore();
        doReturn(toStore).when(transfer).getToStore();
        doReturn(requestedBy).when(transfer).getRequestedBy();
        doReturn(UUID.randomUUID()).when(transfer).getTransferId();
        doReturn(12).when(transfer).getQuantity();
        doReturn(TransferStatus.REJECTED).when(transfer).getStatus();
        doReturn(LocalDateTime.now()).when(transfer).getTimestamp();
        List<InterStoreTransferEntity> transfers = Arrays.asList(transfer);
        doReturn(transfers).when(transferService).getTransfersToStoreByStatus(eq(storeId), eq(status));
        ResponseEntity<List<TransferResponseDto>> response = controller.getPendingTransfers(storeId, status);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(transferService, atLeast(1)).getTransfersToStoreByStatus(eq(storeId), eq(status));
    }

    @Test
    public void testCreateTransferWithNullTransfer() {
        InterStoreTransferEntity createdTransfer = mock(InterStoreTransferEntity.class);
        doReturn(createdTransfer).when(transferService).createTransfer(any());
        ResponseEntity<InterStoreTransferEntity> response = controller.createTransfer(null);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdTransfer, response.getBody());
        verify(transferService, atLeast(1)).createTransfer(any());
    }

    @Test
    public void testGetAllTransfersWithEmptyList() {
        List<InterStoreTransferEntity> transfers = Arrays.asList();
        doReturn(transfers).when(transferService).getAllTransfers();
        ResponseEntity<List<TransferResponseDto>> response = controller.getAllTransfers();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(transferService, atLeast(1)).getAllTransfers();
    }

    @Test
    public void testGetTransfersFromStoreWithEmptyList() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");
        List<InterStoreTransferEntity> transfers = Arrays.asList();
        doReturn(transfers).when(transferService).getTransfersFromStore(eq(storeId));
        ResponseEntity<List<InterStoreTransferEntity>> response = controller.getTransfersFromStore(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(transferService, atLeast(1)).getTransfersFromStore(eq(storeId));
    }

    @Test
    public void testGetTransferHistoryWithEmptyList() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440014");
        List<InterStoreTransferEntity> transfers = Arrays.asList();
        doReturn(transfers).when(transferService).getTransferHistoryForStore(eq(storeId));
        ResponseEntity<List<TransferResponseDto>> response = controller.getTransferHistory(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(transferService, atLeast(1)).getTransferHistoryForStore(eq(storeId));
    }

    @Test
    public void testControllerInstantiation() {
        InterStoreTransferController testController = new InterStoreTransferController(transferService);
        assertThat(testController, notNullValue());
    }
}
