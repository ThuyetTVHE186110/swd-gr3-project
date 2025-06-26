package swd.project.swdgr3project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a shopping cart in the system.
 * A cart can be associated with a registered user or a guest session.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private User user; // Null for guest carts
    private String sessionId; // For guest carts
    private List<CartItem> items;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Entity class representing an item in a shopping cart
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private Long id;
        private Product product;
        private int quantity;
        private BigDecimal subtotal; // price * quantity
    }
}