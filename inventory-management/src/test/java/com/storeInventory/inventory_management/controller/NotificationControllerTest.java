package com.storeInventory.inventory_management.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.NotificationController;
import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.service.NotificationService;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {NotificationController.class})
@DisabledInAotMode
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    @Autowired
    private NotificationController notificationController;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    @DisplayName("GET /api/notifications returns all notifications")
    void testGetAllNotifications() throws Exception {
        when(notificationService.getAllNotifications()).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/notifications");
        MockMvcBuilders.standaloneSetup(notificationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @DisplayName("GET /api/notifications/store/{storeId} returns notifications for store")
    void testGetNotificationsByStore() throws Exception {
        UUID storeId = UUID.randomUUID();
        when(notificationService.getNotificationsByStore(storeId)).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/notifications/store/{storeId}", storeId);
        MockMvcBuilders.standaloneSetup(notificationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @DisplayName("POST /api/notifications creates a notification")
    void testCreateNotification() throws Exception {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationId(UUID.randomUUID());
        when(notificationService.createNotification(any(NotificationEntity.class))).thenReturn(notification);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(notification);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);
        MockMvcBuilders.standaloneSetup(notificationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}
