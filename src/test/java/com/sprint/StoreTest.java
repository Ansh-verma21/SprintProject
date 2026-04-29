package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test01_getById_success() throws Exception {
        mockMvc.perform(get("/stores/1")
                .param("projection", "storeProjection"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").exists())
                .andExpect(jsonPath("$.address.addressId").exists())
                .andExpect(jsonPath("$.address.city.city").exists());
    }

    @Test
    public void test02_getById_notFound() throws Exception {
        mockMvc.perform(get("/stores/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test03_findByCity_success() throws Exception {
        mockMvc.perform(get("/stores/search/findByAddress_City_CityIgnoreCase")
                .param("city", "Lethbridge")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].address.city.city").value("Lethbridge"));
    }

    @Test
    public void test04_findByCity_caseInsensitive() throws Exception {
        mockMvc.perform(get("/stores/search/findByAddress_City_CityIgnoreCase")
                .param("city", "leTHbRIdGE")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void test05_findByCity_emptyResult() throws Exception {
        mockMvc.perform(get("/stores/search/findByAddress_City_CityIgnoreCase")
                .param("city", "NoSuchCity")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void test06_post_validStore() throws Exception {
        mockMvc.perform(post("/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "managerStaff": "http://localhost:8000/staff/1",
                          "address": "http://localhost:8000/addresses/1"
                        }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    public void test07_post_missingManagerStaff() throws Exception {
        mockMvc.perform(post("/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "address": "http://localhost:8000/addresses/1"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test08_put_success() throws Exception {
        mockMvc.perform(put("/stores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "managerStaff": "http://localhost:8000/staff/1",
                          "address": "http://localhost:8000/addresses/1"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test09_put_notFound() throws Exception {
        mockMvc.perform(put("/stores/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void test10_getInventoriesByStoreId_success() throws Exception {
        mockMvc.perform(get("/stores/1/inventories"))
                .andExpect(status().isOk());
    }

    @Test
    public void test11_getCustomersByStoreId_success() throws Exception {
        mockMvc.perform(get("/stores/1/customers"))
                .andExpect(status().isOk());
    }
}
