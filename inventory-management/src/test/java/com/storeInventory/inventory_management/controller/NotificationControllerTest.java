package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.NotificationController;
import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.service.NotificationService;
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
    controllers = NotificationController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    private NotificationEntity getSampleNotification() {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationId(UUID.randomUUID());
        notification.setType(NotificationEntity.NotificationType.LOW_STOCK);
        notification.setIsRead(false);
        return notification;
    }

    @Test
    void testGetAllNotifications() throws Exception {
        NotificationEntity notification = getSampleNotification();
        when(notificationService.getAllNotifications()).thenReturn(Collections.singletonList(notification));
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type", is("LOW_STOCK")));
    }

    @Test
    void testGetNotificationsByStore() throws Exception {
        UUID storeId = UUID.randomUUID();
        NotificationEntity notification = getSampleNotification();
        when(notificationService.getNotificationsByStore(eq(storeId))).thenReturn(Collections.singletonList(notification));
        mockMvc.perform(get("/api/notifications/store/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type", is("LOW_STOCK")));
    }

    @Test
    void testCreateNotification() throws Exception {
        NotificationEntity notification = getSampleNotification();
        when(notificationService.createNotification(any(NotificationEntity.class))).thenReturn(notification);
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("LOW_STOCK")));
    }
} 