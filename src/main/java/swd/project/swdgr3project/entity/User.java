package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a user in the system.
 * FINAL CORRECTED VERSION with JPA annotations and Lombok.
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;
    private String phone;
    private String address;
    private String avatarUrl;

    @Column(nullable = false)
    private String role; // ADMIN, MANAGER, SALES, CUSTOMER

    private boolean active = true;
    private String googleId; // For Google OAuth users

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // A user can have many orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Order> orders = new HashSet<>();

    // Constructors, Getters, and Setters are handled by Lombok's @Data annotation.

    // Helper methods
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
        order.setUser(null);
    }
}