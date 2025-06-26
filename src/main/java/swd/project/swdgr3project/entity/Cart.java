package swd.project.swdgr3project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a shopping cart in the system.
 */
public class Cart {
    private Long id;
    private User user; // null for guest carts
    private String sessionId; // for guest carts
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Cart() {
    }

    public Cart(Long id, User user, String sessionId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    /**
     * Adds a product to the cart or updates its quantity if it already exists.
     * 
     * @param product The product to add
     * @param quantity The quantity to add
     * @return The cart item that was added or updated
     */
    public CartItem addItem(Product product, int quantity) {
        // Check if the product already exists in the cart
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                // Update quantity
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            }
        }
        
        // Create a new cart item
        CartItem newItem = new CartItem();
        newItem.setCart(this);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setPrice(product.getPrice());
        newItem.setCreatedAt(LocalDateTime.now());
        newItem.setUpdatedAt(LocalDateTime.now());
        
        // Add to the cart
        items.add(newItem);
        
        return newItem;
    }

    /**
     * Updates the quantity of a product in the cart.
     * 
     * @param productId The ID of the product to update
     * @param quantity The new quantity
     * @return The updated cart item, or null if the product was not found in the cart
     */
    public CartItem updateItemQuantity(Long productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                item.setUpdatedAt(LocalDateTime.now());
                return item;
            }
        }
        return null;
    }

    /**
     * Removes a product from the cart.
     * 
     * @param productId The ID of the product to remove
     * @return true if the product was removed, false if it was not found in the cart
     */
    public boolean removeItem(Long productId) {
        return items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    /**
     * Clears all items from the cart.
     */
    public void clearItems() {
        items.clear();
    }

    /**
     * Calculates the total price of all items in the cart.
     * 
     * @return The total price
     */
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Checks if the cart is empty.
     * 
     * @return true if the cart is empty, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the total number of items in the cart.
     * 
     * @return The total number of items
     */
    public int getItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", sessionId='" + sessionId + '\'' +
                ", itemCount=" + getItemCount() +
                ", total=" + calculateTotal() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}