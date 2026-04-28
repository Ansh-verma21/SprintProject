package com.sprint.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmCategoryId implements Serializable {
    @Column(name = "film_id")
    private Long filmId;

    @Column(name = "category_id")
    private Long categoryId;
}
