
package com.sprint;

import com.sprint.Entities.Film;
import com.sprint.Repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    // =========================
    // findByReleaseYear
    // =========================

    @Test
    public void testFindByReleaseYear_success() {
        Page<Film> page = filmRepository.findByReleaseYear("2006", PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByReleaseYear_noResults() {
        Page<Film> page = filmRepository.findByReleaseYear("1900", PageRequest.of(0, 5));
        assertTrue(page.isEmpty());
    }

    @Test
    public void testFindByReleaseYear_pagination() {
        Page<Film> page = filmRepository.findByReleaseYear("2006", PageRequest.of(0, 2));
        assertTrue(page.getContent().size() <= 2);
    }

    // =========================
    // findByTitleContainingIgnoreCase
    // =========================

    @Test
    public void testFindByTitle_success() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCase("ACADEMY", PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByTitle_caseInsensitive() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCase("academy", PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByTitle_noResults() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCase("NoSuchFilm", PageRequest.of(0, 5));
        assertTrue(page.isEmpty());
    }

    // =========================
    // findByTitleAndReleaseYear
    // =========================

    @Test
    public void testFindByTitleAndYear_success() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCaseAndReleaseYear(
                "ACADEMY", "2006", PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByTitleAndYear_noResults() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCaseAndReleaseYear(
                "ACADEMY", "1900", PageRequest.of(0, 5));
        assertTrue(page.isEmpty());
    }

    @Test
    public void testFindByTitleAndYear_partialMatch() {
        Page<Film> page = filmRepository.findByTitleContainingIgnoreCaseAndReleaseYear(
                "ACA", "2006", PageRequest.of(0, 5));
        assertFalse(page.isEmpty());
    }
}

