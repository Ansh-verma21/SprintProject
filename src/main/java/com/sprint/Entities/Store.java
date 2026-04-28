package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;
    
    @ManyToOne
    @JoinColumn(name = "manager_staff_id", nullable = false)
    private Staff managerStaff;
    
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "store")
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "store")
    private List<Staff> staffList;

    @OneToMany(mappedBy = "store")
    private List<Customer> customers;
}
