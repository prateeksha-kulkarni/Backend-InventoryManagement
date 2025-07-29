package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private NotificationController notificationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService);
    }

    @Test
    public void testGetAllNotifications() {
        // Arrange
        List<NotificationEntity> expectedNotifications = new ArrayList<>();
        NotificationEntity notification1 = mock(NotificationEntity.class);
        NotificationEntity notification2 = mock(NotificationEntity.class);
        expectedNotifications.add(notification1);
        expectedNotifications.add(notification2);
        doReturn(expectedNotifications).when(notificationService).getAllNotifications();
        // Act
        ResponseEntity<List<NotificationEntity>> result = notificationController.getAllNotifications();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(equalTo(200)));
        assertThat(result.getBody(), is(equalTo(expectedNotifications)));
        verify(notificationService, atLeast(1)).getAllNotifications();
    }

    @Test
    public void testGetAllNotificationsWithEmptyList() {
        // Arrange
        List<NotificationEntity> emptyNotifications = new ArrayList<>();
        doReturn(emptyNotifications).when(notificationService).getAllNotifications();
        // Act
        ResponseEntity<List<NotificationEntity>> result = notificationController.getAllNotifications();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(equalTo(200)));
        assertThat(result.getBody(), is(equalTo(emptyNotifications)));
        verify(notificationService, atLeast(1)).getAllNotifications();
    }

    @Test
    public void testGetNotificationsByStore() {
        // Arrange
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        List<NotificationEntity> expectedNotifications = new ArrayList<>();
        NotificationEntity notification1 = mock(NotificationEntity.class);
        NotificationEntity notification2 = mock(NotificationEntity.class);
        expectedNotifications.add(notification1);
        expectedNotifications.add(notification2);
        doReturn(expectedNotifications).when(notificationService).getNotificationsByStore(storeId);
        // Act
        ResponseEntity<List<NotificationEntity>> result = notificationController.getNotificationsByStore(storeId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(equalTo(200)));
        assertThat(result.getBody(), is(equalTo(expectedNotifications)));
        verify(notificationService, atLeast(1)).getNotificationsByStore(storeId);
    }

    @Test
    public void testGetNotificationsByStoreWithEmptyList() {
        // Arrange
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        List<NotificationEntity> emptyNotifications = new ArrayList<>();
        doReturn(emptyNotifications).when(notificationService).getNotificationsByStore(storeId);
        // Act
        ResponseEntity<List<NotificationEntity>> result = notificationController.getNotificationsByStore(storeId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(equalTo(200)));
        assertThat(result.getBody(), is(equalTo(emptyNotifications)));
        verify(notificationService, atLeast(1)).getNotificationsByStore(storeId);
    }

    @Test
    public void testCreateNotification() {
        // Arrange
        NotificationEntity inputNotification = mock(NotificationEntity.class);
        NotificationEntity expectedNotification = mock(NotificationEntity.class);
        doReturn(expectedNotification).when(notificationService).createNotification(inputNotification);
        // Act
        ResponseEntity<NotificationEntity> result = notificationController.createNotification(inputNotification);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(equalTo(200)));
        assertThat(result.getBody(), is(equalTo(expectedNotification)));
        verify(notificationService, atLeast(1)).createNotification(inputNotification);
    }
}
