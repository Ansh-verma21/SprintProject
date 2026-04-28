package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long cityId;
    
    @Column(name = "city", length = 50)
    private String city;
    
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "city")
    private List<Address> addresses;
}
