
package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

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

    // =========================
    // POST VALIDATION - TITLE CONSTRAINTS
    // =========================

    @Test
    public void test15_postFilm_blankTitle() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "",
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

    @Test
    public void test16_postFilm_titleExceedsLength() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "%s",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """.formatted("A".repeat(256))))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // POST VALIDATION - RENTAL DURATION CONSTRAINTS
    // =========================

    @Test
    public void test17_postFilm_missingRentalDuration() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test18_postFilm_rentalDurationTooLow() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 0,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test19_postFilm_rentalDurationExceedsMax() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 366,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // POST VALIDATION - RENTAL RATE CONSTRAINTS
    // =========================

    @Test
    public void test20_postFilm_missingRentalRate() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test21_postFilm_rentalRateExceedsMax() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 999.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test22_postFilm_rentalRateInvalidDecimalPlaces() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.999,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // POST VALIDATION - REPLACEMENT COST CONSTRAINTS
    // =========================

    @Test
    public void test23_postFilm_missingReplacementCost() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test24_postFilm_replacementCostExceedsMax() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 9999.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test25_postFilm_replacementCostInvalidDecimalPlaces() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 19.999,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // POST VALIDATION - LENGTH CONSTRAINTS
    // =========================

    @Test
    public void test26_postFilm_lengthTooLow() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 0,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test27_postFilm_lengthExceedsMax() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 10000,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // POST VALIDATION - RELEASE YEAR CONSTRAINTS
    // =========================

    @Test
    public void test28_postFilm_invalidReleaseYearFormat() throws Exception {
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "TEST FILM",
                          "releaseYear": "06",
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
    // POST VALIDATION - SPECIAL FEATURES CONSTRAINTS
    // =========================

    @Test
    public void test29_postFilm_specialFeaturesExceedsLength() throws Exception {
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
                          "rating": "PG",
                          "specialFeatures": "%s"
                        }
                        """.formatted("A".repeat(101))))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // PUT VALIDATION - TITLE CONSTRAINTS
    // =========================

    @Test
    public void test30_putFilm_success() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "description": "Updated description",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 120,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test31_putFilm_blankTitle() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "",
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

    @Test
    public void test32_putFilm_invalidRating() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
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
    public void test33_putFilm_rentalDurationTooLow() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 0,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test34_putFilm_rentalRateExceedsMax() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 999.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test35_putFilm_replacementCostExceedsMax() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 9999.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test36_putFilm_lengthExceedsMax() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "releaseYear": "2006",
                          "language": "http://localhost:8000/languages/1",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "length": 10000,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test37_putFilm_missingLanguage() throws Exception {
        mockMvc.perform(put("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "UPDATED FILM",
                          "releaseYear": "2006",
                          "rentalDuration": 5,
                          "rentalRate": 2.99,
                          "replacementCost": 19.99,
                          "rating": "PG"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // =========================
    // PATCH - PARTIAL UPDATES (ONLY REQUIRED FIELDS NEEDED)
    // =========================

    @Test
    public void test38_patchFilm_updateTitle_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "PATCHED TITLE"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test39_patchFilm_updateRating_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "rating": "R"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test40_patchFilm_updateDescription_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "description": "Patched description"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test41_patchFilm_updateRentalRate_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "rentalRate": 3.99
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test42_patchFilm_updateLength_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "length": 150
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test43_patchFilm_updateMultipleFields_success() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": "NEW TITLE",
                          "rating": "NC-17",
                          "length": 200
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test44_patchFilm_invalidRating_fails() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "rating": "INVALID"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test45_patchFilm_rentalRateTooHigh_fails() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "rentalRate": 999.99
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test46_patchFilm_lengthTooHigh_fails() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "length": 10000
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test47_patchFilm_blankTitle_fails() throws Exception {
        mockMvc.perform(patch("/films/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title": ""
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }
}

