package swd.project.swdgr3project.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

/**
 * Entity class representing an item in a shopping cart.
 * FINAL CORRECTED VERSION: Uses BigDecimal for price consistently.
 */
@Data
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    // SỬA LỖI: Chuyển sang BigDecimal để nhất quán
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Lombok sẽ tự động tạo các phương thức:
    // public BigDecimal getPrice() { ... }
    // public void setPrice(BigDecimal price) { ... }

    /**
     * Calculates the subtotal for this cart item (price * quantity).
     * @return The subtotal as a BigDecimal
     */
    public BigDecimal getSubtotal() {
        if (this.price == null) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}