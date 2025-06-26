package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity class representing a shopping cart in the system.
 * FINAL CORRECTED VERSION using BigDecimal consistently.
 */
@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true)
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartItem> items = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Helper methods
    public CartItem addItem(Product product, int quantity) {
        if (product == null || product.getId() == null) {
            return null; // Do not process invalid products
        }

        // Check if item already exists
        for (CartItem item : items) {
            if (Objects.equals(item.getProduct().getId(), product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setUpdatedAt(LocalDateTime.now());
                return item;
            }
        }

        // Create new item if it doesn't exist
        CartItem newItem = new CartItem();
        newItem.setCart(this);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        // SỬA LỖI: Gán trực tiếp đối tượng BigDecimal.
        // product.getPrice() trả về BigDecimal, và newItem.setPrice() cũng yêu cầu BigDecimal.
        newItem.setPrice(product.getPrice());

        LocalDateTime now = LocalDateTime.now();
        newItem.setCreatedAt(now);
        newItem.setUpdatedAt(now);

        items.add(newItem);
        return newItem;
    }

    public CartItem updateItemQuantity(Long productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                    item.setUpdatedAt(LocalDateTime.now());
                } else {
                    // Remove item if quantity is 0 or less
                    items.remove(item);
                }
                return item;
            }
        }
        return null;
    }

    public boolean removeItem(Long productId) {
        return items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearItems() {
        items.clear();
    }

    /**
     * Calculates the total price of all items in the cart.
     * @return The total price as a BigDecimal.
     */
    public BigDecimal calculateTotal() { // SỬA LỖI: Kiểu trả về là BigDecimal
        if (items == null) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(CartItem::getSubtotal) // .map() tạo ra Stream<BigDecimal>
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Cộng dồn các BigDecimal
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}