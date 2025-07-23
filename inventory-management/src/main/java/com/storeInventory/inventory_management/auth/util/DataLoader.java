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
            StoreEntity mysuruStore = new StoreEntity();
            mysuruStore.setName("Mysuru Store");
            mysuruStore.setLocation("JLB Road, Mysuru");
            storeRepository.save(mysuruStore);

            StoreEntity bengaluruStore = new StoreEntity();
            bengaluruStore.setName("Bengaluru Store");
            bengaluruStore.setLocation("Koramangala, Bengaluru");
            storeRepository.save(bengaluruStore);

            // Create users
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setName("Sahana GN");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole(UserRole.ADMIN);
            admin.setEmail("sahanagnpari@gmail.com");
            admin.setStore(mysuruStore);
            userRepository.save(admin);

            UserEntity manager1 = new UserEntity();
            manager1.setUsername("manager_mysuru");
            manager1.setName("Ravi Kumar");
            manager1.setPassword(passwordEncoder.encode("managerpass"));
            manager1.setRole(UserRole.MANAGER);
            manager1.setEmail("manager1@store.com");
            manager1.setStore(mysuruStore);
            userRepository.save(manager1);

            UserEntity manager2 = new UserEntity();
            manager2.setUsername("manager_blr");
            manager2.setName("Divya R");
            manager2.setPassword(passwordEncoder.encode("managerpass"));
            manager2.setRole(UserRole.MANAGER);
            manager2.setEmail("manager2@store.com");
            manager2.setStore(bengaluruStore);
            userRepository.save(manager2);

            UserEntity analyst = new UserEntity();
            analyst.setUsername("analyst1");
            analyst.setName("Kiran S");
            analyst.setPassword(passwordEncoder.encode("analystpass"));
            analyst.setRole(UserRole.ANALYST);
            analyst.setEmail("analyst1@store.com");
            analyst.setStore(mysuruStore);
            userRepository.save(analyst);

            UserEntity associate = new UserEntity();
            associate.setUsername("associate1");
            associate.setName("Pooja M");
            associate.setPassword(passwordEncoder.encode("associatepass"));
            associate.setRole(UserRole.ASSOCIATE);
            associate.setEmail("associate1@store.com");
            associate.setStore(bengaluruStore);
            userRepository.save(associate);

            // Create products (non-food)
            ProductEntity laptop = new ProductEntity();
            laptop.setName("HP Pavilion Laptop");
            laptop.setSku("SKU-LAPTOP-001");
            laptop.setCategory(ProductCategory.ELECTRONICS);
            laptop.setDescription("HP Pavilion 15.6-inch, Intel i5, 8GB RAM");
            productRepository.save(laptop);

            ProductEntity fridge = new ProductEntity();
            fridge.setName("Samsung Double Door Fridge");
            fridge.setSku("SKU-FRIDGE-002");
            fridge.setCategory(ProductCategory.ELECTRONICS);
            fridge.setDescription("Samsung 253L 2 Star Refrigerator");
            productRepository.save(fridge);

            ProductEntity jeans = new ProductEntity();
            jeans.setName("Men's Slim Fit Jeans");
            jeans.setSku("SKU-JEANS-003");
            jeans.setCategory(ProductCategory.CLOTHING);
            jeans.setDescription("Blue Denim Slim Fit Jeans");
            productRepository.save(jeans);

            ProductEntity washingMachine = new ProductEntity();
            washingMachine.setName("LG Washing Machine");
            washingMachine.setSku("SKU-WASH-004");
            washingMachine.setCategory(ProductCategory.ELECTRONICS);
            washingMachine.setDescription("LG 7kg Front Load Washing Machine");
            productRepository.save(washingMachine);

            ProductEntity smartwatch = new ProductEntity();
            smartwatch.setName("Noise Smartwatch");
            smartwatch.setSku("SKU-WATCH-005");
            smartwatch.setCategory(ProductCategory.ELECTRONICS);
            smartwatch.setDescription("Noise ColorFit Ultra 2 with AMOLED Display");
            productRepository.save(smartwatch);

            // Inventory
            InventoryEntity inv1 = new InventoryEntity();
            inv1.setStore(mysuruStore);
            inv1.setProduct(laptop);
            inv1.setQuantity(20);
            inv1.setMinThreshold(5);
            inventoryRepository.save(inv1);

            InventoryEntity inv2 = new InventoryEntity();
            inv2.setStore(bengaluruStore);
            inv2.setProduct(fridge);
            inv2.setQuantity(10);
            inv2.setMinThreshold(2);
            inventoryRepository.save(inv2);

            InventoryEntity inv3 = new InventoryEntity();
            inv3.setStore(mysuruStore);
            inv3.setProduct(jeans);
            inv3.setQuantity(40);
            inv3.setMinThreshold(8);
            inventoryRepository.save(inv3);

            InventoryEntity inv4 = new InventoryEntity();
            inv4.setStore(bengaluruStore);
            inv4.setProduct(washingMachine);
            inv4.setQuantity(12);
            inv4.setMinThreshold(3);
            inventoryRepository.save(inv4);

            InventoryEntity inv5 = new InventoryEntity();
            inv5.setStore(mysuruStore);
            inv5.setProduct(smartwatch);
            inv5.setQuantity(25);
            inv5.setMinThreshold(5);
            inventoryRepository.save(inv5);

            // Inter-store transfer
            InterStoreTransferEntity transfer = new InterStoreTransferEntity();
            transfer.setProduct(smartwatch);
            transfer.setFromStore(mysuruStore);
            transfer.setToStore(bengaluruStore);
            transfer.setQuantity(5);
            transfer.setStatus(TransferStatus.REQUESTED);
            transfer.setRequestedBy(manager1);
            interStoreTransferRepository.save(transfer);

            // Stock adjustment
            StockAdjustmentEntity adjustment = new StockAdjustmentEntity();
            adjustment.setInventory(inv1);
            adjustment.setUser(manager1);
            adjustment.setChangeType(ChangeType.ADD);
            adjustment.setQuantityChange(10);
            adjustment.setReason("New laptop stock received");
            stockAdjustmentRepository.save(adjustment);

            // Notification
            NotificationEntity notification = new NotificationEntity();
            notification.setStore(bengaluruStore);
            notification.setProduct(fridge);
            notification.setType(NotificationEntity.NotificationType.LOW_STOCK);
            notification.setIsRead(false);
            notificationRepository.save(notification);

            // Change log
            ChangeLogEntity changeLog = new ChangeLogEntity();
            changeLog.setTableName("inventory");
            changeLog.setRecordId(inv1.getInventoryId());
            changeLog.setUser(manager1);
            changeLog.setChangeSummary("Added 10 laptops to Mysuru Store");
            changeLogRepository.save(changeLog);

            System.out.println("Seeded with electronics & clothing inventory (no food items).");
        }
    }
}
