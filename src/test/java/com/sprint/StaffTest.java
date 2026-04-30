package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StaffTest {

    @Autowired
    private MockMvc mockMvc;

    // =====================================================
    // GROUP 1 — findByStore_StoreId
    // Endpoint: /staff/search/findByStore_StoreId
    // =====================================================

    // Test 1 — valid storeId
    @Test
    public void test01_findByStore_success() throws Exception {
        mockMvc.perform(get("/staff/search/findByStore_StoreId")
                .param("storeId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // Test 2 — no results
    @Test
    public void test02_findByStore_noResult() throws Exception {
        mockMvc.perform(get("/staff/search/findByStore_StoreId")
                .param("storeId", "999"))
                .andExpect(status().isNotFound());
    }

    // Test 3 — missing storeId
    @Test
    public void test03_findByStore_missingParam() throws Exception {
        mockMvc.perform(get("/staff/search/findByStore_StoreId"))
                .andExpect(status().isNotFound());
    }

    // Test 4 — invalid storeId type
    @Test
    public void test04_findByStore_invalidParam() throws Exception {
        mockMvc.perform(get("/staff/search/findByStore_StoreId")
                .param("storeId", "abc"))
                .andExpect(status().isInternalServerError());
    }

    // =====================================================
    // GROUP 2 — findByFirstNameContainingIgnoreCaseAndStore_StoreId
    // Endpoint: /staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId
    // =====================================================

    // Test 5 — valid name + store
    @Test
    public void test05_searchAndStore_success() throws Exception {
        mockMvc.perform(get("/staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId")
                .param("name", "Mike")
                .param("storeId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // Test 6 — no results
    @Test
    public void test06_searchAndStore_noResult() throws Exception {
        mockMvc.perform(get("/staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId")
                .param("name", "XYZ")
                .param("storeId", "1"))
                .andExpect(status().isNotFound());
    }

    // Test 7 — missing name
    @Test
    public void test07_searchAndStore_missingName() throws Exception {
        mockMvc.perform(get("/staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId")
                .param("storeId", "1"))
                .andExpect(status().isNotFound());
    }

    // Test 8 — missing storeId
    @Test
    public void test08_searchAndStore_missingStoreId() throws Exception {
        mockMvc.perform(get("/staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId")
                .param("name", "Mike"))
                .andExpect(status().isNotFound());
    }

    // =====================================================
    // GROUP 3 — PAGINATION
    // =====================================================

    // Test 9 — pagination store filter
    @Test
    public void test09_findByStore_pagination() throws Exception {
        mockMvc.perform(get("/staff/search/findByStore_StoreId")
                .param("storeId", "1")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    // Test 10 — pagination search + store
    @Test
    public void test10_searchAndStore_pagination() throws Exception {
        mockMvc.perform(get("/staff/search/findByFirstNameContainingIgnoreCaseAndStore_StoreId")
                .param("name", "Mike")
                .param("storeId", "1")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(1));
    }
}