package com.sprint.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.sprint.Entities.Film;
import com.sprint.Repository.FilmRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Component
@RepositoryEventHandler(Film.class)
public class FilmEventHandler {

    @Autowired
    private Validator validator;

    @Autowired
    private FilmRepository filmRepository;

    @HandleBeforeCreate
    public void validateBeforeCreate(Film film) {
        // For POST, validate all fields strictly
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @HandleBeforeSave
    public void validateBeforeSave(Film film) {
        // For PUT/PATCH, load existing film and validate only the changed fields
        if (film.getFilmId() != null) {
            Film existingFilm = filmRepository.findById(film.getFilmId()).orElse(null);
            if (existingFilm != null) {
                // Merge: keep existing values for null fields (partial update support)
                if (film.getTitle() == null) {
                    film.setTitle(existingFilm.getTitle());
                }
                if (film.getReleaseYear() == null) {
                    film.setReleaseYear(existingFilm.getReleaseYear());
                }
                if (film.getLanguage() == null) {
                    film.setLanguage(existingFilm.getLanguage());
                }
                if (film.getRentalDuration() == null) {
                    film.setRentalDuration(existingFilm.getRentalDuration());
                }
                if (film.getRentalRate() == null) {
                    film.setRentalRate(existingFilm.getRentalRate());
                }
                if (film.getReplacementCost() == null) {
                    film.setReplacementCost(existingFilm.getReplacementCost());
                }
            }
        }

        // Now validate the merged entity
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
