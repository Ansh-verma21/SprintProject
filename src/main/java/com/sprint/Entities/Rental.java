package com.sprint.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rental")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long rentalId;
    
    @Column(name = "rental_date")
    private LocalDateTime rentalDate;
    
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "return_date", nullable = true)
    private LocalDateTime returnDate;
    
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
    
    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "rental")
    private List<Payment> payments;
}
