package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.service.ChangeLogService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class ChangeLogControllerTest {

    @Mock
    private ChangeLogService changeLogService;

    private ChangeLogController changeLogController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        changeLogController = new ChangeLogController(changeLogService);
    }

    @Test
    public void testGetAllChangeLogsReturnsListOfChangeLogs() {
        // Arrange
        List<ChangeLogEntity> expectedChangeLogs = new ArrayList<>();
        ChangeLogEntity changeLog1 = mock(ChangeLogEntity.class);
        ChangeLogEntity changeLog2 = mock(ChangeLogEntity.class);
        expectedChangeLogs.add(changeLog1);
        expectedChangeLogs.add(changeLog2);
        doReturn(expectedChangeLogs).when(changeLogService).getAllChangeLogs();
        // Act
        ResponseEntity<List<ChangeLogEntity>> result = changeLogController.getAllChangeLogs();
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().size(), is(equalTo(2)));
        verify(changeLogService, atLeast(1)).getAllChangeLogs();
    }

    @Test
    public void testGetAllChangeLogsReturnsEmptyList() {
        // Arrange
        List<ChangeLogEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(changeLogService).getAllChangeLogs();
        // Act
        ResponseEntity<List<ChangeLogEntity>> result = changeLogController.getAllChangeLogs();
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().size(), is(equalTo(0)));
        verify(changeLogService, atLeast(1)).getAllChangeLogs();
    }

    @Test
    public void testGetChangeLogsByRecordIdReturnsListOfChangeLogs() {
        // Arrange
        UUID recordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        List<ChangeLogEntity> expectedChangeLogs = new ArrayList<>();
        ChangeLogEntity changeLog1 = mock(ChangeLogEntity.class);
        expectedChangeLogs.add(changeLog1);
        doReturn(expectedChangeLogs).when(changeLogService).getChangeLogsByRecordId(recordId);
        // Act
        ResponseEntity<List<ChangeLogEntity>> result = changeLogController.getChangeLogsByRecordId(recordId);
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().size(), is(equalTo(1)));
        verify(changeLogService, atLeast(1)).getChangeLogsByRecordId(recordId);
    }

    @Test
    public void testGetChangeLogsByRecordIdReturnsEmptyList() {
        // Arrange
        UUID recordId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        List<ChangeLogEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(changeLogService).getChangeLogsByRecordId(recordId);
        // Act
        ResponseEntity<List<ChangeLogEntity>> result = changeLogController.getChangeLogsByRecordId(recordId);
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().size(), is(equalTo(0)));
        verify(changeLogService, atLeast(1)).getChangeLogsByRecordId(recordId);
    }

    @Test
    public void testCreateChangeLogReturnsCreatedChangeLog() {
        // Arrange
        ChangeLogEntity inputChangeLog = mock(ChangeLogEntity.class);
        ChangeLogEntity createdChangeLog = mock(ChangeLogEntity.class);
        UUID expectedLogId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        doReturn(expectedLogId).when(createdChangeLog).getLogId();
        doReturn(createdChangeLog).when(changeLogService).createChangeLog(inputChangeLog);
        // Act
        ResponseEntity<ChangeLogEntity> result = changeLogController.createChangeLog(inputChangeLog);
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().getLogId(), is(equalTo(expectedLogId)));
        verify(changeLogService, atLeast(1)).createChangeLog(inputChangeLog);
    }

    @Test
    public void testCreateChangeLogWithNullInput() {
        // Arrange
        ChangeLogEntity nullChangeLog = null;
        ChangeLogEntity returnedChangeLog = mock(ChangeLogEntity.class);
        doReturn(returnedChangeLog).when(changeLogService).createChangeLog(nullChangeLog);
        // Act
        ResponseEntity<ChangeLogEntity> result = changeLogController.createChangeLog(nullChangeLog);
        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        verify(changeLogService, atLeast(1)).createChangeLog(nullChangeLog);
    }
}
