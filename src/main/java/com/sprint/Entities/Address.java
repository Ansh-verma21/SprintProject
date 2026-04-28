package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    
    @Column(name = "address", length = 50)
    private String address;
    
    @Column(name = "address2", length = 50, nullable = true)
    private String address2;
    
    @Column(name = "district", length = 20)
    private String district;
    
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    
    @Column(name = "postal_code", length = 10, nullable = true)
    private String postalCode;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "address")
    private List<Staff> staffList;

    @OneToMany(mappedBy = "address")
    private List<Customer> customers;
}
