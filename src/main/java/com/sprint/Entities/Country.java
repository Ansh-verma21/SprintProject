package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long countryId;
    
    @Column(name = "country", length = 50)
    private String country;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "country")
    private List<City> cities;
}
