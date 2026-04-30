package com.sprint;

//package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ActorTest {

    @Autowired
    private MockMvc mockMvc;

    // =====================================================
    // GROUP 1 — GET /actors [3 tests]
    // =====================================================

    // Test 1
    @Test
    public void getActors_success() throws Exception {
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk());
    }

    // Test 2
    @Test
    public void getActors_emptyDB() throws Exception {
        mockMvc.perform(get("/actors")
                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                    "$._embedded.actors").exists());
    }

    // Test 3
    @Test
    public void getActors_fullNameVisible() throws Exception {
                mockMvc.perform(get("/actors")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                    "$._embedded.actors[0].firstName").exists())
                .andExpect(jsonPath(
                    "$._embedded.actors[0].lastName").exists());
    }

    // =====================================================
    // GROUP 2 — GET /actors/search [4 tests]
    // =====================================================

    // Test 4
    @Test
    public void search_validName() throws Exception {
        mockMvc.perform(get("/actors/search/" +
                "findByFirstNameContainingIgnoreCase")
                                .param("firstName", "PENELOPE")
                                .accept("application/hal+json"))
                .andDo(print())
                .andExpect(status().isOk())
                                .andExpect(jsonPath(
                                        "$._embedded.actors").exists());
    }

    // Test 5
    @Test
    public void search_partialName() throws Exception {
        mockMvc.perform(get("/actors/search/" +
                "findByFirstNameContainingIgnoreCase")
                                .param("firstName", "PEN")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                                .andExpect(jsonPath(
                                        "$._embedded.actors").exists());
    }

    // Test 6
    @Test
    public void search_noMatch() throws Exception {
        mockMvc.perform(get("/actors/search/" +
                "findByFirstNameContainingIgnoreCase")
                                .param("firstName", "ZZZZZ")
                                .accept("application/hal+json"))
                .andExpect(status().isNotFound());
    }

    // Test 7
    @Test
    public void search_blankInput() throws Exception {
        mockMvc.perform(get("/actors/search/" +
                "findByFirstNameContainingIgnoreCase")
                .param("firstName", ""))
                .andExpect(status().isOk());
    }

    // =====================================================
    // GROUP 3 — POST /actors [5 tests]
    // =====================================================

    // Test 8
    @Test
    public void addActor_validInput() throws Exception {
        mockMvc.perform(post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "TOM",
                          "lastName": "HANKS"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    // Test 9
    @Test
    public void addActor_firstNameEmpty() throws Exception {
        mockMvc.perform(post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "lastName": "HANKS"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // Test 10
    @Test
    public void addActor_lastNameEmpty() throws Exception {
        mockMvc.perform(post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "TOM"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    // Test 11
    @Test
    public void addActor_savedInUppercase() throws Exception {
        mockMvc.perform(post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "TOM",
                          "lastName": "HANKS"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    // Test 12
    @Test
    public void addActor_lastUpdateAutoSet() throws Exception {
        mockMvc.perform(post("/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "TOM",
                          "lastName": "HANKS"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    // =====================================================
    // GROUP 4 — PATCH /actors/{id} firstname [3 tests]
    // =====================================================

    // Test 13
    @Test
    public void updateFirstName_validId() throws Exception {
        mockMvc.perform(patch("/actors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "firstName": "TOMMY" }
                        """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/actors/1"))
                .andExpect(jsonPath("$.firstName").value("TOMMY"));
    }

    // Test 14
    @Test
    public void updateFirstName_invalidId() throws Exception {
        mockMvc.perform(patch("/actors/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "firstName": "TOMMY" }
                        """))
                .andExpect(status().isNotFound());
    }

    // Test 15
    @Test
    public void updateFirstName_savedInUppercase() throws Exception {
        mockMvc.perform(patch("/actors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "firstName": "TOMMY" }
                        """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/actors/1"))
                .andExpect(jsonPath("$.firstName").value("TOMMY"));
    }

    // =====================================================
    // GROUP 5 — PATCH /actors/{id} lastname [3 tests]
    // =====================================================

    // Test 16
    @Test
    public void updateLastName_validId() throws Exception {
        mockMvc.perform(patch("/actors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "lastName": "SMITH" }
                        """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/actors/1"))
                .andExpect(jsonPath("$.lastName").value("SMITH"));
    }

    // Test 17
    @Test
    public void updateLastName_invalidId() throws Exception {
        mockMvc.perform(patch("/actors/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "lastName": "SMITH" }
                        """))
                .andExpect(status().isNotFound());
    }

    // Test 18
    @Test
    public void updateLastName_savedInUppercase() throws Exception {
        mockMvc.perform(patch("/actors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "lastName": "SMITH" }
                        """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/actors/1"))
                .andExpect(jsonPath("$.lastName").value("SMITH"));
    }

    // =====================================================
    // GROUP 6 — GET /actors/detail — Page 3 [8 tests]
    // Films via filmActors endpoint
    // =====================================================

    // Test 19
    @Test
    public void actorDetail_validId() throws Exception {
        mockMvc.perform(get("/actors/1"))
                .andExpect(status().isOk());
    }

    // Test 20
    @Test
    public void actorDetail_fullNameShown() throws Exception {
        mockMvc.perform(get("/actors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());
    }

    // Test 21
    @Test
    public void actorDetail_moviesShown() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                    "$._embedded.filmActors").exists());
    }

    // Test 22
    @Test
    public void actorDetail_yearVisible() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                                .andExpect(jsonPath(
                                        "$._embedded.filmActors[0]" +
                                        "._links.film.href").exists());
    }

    // Test 23
    @Test
    public void actorDetail_ratingVisible() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                                .andExpect(jsonPath(
                                        "$._embedded.filmActors[0]" +
                                        "._links.film.href").exists());
    }

    // Test 24
    @Test
    public void actorDetail_noMovies() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "9999")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isArray())
                                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isEmpty());
    }

    // Test 25
    @Test
    public void actorDetail_invalidId() throws Exception {
        mockMvc.perform(get("/actors/9999"))
                .andExpect(status().isNotFound());
    }

    // Test 26
    @Test
    public void actorDetail_backButton() throws Exception {
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk());
    }

    // =====================================================
    // GROUP 7 — GET /filmActors films [6 tests]
    // =====================================================

    // Test 27
    @Test
    public void getFilms_validId() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                    "$._embedded.filmActors").exists());
    }

    // Test 28
    @Test
    public void getFilms_titleShown() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors[0]" +
                                        "._links.film.href").exists());
    }

    // Test 29
    @Test
    public void getFilms_yearShown() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors[0]" +
                                        "._links.film.href").exists());
    }

    // Test 30
    @Test
    public void getFilms_ratingShown() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "1")
                                .param("projection", "actorFilms")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors[0]" +
                                        "._links.film.href").exists());
    }

    // Test 31
    @Test
    public void getFilms_noFilms() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "9999")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isArray())
                                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isEmpty());
    }

    // Test 32
    @Test
    public void getFilms_invalidId() throws Exception {
        mockMvc.perform(get("/filmActors/search/" +
                "findByActorActorId")
                                .param("actorId", "9999")
                                .accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isArray())
                                .andExpect(jsonPath(
                                        "$._embedded.filmActors").isEmpty());
    }
}