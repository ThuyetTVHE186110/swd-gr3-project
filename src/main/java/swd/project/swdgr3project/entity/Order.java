package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing an order in the system.
 * FINAL, COMPLETE, AND CORRECTED VERSION using BigDecimal for all monetary values.
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    private String guestEmail;
    private String guestPhone;
    private String shippingAddress;
    private String shippingCity;
    private String shippingDistrict;
    private String shippingWard;
    private String shippingNote;

    // --- MONETARY FIELDS CORRECTED TO USE BIGDECIMAL ---
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingFee;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal tax;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    // --- OTHER FIELDS ---
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String paymentStatus;

    private String paymentTransactionId;
    private String trackingNumber;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderItem> items = new ArrayList<>();

    // Lombok's @Data annotation handles constructors, getters, and setters.

    // --- HELPER METHODS CORRECTED TO USE BIGDECIMAL ---

    /**
     * Adds an item to the order and sets the bidirectional relationship.
     */
    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Removes an item from the order and breaks the bidirectional relationship.
     */
    public void removeItem(OrderItem item) {
        if (items != null) {
            items.remove(item);
            item.setOrder(null);
        }
    }

    /**
     * Calculates the subtotal of all items in the order.
     * @return The subtotal as a BigDecimal.
     */
    public BigDecimal calculateSubtotal() {
        if (items == null) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(OrderItem::getSubtotal) // Assumes OrderItem.getSubtotal() returns BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total of the order (subtotal + shipping + tax).
     * @return The total as a BigDecimal.
     */
    public BigDecimal calculateTotal() {
        BigDecimal currentSubtotal = (this.subtotal != null) ? this.subtotal : calculateSubtotal();
        BigDecimal currentShippingFee = (this.shippingFee != null) ? this.shippingFee : BigDecimal.ZERO;
        BigDecimal currentTax = (this.tax != null) ? this.tax : BigDecimal.ZERO;

        return currentSubtotal.add(currentShippingFee).add(currentTax);
    }

    /**
     * Updates the order totals based on the current items, shipping fee, and tax.
     */
    public void updateTotals() {
        this.subtotal = calculateSubtotal();
        this.total = calculateTotal();
    }
}