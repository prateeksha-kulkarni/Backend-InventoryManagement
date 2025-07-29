package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Timeout(10)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationService(notificationRepository);
    }

    @Test
    public void testGetAllNotifications() {
        NotificationEntity notification1 = mock(NotificationEntity.class);
        NotificationEntity notification2 = mock(NotificationEntity.class);
        List<NotificationEntity> expectedNotifications = Arrays.asList(notification1, notification2);
        when(notificationRepository.findAll()).thenReturn(expectedNotifications);
        List<NotificationEntity> result = notificationService.getAllNotifications();
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertEquals(expectedNotifications, result);
        verify(notificationRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAllNotificationsEmptyList() {
        List<NotificationEntity> expectedNotifications = Collections.emptyList();
        when(notificationRepository.findAll()).thenReturn(expectedNotifications);
        List<NotificationEntity> result = notificationService.getAllNotifications();
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        assertEquals(expectedNotifications, result);
        verify(notificationRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetNotificationsByStore() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID otherStoreId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        StoreEntity store1 = mock(StoreEntity.class);
        StoreEntity store2 = mock(StoreEntity.class);
        when(store1.getStoreId()).thenReturn(storeId);
        when(store2.getStoreId()).thenReturn(otherStoreId);
        NotificationEntity notification1 = mock(NotificationEntity.class);
        NotificationEntity notification2 = mock(NotificationEntity.class);
        NotificationEntity notification3 = mock(NotificationEntity.class);
        when(notification1.getStore()).thenReturn(store1);
        when(notification2.getStore()).thenReturn(store2);
        when(notification3.getStore()).thenReturn(store1);
        List<NotificationEntity> allNotifications = Arrays.asList(notification1, notification2, notification3);
        when(notificationRepository.findAll()).thenReturn(allNotifications);
        List<NotificationEntity> result = notificationService.getNotificationsByStore(storeId);
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(notification1));
        assertThat(result.get(1), equalTo(notification3));
        verify(notificationRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetNotificationsByStoreNoMatches() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID otherStoreId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        StoreEntity store1 = mock(StoreEntity.class);
        when(store1.getStoreId()).thenReturn(otherStoreId);
        NotificationEntity notification1 = mock(NotificationEntity.class);
        when(notification1.getStore()).thenReturn(store1);
        List<NotificationEntity> allNotifications = Arrays.asList(notification1);
        when(notificationRepository.findAll()).thenReturn(allNotifications);
        List<NotificationEntity> result = notificationService.getNotificationsByStore(storeId);
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(notificationRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetNotificationsByStoreEmptyList() {
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        List<NotificationEntity> allNotifications = Collections.emptyList();
        when(notificationRepository.findAll()).thenReturn(allNotifications);
        List<NotificationEntity> result = notificationService.getNotificationsByStore(storeId);
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(notificationRepository, atLeast(1)).findAll();
    }

    @Test
    public void testCreateNotification() {
        NotificationEntity notification = mock(NotificationEntity.class);
        NotificationEntity savedNotification = mock(NotificationEntity.class);
        when(notificationRepository.save(notification)).thenReturn(savedNotification);
        NotificationEntity result = notificationService.createNotification(notification);
        assertThat(result, is(notNullValue()));
        assertEquals(savedNotification, result);
        verify(notificationRepository, atLeast(1)).save(notification);
    }

    @Test
    public void testCreateNotificationWithNullInput() {
        NotificationEntity savedNotification = mock(NotificationEntity.class);
        when(notificationRepository.save(null)).thenReturn(savedNotification);
        NotificationEntity result = notificationService.createNotification(null);
        assertThat(result, is(notNullValue()));
        assertEquals(savedNotification, result);
        verify(notificationRepository, atLeast(1)).save(null);
    }
}
