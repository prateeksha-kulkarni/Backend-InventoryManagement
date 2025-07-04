package com.storeInventory.inventory_management.auth.util;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import com.storeInventory.inventory_management.auth.repository.NotificationRepository;
import com.storeInventory.inventory_management.auth.repository.ChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InterStoreTransferRepository interStoreTransferRepository;

    @Autowired
    private StockAdjustmentRepository stockAdjustmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ChangeLogRepository changeLogRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0 && storeRepository.count() == 0 && productRepository.count() == 0) {
            // Create stores
            StoreEntity store1 = new StoreEntity();
            store1.setName("Store 1");
            store1.setLocation("Downtown");
            storeRepository.save(store1);

            StoreEntity store2 = new StoreEntity();
            store2.setName("Store 2");
            store2.setLocation("Uptown");
            storeRepository.save(store2);

            // Create users
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setName("Admin User");
            admin.setPasswordHash(passwordEncoder.encode("adminpass"));
            admin.setRole(UserRole.ADMIN);
            admin.setEmail("admin@store.com");
            admin.setStore(store1);
            userRepository.save(admin);

            UserEntity manager1 = new UserEntity();
            manager1.setUsername("manager1");
            manager1.setName("Manager One");
            manager1.setPasswordHash(passwordEncoder.encode("manager1pass"));
            manager1.setRole(UserRole.MANAGER);
            manager1.setEmail("manager1@store.com");
            manager1.setStore(store1);
            userRepository.save(manager1);

            UserEntity manager2 = new UserEntity();
            manager2.setUsername("manager2");
            manager2.setName("Manager Two");
            manager2.setPasswordHash(passwordEncoder.encode("manager2pass"));
            manager2.setRole(UserRole.MANAGER);
            manager2.setEmail("manager2@store.com");
            manager2.setStore(store2);
            userRepository.save(manager2);

            UserEntity analyst = new UserEntity();
            analyst.setUsername("analyst1");
            analyst.setName("Analyst One");
            analyst.setPasswordHash(passwordEncoder.encode("analystpass"));
            analyst.setRole(UserRole.ANALYST);
            analyst.setEmail("analyst1@store.com");
            analyst.setStore(store1);
            userRepository.save(analyst);

            UserEntity associate = new UserEntity();
            associate.setUsername("associate1");
            associate.setName("Associate One");
            associate.setPasswordHash(passwordEncoder.encode("associatepass"));
            associate.setRole(UserRole.ASSOCIATE);
            associate.setEmail("associate1@store.com");
            associate.setStore(store2);
            userRepository.save(associate);

            // Create products
            ProductEntity productA = new ProductEntity();
            productA.setName("ProductA");
            productA.setSku("SKU-PA-001");
            productA.setCategory(ProductCategory.FOOD);
            productA.setDescription("Sample Product A");
            productRepository.save(productA);

            ProductEntity productB = new ProductEntity();
            productB.setName("ProductB");
            productB.setSku("SKU-PB-002");
            productB.setCategory(ProductCategory.ELECTRONICS);
            productB.setDescription("Sample Product B");
            productRepository.save(productB);

            ProductEntity productC = new ProductEntity();
            productC.setName("ProductC");
            productC.setSku("SKU-PC-003");
            productC.setCategory(ProductCategory.CLOTHING);
            productC.setDescription("Sample Product C");
            productRepository.save(productC);

            // Create inventory
            InventoryEntity inv1 = new InventoryEntity();
            inv1.setStore(store1);
            inv1.setProduct(productA);
            inv1.setQuantity(100);
            inv1.setMinThreshold(10);
            inventoryRepository.save(inv1);

            InventoryEntity inv2 = new InventoryEntity();
            inv2.setStore(store2);
            inv2.setProduct(productA);
            inv2.setQuantity(50);
            inv2.setMinThreshold(5);
            inventoryRepository.save(inv2);

            InventoryEntity inv3 = new InventoryEntity();
            inv3.setStore(store1);
            inv3.setProduct(productB);
            inv3.setQuantity(30);
            inv3.setMinThreshold(3);
            inventoryRepository.save(inv3);

            InventoryEntity inv4 = new InventoryEntity();
            inv4.setStore(store1);
            inv4.setProduct(productC);
            inv4.setQuantity(75);
            inv4.setMinThreshold(7);
            inventoryRepository.save(inv4);

            // Create inter-store transfer
            InterStoreTransferEntity transfer = new InterStoreTransferEntity();
            transfer.setProduct(productA);
            transfer.setFromStore(store1);
            transfer.setToStore(store2);
            transfer.setQuantity(10);
            transfer.setStatus(TransferStatus.REQUESTED);
            transfer.setRequestedBy(manager1);
            transfer.setApprovedBy(manager2);
            interStoreTransferRepository.save(transfer);

            // Create stock adjustment
            StockAdjustmentEntity adjustment = new StockAdjustmentEntity();
            adjustment.setInventory(inv1);
            adjustment.setUser(manager1);
            adjustment.setChangeType(ChangeType.ADD);
            adjustment.setQuantityChange(20);
            adjustment.setReason("Restock");
            stockAdjustmentRepository.save(adjustment);

            // Create notification
            NotificationEntity notification = new NotificationEntity();
            notification.setStore(store1);
            notification.setProduct(productA);
            notification.setType(NotificationEntity.NotificationType.LOW_STOCK);
            notification.setIsRead(false);
            notificationRepository.save(notification);

            // Create change log
            ChangeLogEntity changeLog = new ChangeLogEntity();
            changeLog.setTableName("inventory");
            changeLog.setRecordId(inv1.getInventoryId());
            changeLog.setUser(manager1);
            changeLog.setChangeSummary("Added 20 units to inventory for ProductA in Store 1");
            changeLogRepository.save(changeLog);

            System.out.println("✅ Seeded all major tables: stores, users, products, inventory, transfer, adjustment, notification, changelog.");
        }
    }
}
