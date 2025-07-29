package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.ChangeLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class ChangeLogServiceTest {

    @Mock
    private ChangeLogRepository changeLogRepository;

    private ChangeLogService changeLogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        changeLogService = new ChangeLogService(changeLogRepository);
    }

    @Test
    public void testGetAllChangeLogsReturnsAllChangeLogs() {
        // Arrange
        UUID logId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID logId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UUID recordId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        UUID recordId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        UserEntity user1 = mock(UserEntity.class);
        UserEntity user2 = mock(UserEntity.class);
        ChangeLogEntity changeLog1 = new ChangeLogEntity(logId1, "table1", recordId1, user1, "summary1", LocalDateTime.now());
        ChangeLogEntity changeLog2 = new ChangeLogEntity(logId2, "table2", recordId2, user2, "summary2", LocalDateTime.now());
        List<ChangeLogEntity> expectedChangeLogs = new ArrayList<>();
        expectedChangeLogs.add(changeLog1);
        expectedChangeLogs.add(changeLog2);
        doReturn(expectedChangeLogs).when(changeLogRepository).findAll();
        // Act
        List<ChangeLogEntity> result = changeLogService.getAllChangeLogs();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertEquals(changeLog1, result.get(0));
        assertEquals(changeLog2, result.get(1));
        verify(changeLogRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAllChangeLogsReturnsEmptyListWhenNoChangeLogs() {
        // Arrange
        List<ChangeLogEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(changeLogRepository).findAll();
        // Act
        List<ChangeLogEntity> result = changeLogService.getAllChangeLogs();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(changeLogRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetChangeLogsByRecordIdReturnsMatchingLogs() {
        // Arrange
        UUID targetRecordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID otherRecordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UUID logId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        UUID logId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440004");
        UUID logId3 = UUID.fromString("550e8400-e29b-41d4-a716-446655440005");
        UserEntity user = mock(UserEntity.class);
        ChangeLogEntity changeLog1 = new ChangeLogEntity(logId1, "table1", targetRecordId, user, "summary1", LocalDateTime.now());
        ChangeLogEntity changeLog2 = new ChangeLogEntity(logId2, "table2", otherRecordId, user, "summary2", LocalDateTime.now());
        ChangeLogEntity changeLog3 = new ChangeLogEntity(logId3, "table3", targetRecordId, user, "summary3", LocalDateTime.now());
        List<ChangeLogEntity> allChangeLogs = new ArrayList<>();
        allChangeLogs.add(changeLog1);
        allChangeLogs.add(changeLog2);
        allChangeLogs.add(changeLog3);
        doReturn(allChangeLogs).when(changeLogRepository).findAll();
        // Act
        List<ChangeLogEntity> result = changeLogService.getChangeLogsByRecordId(targetRecordId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertEquals(changeLog1, result.get(0));
        assertEquals(changeLog3, result.get(1));
        verify(changeLogRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetChangeLogsByRecordIdReturnsEmptyListWhenNoMatches() {
        // Arrange
        UUID targetRecordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID otherRecordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UUID logId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        UserEntity user = mock(UserEntity.class);
        ChangeLogEntity changeLog = new ChangeLogEntity(logId, "table1", otherRecordId, user, "summary", LocalDateTime.now());
        List<ChangeLogEntity> allChangeLogs = new ArrayList<>();
        allChangeLogs.add(changeLog);
        doReturn(allChangeLogs).when(changeLogRepository).findAll();
        // Act
        List<ChangeLogEntity> result = changeLogService.getChangeLogsByRecordId(targetRecordId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(changeLogRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetChangeLogsByRecordIdWithEmptyRepository() {
        // Arrange
        UUID targetRecordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        List<ChangeLogEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(changeLogRepository).findAll();
        // Act
        List<ChangeLogEntity> result = changeLogService.getChangeLogsByRecordId(targetRecordId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(changeLogRepository, atLeast(1)).findAll();
    }

    @Test
    public void testCreateChangeLogSavesAndReturnsChangeLog() {
        // Arrange
        UUID logId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID recordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UserEntity user = mock(UserEntity.class);
        ChangeLogEntity inputChangeLog = new ChangeLogEntity(logId, "tableName", recordId, user, "changeSummary", LocalDateTime.now());
        ChangeLogEntity savedChangeLog = new ChangeLogEntity(logId, "tableName", recordId, user, "changeSummary", LocalDateTime.now());
        doReturn(savedChangeLog).when(changeLogRepository).save(inputChangeLog);
        // Act
        ChangeLogEntity result = changeLogService.createChangeLog(inputChangeLog);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, equalTo(savedChangeLog));
        verify(changeLogRepository, atLeast(1)).save(inputChangeLog);
    }

    @Test
    public void testCreateChangeLogWithNullChangeSummary() {
        // Arrange
        UUID logId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID recordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UserEntity user = mock(UserEntity.class);
        ChangeLogEntity inputChangeLog = new ChangeLogEntity(logId, "tableName", recordId, user, null, LocalDateTime.now());
        ChangeLogEntity savedChangeLog = new ChangeLogEntity(logId, "tableName", recordId, user, null, LocalDateTime.now());
        doReturn(savedChangeLog).when(changeLogRepository).save(inputChangeLog);
        // Act
        ChangeLogEntity result = changeLogService.createChangeLog(inputChangeLog);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, equalTo(savedChangeLog));
        verify(changeLogRepository, atLeast(1)).save(inputChangeLog);
    }

    @Test
    public void testCreateChangeLogWithMinimalData() {
        // Arrange
        UUID logId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID recordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UserEntity user = mock(UserEntity.class);
        ChangeLogEntity inputChangeLog = new ChangeLogEntity();
        inputChangeLog.setLogId(logId);
        inputChangeLog.setTableName("minimalTable");
        inputChangeLog.setRecordId(recordId);
        inputChangeLog.setUser(user);
        inputChangeLog.setChangeSummary("");
        inputChangeLog.setTimestamp(LocalDateTime.now());
        ChangeLogEntity savedChangeLog = new ChangeLogEntity();
        savedChangeLog.setLogId(logId);
        savedChangeLog.setTableName("minimalTable");
        savedChangeLog.setRecordId(recordId);
        savedChangeLog.setUser(user);
        savedChangeLog.setChangeSummary("");
        savedChangeLog.setTimestamp(LocalDateTime.now());
        doReturn(savedChangeLog).when(changeLogRepository).save(inputChangeLog);
        // Act
        ChangeLogEntity result = changeLogService.createChangeLog(inputChangeLog);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, equalTo(savedChangeLog));
        verify(changeLogRepository, atLeast(1)).save(inputChangeLog);
    }
}
