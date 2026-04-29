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
public class InventoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test01_getById_success() throws Exception {
        mockMvc.perform(get("/inventories/1")
                .param("projection", "inventoryProjection"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryId").exists())
                .andExpect(jsonPath("$.film.filmId").exists())
                .andExpect(jsonPath("$.store.storeId").exists());
    }

    @Test
    public void test02_getById_notFound() throws Exception {
        mockMvc.perform(get("/inventories/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test03_findByStoreId_success() throws Exception {
        mockMvc.perform(get("/inventories/search/findByStore_StoreId")
                .param("storeId", "1")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].store.storeId").value(1));
    }

    @Test
    public void test04_findByStoreId_emptyResult() throws Exception {
        mockMvc.perform(get("/inventories/search/findByStore_StoreId")
                .param("storeId", "999999")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void test05_getAll_pagination() throws Exception {
        mockMvc.perform(get("/inventories")
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2));
    }

    @Test
    public void test06_post_validInventory() throws Exception {
        mockMvc.perform(post("/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "film": "http://localhost:8000/films/1",
                          "store": "http://localhost:8000/stores/1"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    public void test07_post_missingFilm() throws Exception {
        mockMvc.perform(post("/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "store": "http://localhost:8000/stores/1"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test08_put_success() throws Exception {
        mockMvc.perform(put("/inventories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "film": "http://localhost:8000/films/1",
                          "store": "http://localhost:8000/stores/1"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test09_put_notFound() throws Exception {
        mockMvc.perform(put("/inventories/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void test10_getFilmByInventoryId_success() throws Exception {
        mockMvc.perform(get("/inventories/1/film"))
                .andExpect(status().isOk());
    }

    @Test
    public void test11_getStoreByInventoryId_success() throws Exception {
        mockMvc.perform(get("/inventories/1/store"))
                .andExpect(status().isOk());
    }
}
