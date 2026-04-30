
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
public class FilmTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // PAGE 2 (LIST)
    // =========================

    @Test
    public void test01_getFilms_success() throws Exception {
        mockMvc.perform(get("/films?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // =========================
    // PAGE 3 (DETAIL)
    // =========================

    @Test
    public void test02_getFilmDetails_success() throws Exception {
        mockMvc.perform(get("/films/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.rating").exists());
    }

    // =========================
    // RELATIONS
    // =========================

    @Test
    public void test03_getActors_success() throws Exception {
        mockMvc.perform(get("/films/1/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void test04_getCategories_success() throws Exception {
        mockMvc.perform(get("/films/1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void test05_getLanguage_success() throws Exception {
        mockMvc.perform(get("/films/1/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    // =========================
    // SEARCH
    // =========================

    @Test
    public void test06_searchByTitle_success() throws Exception {
        mockMvc.perform(get("/films/search/byTitle")
                .param("title", "academy")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void test07_filterByReleaseYear_success() throws Exception {
        mockMvc.perform(get("/films/search/byReleaseYear")
                .param("releaseYear", "2006")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void test08_search_noResults() throws Exception {
        mockMvc.perform(get("/films/search/byTitle")
                .param("title", "NoSuchFilm"))
                                .andExpect(status().isNotFound());
    }

    // =========================
    // POST (VALID + INVALID)
    // =========================

    @Test
    public void test09_postFilm_success() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "description": "Testing",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 120,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    public void test10_postFilm_invalidRating() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "INVALID"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test11_postFilm_missingTitle() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // EDGE CASES
    // =========================

    @Test
    public void test12_getFilm_invalidId() throws Exception {
        mockMvc.perform(get("/films/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test13_pagination_limit() throws Exception {
        mockMvc.perform(get("/films?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    // =========================
    // INTEGRATION FLOW
    // =========================

    @Test
    public void test14_createAndFetchFilm_flow() throws Exception {

        var result = mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "FLOW TEST FILM",
                          "description": "Integration test",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 100,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FLOW TEST FILM"));
    }
}

