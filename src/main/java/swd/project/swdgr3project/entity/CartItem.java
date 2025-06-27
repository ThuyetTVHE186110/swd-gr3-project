package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing an item in a shopping cart.
 * FINAL CORRECTED VERSION: Uses BigDecimal for price consistently and includes productName.
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

    // SỬA LỖI: Thêm trường productName để lưu trữ tên sản phẩm
    // Thường thì khi thêm vào giỏ hàng, ta sẽ lưu lại tên sản phẩm tại thời điểm đó
    // để tránh trường hợp tên sản phẩm bị thay đổi sau này.
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Price at the time of adding to cart

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Lombok generates getters and setters.
    // Add custom setter for product to also set productName.
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.productName = product.getName(); // Tự động cập nhật productName
        } else {
            this.productName = null; // Hoặc giá trị mặc định khác nếu cần
        }
    }

    // SỬA LỖI: Đảm bảo getPrice() và setPrice() là đúng kiểu BigDecimal
    // Lombok @Data đã tự tạo ra các getter/setter này cho BigDecimal.

    /**
     * Calculates the subtotal for this cart item (price * quantity).
     * @return The subtotal as a BigDecimal.
     */
    public BigDecimal getSubtotal() {
        if (this.price == null) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}