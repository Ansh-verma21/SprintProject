package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Entity
@Table(name = "film_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmCategory {
    
    @EmbeddedId
    private FilmCategoryId id;
    
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    @MapsId("filmId")
    private Film film;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @MapsId("categoryId")
    private Category category;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;
}
