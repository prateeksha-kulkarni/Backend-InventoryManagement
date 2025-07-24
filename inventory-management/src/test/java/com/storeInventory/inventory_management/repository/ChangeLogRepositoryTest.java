package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.ChangeLogRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase
public class ChangeLogRepositoryTest {

    @Autowired
    ChangeLogRepository changeLogRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenSavedChangeLog_thenReturnNonNullLog() {
        UserEntity user = UserEntity.builder()
                .username("user1")
                .password("password")
                .email("user1@example.com")
                .role(UserRole.MANAGER)
                .name("User One")
                .build();
        user = userRepository.save(user);

        ChangeLogEntity log = new ChangeLogEntity();
        log.setTableName("product");
        log.setRecordId(UUID.randomUUID());
        log.setUser(user);
        log.setChangeSummary("Added new product");

        ChangeLogEntity savedLog = changeLogRepository.save(log);

        Assertions.assertNotNull(savedLog);
        Assertions.assertEquals("product", savedLog.getTableName());
        Assertions.assertEquals(user.getUserId(), savedLog.getUser().getUserId());
    }
} 