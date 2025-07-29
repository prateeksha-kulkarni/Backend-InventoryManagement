package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class InterStoreTransferServiceTest {

    @Mock
    private InterStoreTransferRepository transferRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    private InterStoreTransferService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new InterStoreTransferService(transferRepository, inventoryRepository, userRepository, storeRepository, productRepository);
    }

    @Test
    public void testGetTransfersToStore() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        TransferStatus status = TransferStatus.REQUESTED;
        List<InterStoreTransferEntity> expectedTransfers = new ArrayList<>();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        expectedTransfers.add(transfer);
        doReturn(expectedTransfers).when(transferRepository).findByToStore_StoreIdAndStatus(eq(storeId), eq(status));
        List<InterStoreTransferEntity> result = service.getTransfersToStore(storeId, status);
        assertEquals(expectedTransfers, result);
        verify(transferRepository, atLeast(1)).findByToStore_StoreIdAndStatus(eq(storeId), eq(status));
    }

    @Test
    public void testGetTransfersFromStore() {
        UUID fromStoreId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        List<InterStoreTransferEntity> expectedTransfers = new ArrayList<>();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        expectedTransfers.add(transfer);
        doReturn(expectedTransfers).when(transferRepository).findByFromStore_StoreId(eq(fromStoreId));
        List<InterStoreTransferEntity> result = service.getTransfersFromStore(fromStoreId);
        assertEquals(expectedTransfers, result);
        verify(transferRepository, atLeast(1)).findByFromStore_StoreId(eq(fromStoreId));
    }

    @Test
    public void testGetAllTransfers() {
        List<InterStoreTransferEntity> expectedTransfers = new ArrayList<>();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        expectedTransfers.add(transfer);
        doReturn(expectedTransfers).when(transferRepository).findAll();
        List<InterStoreTransferEntity> result = service.getAllTransfers();
        assertEquals(expectedTransfers, result);
        verify(transferRepository, atLeast(1)).findAll();
    }

    @Test
    public void testCreateTransferWithRequestedBy() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        UserEntity requestedByUser = mock(UserEntity.class);
        UserEntity savedUser = mock(UserEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(requestedByUser).when(transfer).getRequestedBy();
        doReturn("testuser").when(requestedByUser).getUsername();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(savedUser)).when(userRepository).findByUsername(eq("testuser"));
        doReturn(savedTransfer).when(transferRepository).save(eq(transfer));
        InterStoreTransferEntity result = service.createTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(transfer, atLeast(1)).setRequestedBy(eq(savedUser));
        verify(transferRepository, atLeast(1)).save(eq(transfer));
    }

    @Test
    public void testCreateTransferWithApprovedBy() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        UserEntity approvedByUser = mock(UserEntity.class);
        UserEntity savedUser = mock(UserEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(null).when(transfer).getRequestedBy();
        doReturn(approvedByUser).when(transfer).getApprovedBy();
        doReturn("approver").when(approvedByUser).getUsername();
        doReturn(Optional.of(savedUser)).when(userRepository).findByUsername(eq("approver"));
        doReturn(savedTransfer).when(transferRepository).save(eq(transfer));
        InterStoreTransferEntity result = service.createTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(transfer, atLeast(1)).setApprovedBy(eq(savedUser));
        verify(transferRepository, atLeast(1)).save(eq(transfer));
    }

    @Test
    public void testCreateTransferRequestedByUserNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        UserEntity requestedByUser = mock(UserEntity.class);
        doReturn(requestedByUser).when(transfer).getRequestedBy();
        doReturn("nonexistent").when(requestedByUser).getUsername();
        doReturn(Optional.empty()).when(userRepository).findByUsername(eq("nonexistent"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createTransfer(transfer));
        assertEquals("User not found: nonexistent", exception.getMessage());
    }

    @Test
    public void testCreateTransferApprovedByUserNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        UserEntity approvedByUser = mock(UserEntity.class);
        doReturn(null).when(transfer).getRequestedBy();
        doReturn(approvedByUser).when(transfer).getApprovedBy();
        doReturn("nonexistent").when(approvedByUser).getUsername();
        doReturn(Optional.empty()).when(userRepository).findByUsername(eq("nonexistent"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createTransfer(transfer));
        assertEquals("User not found: nonexistent", exception.getMessage());
    }

    @Test
    public void testCreateTransferWithNullRequestedBy() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(null).when(transfer).getRequestedBy();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(savedTransfer).when(transferRepository).save(eq(transfer));
        InterStoreTransferEntity result = service.createTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(transferRepository, atLeast(1)).save(eq(transfer));
    }

    @Test
    public void testUpdateTransferWithNullTransferId() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(null).when(transfer).getTransferId();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("transferId is required", exception.getMessage());
    }

    @Test
    public void testUpdateTransferNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(Optional.empty()).when(transferRepository).findById(eq(transferId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("Transfer not found", exception.getMessage());
    }

    @Test
    public void testUpdateTransferCompleted() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        InventoryEntity fulfillerInventory = mock(InventoryEntity.class);
        InventoryEntity requesterInventory = mock(InventoryEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.COMPLETED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(10).when(dbTransfer).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(dbTransfer).getStatus();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.of(product)).when(productRepository).findById(eq(productId));
        doReturn(Optional.of(fulfillerInventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(toStoreId), eq(productId));
        doReturn(15).when(fulfillerInventory).getQuantity();
        doReturn(Optional.of(requesterInventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(fromStoreId), eq(productId));
        doReturn(5).when(requesterInventory).getQuantity();
        doReturn(savedTransfer).when(transferRepository).save(eq(dbTransfer));
        InterStoreTransferEntity result = service.updateTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(dbTransfer, atLeast(1)).setStatus(eq(TransferStatus.COMPLETED));
        verify(fulfillerInventory, atLeast(1)).setQuantity(eq(5));
        verify(requesterInventory, atLeast(1)).setQuantity(eq(15));
        verify(inventoryRepository, atLeast(1)).save(eq(fulfillerInventory));
        verify(inventoryRepository, atLeast(1)).save(eq(requesterInventory));
    }

    @Test
    public void testUpdateTransferCompletedInsufficientStock() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        InventoryEntity fulfillerInventory = mock(InventoryEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.COMPLETED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(20).when(dbTransfer).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(dbTransfer).getStatus();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.of(product)).when(productRepository).findById(eq(productId));
        doReturn(Optional.of(fulfillerInventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(toStoreId), eq(productId));
        doReturn(10).when(fulfillerInventory).getQuantity();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("Not enough stock at current store to fulfill the request", exception.getMessage());
    }

    @Test
    public void testUpdateTransferCompletedFulfillerInventoryNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.COMPLETED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(10).when(dbTransfer).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(dbTransfer).getStatus();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.of(product)).when(productRepository).findById(eq(productId));
        doReturn(Optional.empty()).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(toStoreId), eq(productId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("Insufficient stock at destination store", exception.getMessage());
    }

    @Test
    public void testUpdateTransferCompletedCreateNewRequesterInventory() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        InventoryEntity fulfillerInventory = mock(InventoryEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.COMPLETED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(10).when(dbTransfer).getQuantity();
        doReturn(TransferStatus.COMPLETED).when(dbTransfer).getStatus();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.of(product)).when(productRepository).findById(eq(productId));
        doReturn(Optional.of(fulfillerInventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(toStoreId), eq(productId));
        doReturn(15).when(fulfillerInventory).getQuantity();
        doReturn(Optional.empty()).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(eq(fromStoreId), eq(productId));
        doReturn(savedTransfer).when(transferRepository).save(eq(dbTransfer));
        InventoryEntity newInventory = mock(InventoryEntity.class);
        doReturn(newInventory).when(inventoryRepository).save(any(InventoryEntity.class));
        InterStoreTransferEntity result = service.updateTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(dbTransfer, atLeast(1)).setStatus(eq(TransferStatus.COMPLETED));
        verify(fulfillerInventory, atLeast(1)).setQuantity(eq(5));
        verify(inventoryRepository, atLeast(1)).save(eq(fulfillerInventory));
        verify(inventoryRepository, atLeast(1)).save(any(InventoryEntity.class));
    }

    @Test
    public void testUpdateTransferWithApprovedBy() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UserEntity approvedByUser = mock(UserEntity.class);
        UserEntity savedUser = mock(UserEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(approvedByUser).when(transfer).getApprovedBy();
        doReturn("approver").when(approvedByUser).getUsername();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(null).when(dbTransfer).getFromStore();
        doReturn(null).when(dbTransfer).getToStore();
        doReturn(null).when(dbTransfer).getProduct();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(TransferStatus.REQUESTED).when(dbTransfer).getStatus();
        doReturn(Optional.of(savedUser)).when(userRepository).findByUsername(eq("approver"));
        doReturn(savedTransfer).when(transferRepository).save(eq(dbTransfer));
        InterStoreTransferEntity result = service.updateTransfer(transfer);
        assertEquals(savedTransfer, result);
        verify(dbTransfer, atLeast(1)).setStatus(eq(TransferStatus.REQUESTED));
        verify(dbTransfer, atLeast(1)).setApprovedBy(eq(savedUser));
    }

    @Test
    public void testUpdateTransferFromStoreNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(null).when(dbTransfer).getToStore();
        doReturn(null).when(dbTransfer).getProduct();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(TransferStatus.REQUESTED).when(dbTransfer).getStatus();
        doReturn(Optional.empty()).when(storeRepository).findById(eq(fromStoreId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("From store not found", exception.getMessage());
    }

    @Test
    public void testUpdateTransferToStoreNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(null).when(dbTransfer).getProduct();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(TransferStatus.REQUESTED).when(dbTransfer).getStatus();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.empty()).when(storeRepository).findById(eq(toStoreId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("To store not found", exception.getMessage());
    }

    @Test
    public void testUpdateTransferProductNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(null).when(dbTransfer).getRequestedBy();
        doReturn(TransferStatus.REQUESTED).when(dbTransfer).getStatus();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.empty()).when(productRepository).findById(eq(productId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testUpdateTransferRequestedByNotFound() {
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity dbTransfer = mock(InterStoreTransferEntity.class);
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID fromStoreId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        UUID toStoreId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000");
        StoreEntity fromStore = mock(StoreEntity.class);
        StoreEntity toStore = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        UserEntity requestedBy = mock(UserEntity.class);
        doReturn(transferId).when(transfer).getTransferId();
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(null).when(transfer).getApprovedBy();
        doReturn(Optional.of(dbTransfer)).when(transferRepository).findById(eq(transferId));
        doReturn(fromStore).when(dbTransfer).getFromStore();
        doReturn(fromStoreId).when(fromStore).getStoreId();
        doReturn(toStore).when(dbTransfer).getToStore();
        doReturn(toStoreId).when(toStore).getStoreId();
        doReturn(product).when(dbTransfer).getProduct();
        doReturn(productId).when(product).getProductId();
        doReturn(requestedBy).when(dbTransfer).getRequestedBy();
        doReturn("testuser").when(requestedBy).getUsername();
        doReturn(TransferStatus.REQUESTED).when(dbTransfer).getStatus();
        doReturn(Optional.of(fromStore)).when(storeRepository).findById(eq(fromStoreId));
        doReturn(Optional.of(toStore)).when(storeRepository).findById(eq(toStoreId));
        doReturn(Optional.of(product)).when(productRepository).findById(eq(productId));
        doReturn(Optional.empty()).when(userRepository).findByUsername(eq("testuser"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateTransfer(transfer));
        assertEquals("User not found: testuser", exception.getMessage());
    }

    @Test
    public void testGetTransferById() {
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(Optional.of(transfer)).when(transferRepository).findById(eq(transferId));
        InterStoreTransferEntity result = service.getTransferById(transferId);
        assertEquals(transfer, result);
        verify(transferRepository, atLeast(1)).findById(eq(transferId));
    }

    @Test
    public void testGetTransferByIdNotFound() {
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doReturn(Optional.empty()).when(transferRepository).findById(eq(transferId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getTransferById(transferId));
        assertEquals("Transfer not found", exception.getMessage());
    }

    @Test
    public void testGetTransferHistoryForStore() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        List<InterStoreTransferEntity> expectedTransfers = new ArrayList<>();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        expectedTransfers.add(transfer);
        doReturn(expectedTransfers).when(transferRepository).findByFromStore_StoreIdOrToStore_StoreId(eq(storeId), eq(storeId));
        List<InterStoreTransferEntity> result = service.getTransferHistoryForStore(storeId);
        assertEquals(expectedTransfers, result);
        verify(transferRepository, atLeast(1)).findByFromStore_StoreIdOrToStore_StoreId(eq(storeId), eq(storeId));
    }

    @Test
    public void testRejectTransfer() {
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        InterStoreTransferEntity savedTransfer = mock(InterStoreTransferEntity.class);
        doReturn(Optional.of(transfer)).when(transferRepository).findById(eq(transferId));
        doReturn(TransferStatus.REQUESTED).when(transfer).getStatus();
        doReturn(savedTransfer).when(transferRepository).save(eq(transfer));
        service.rejectTransfer(transferId);
        verify(transfer, atLeast(1)).setStatus(eq(TransferStatus.REJECTED));
        verify(transferRepository, atLeast(1)).save(eq(transfer));
    }

    @Test
    public void testRejectTransferNotFound() {
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doReturn(Optional.empty()).when(transferRepository).findById(eq(transferId));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.rejectTransfer(transferId));
        assertEquals("Transfer not found", exception.getMessage());
    }

    @Test
    public void testRejectTransferNotInRequestedState() {
        UUID transferId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(Optional.of(transfer)).when(transferRepository).findById(eq(transferId));
        doReturn(TransferStatus.COMPLETED).when(transfer).getStatus();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.rejectTransfer(transferId));
        assertEquals("Only transfers in REQUESTED state can be rejected.", exception.getMessage());
    }

    @Test
    public void testGetTransfersToStoreByStatus() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        TransferStatus status = TransferStatus.REQUESTED;
        List<InterStoreTransferEntity> expectedTransfers = new ArrayList<>();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        expectedTransfers.add(transfer);
        doReturn(expectedTransfers).when(transferRepository).fetchTransfersToStoreWithDetails(eq(storeId), eq(status));
        List<InterStoreTransferEntity> result = service.getTransfersToStoreByStatus(storeId, status);
        assertEquals(expectedTransfers, result);
        verify(transferRepository, atLeast(1)).fetchTransfersToStoreWithDetails(eq(storeId), eq(status));
    }
}
