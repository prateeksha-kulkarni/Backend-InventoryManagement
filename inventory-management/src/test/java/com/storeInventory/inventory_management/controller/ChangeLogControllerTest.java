package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.ChangeLogController;
import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.service.ChangeLogService;
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
    controllers = ChangeLogController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class ChangeLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChangeLogService changeLogService;

    @Autowired
    private ObjectMapper objectMapper;

    private ChangeLogEntity getSampleChangeLog() {
        ChangeLogEntity log = new ChangeLogEntity();
        log.setLogId(UUID.randomUUID());
        log.setTableName("test_table");
        log.setRecordId(UUID.randomUUID());
        log.setChangeSummary("Test change summary");
        return log;
    }

    @Test
    void testGetAllChangeLogs() throws Exception {
        ChangeLogEntity log = getSampleChangeLog();
        when(changeLogService.getAllChangeLogs()).thenReturn(Collections.singletonList(log));
        mockMvc.perform(get("/api/changelog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].changeSummary", is("Test change summary")));
    }

    @Test
    void testGetChangeLogsByRecordId() throws Exception {
        UUID recordId = UUID.randomUUID();
        ChangeLogEntity log = getSampleChangeLog();
        when(changeLogService.getChangeLogsByRecordId(eq(recordId))).thenReturn(Collections.singletonList(log));
        mockMvc.perform(get("/api/changelog/record/" + recordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].changeSummary", is("Test change summary")));
    }

    @Test
    void testCreateChangeLog() throws Exception {
        ChangeLogEntity log = getSampleChangeLog();
        when(changeLogService.createChangeLog(any(ChangeLogEntity.class))).thenReturn(log);
        mockMvc.perform(post("/api/changelog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(log)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.changeSummary", is("Test change summary")));
    }
} 