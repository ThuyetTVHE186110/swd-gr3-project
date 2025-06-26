package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;

    // SỬA LỖI: Chuyển sang BigDecimal
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Calculates the subtotal for this order item.
     * @return The subtotal as a BigDecimal
     */
    public BigDecimal getSubtotal() {
        if (this.price == null) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}