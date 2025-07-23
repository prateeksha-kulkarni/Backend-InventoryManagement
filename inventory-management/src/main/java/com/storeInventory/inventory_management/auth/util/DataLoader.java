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
            store1.setName("Lowe's Bengaluru");
            store1.setLocation("Bengaluru");
            storeRepository.save(store1);

            StoreEntity store2 = new StoreEntity();
            store2.setName("Lowe's Mumbai");
            store2.setLocation("Mumbai");
            storeRepository.save(store2);

            StoreEntity store3 = new StoreEntity();
            store3.setName("Lowe's Chennai");
            store3.setLocation("Chennai");
            storeRepository.save(store3);

            // Create users
            UserEntity admin = new UserEntity();
            admin.setUsername("admin.lowes");
            admin.setName("Ravi Shankar");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole(UserRole.ADMIN);
            admin.setEmail("ravi.shankar@lowes.in");
            admin.setStore(store1);
            userRepository.save(admin);

            UserEntity manager1 = new UserEntity();
            manager1.setUsername("manager.bangalore");
            manager1.setName("Ananya Reddy");
            manager1.setPassword(passwordEncoder.encode("manager1pass"));
            manager1.setRole(UserRole.MANAGER);
            manager1.setEmail("ananya.reddy@lowes.in");
            manager1.setStore(store1);
            userRepository.save(manager1);

            UserEntity manager2 = new UserEntity();
            manager2.setUsername("manager.mumbai");
            manager2.setName("Vikram Shah");
            manager2.setPassword(passwordEncoder.encode("manager2pass"));
            manager2.setRole(UserRole.MANAGER);
            manager2.setEmail("vikram.shah@lowes.in");
            manager2.setStore(store2);
            userRepository.save(manager2);

            UserEntity analyst = new UserEntity();
            analyst.setUsername("analyst.chennai");
            analyst.setName("Divya Suresh");
            analyst.setPassword(passwordEncoder.encode("analystpass"));
            analyst.setRole(UserRole.ANALYST);
            analyst.setEmail("divya.suresh@lowes.in");
            analyst.setStore(store3);
            userRepository.save(analyst);

            UserEntity associate = new UserEntity();
            associate.setUsername("associate.mumbai");
            associate.setName("Kunal Mehta");
            associate.setPassword(passwordEncoder.encode("associatepass"));
            associate.setRole(UserRole.ASSOCIATE);
            associate.setEmail("kunal.mehta@lowes.in");
            associate.setStore(store2);
            userRepository.save(associate);

            // Create products
            ProductEntity product1 = new ProductEntity();
            product1.setName("Power Drill");
            product1.setSku("PD-2023-IND");
            product1.setCategory(ProductCategory.ELECTRONICS);
            product1.setDescription("Cordless Power Drill - 20V");
            productRepository.save(product1);

            ProductEntity product2 = new ProductEntity();
            product2.setName("LED Bulb");
            product2.setSku("LED-9W-B22");
            product2.setCategory(ProductCategory.ELECTRONICS);
            product2.setDescription("9W B22 Cool White LED Bulb");
            productRepository.save(product2);

            ProductEntity product3 = new ProductEntity();
            product3.setName("Cement Bag");
            product3.setSku("CEM-BAG-50KG");
            product3.setCategory(ProductCategory.HOME_GOODS);
            product3.setDescription("50KG OPC 43 Grade Cement Bag");
            productRepository.save(product3);

            // Create inventory
            InventoryEntity inv1 = new InventoryEntity();
            inv1.setStore(store1);
            inv1.setProduct(product1);
            inv1.setQuantity(35);
            inv1.setMinThreshold(5);
            inventoryRepository.save(inv1);

            InventoryEntity inv2 = new InventoryEntity();
            inv2.setStore(store2);
            inv2.setProduct(product1);
            inv2.setQuantity(20);
            inv2.setMinThreshold(5);
            inventoryRepository.save(inv2);

            InventoryEntity inv3 = new InventoryEntity();
            inv3.setStore(store1);
            inv3.setProduct(product2);
            inv3.setQuantity(100);
            inv3.setMinThreshold(20);
            inventoryRepository.save(inv3);

            InventoryEntity inv4 = new InventoryEntity();
            inv4.setStore(store3);
            inv4.setProduct(product3);
            inv4.setQuantity(60);
            inv4.setMinThreshold(10);
            inventoryRepository.save(inv4);

            // Create stock adjustment (example only)
            StockAdjustmentEntity adjustment = new StockAdjustmentEntity();
            adjustment.setInventory(inv1);
            adjustment.setUser(manager1);
            adjustment.setChangeType(ChangeType.ADD);
            adjustment.setQuantityChange(10);
            adjustment.setReason("Restocking Power Drills");
            stockAdjustmentRepository.save(adjustment);

            // Create notification
            NotificationEntity notification = new NotificationEntity();
            notification.setStore(store1);
            notification.setProduct(product2);
            notification.setType(NotificationEntity.NotificationType.LOW_STOCK);
            notification.setIsRead(false);
            notificationRepository.save(notification);

            System.out.println("Seeded  data for users, products, stores, and inventory.");
        }
    }

}

