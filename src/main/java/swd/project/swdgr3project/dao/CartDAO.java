package swd.project.swdgr3project.dao;

import swd.project.swdgr3project.entity.Cart;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.User;

import java.util.Optional;

/**
 * Data Access Object interface for Cart entity.
 * Provides methods to interact with cart data in the database.
 */
public interface CartDAO {
    
    /**
     * Save a new cart to the database.
     *
     * @param cart The cart to save
     * @return The saved cart with generated ID
     */
    Cart save(Cart cart);
    
    /**
     * Update an existing cart in the database.
     *
     * @param cart The cart to update
     * @return The updated cart
     */
    Cart update(Cart cart);
    
    /**
     * Find a cart by its ID.
     *
     * @param id The ID of the cart to find
     * @return An Optional containing the found cart, or empty if not found
     */
    Optional<Cart> findById(Long id);
    
    /**
     * Find a cart by user ID.
     *
     * @param userId The ID of the user whose cart to find
     * @return An Optional containing the found cart, or empty if not found
     */
    Optional<Cart> findByUserId(Long userId);
    
    /**
     * Find a cart by session ID (for guest carts).
     *
     * @param sessionId The session ID associated with the cart
     * @return An Optional containing the found cart, or empty if not found
     */
    Optional<Cart> findBySessionId(String sessionId);
    
    /**
     * Add an item to a cart. If the item already exists, update its quantity.
     *
     * @param cart The cart to add the item to
     * @param product The product to add
     * @param quantity The quantity to add
     * @return The updated cart
     */
    Cart addItem(Cart cart, Product product, int quantity);
    
    /**
     * Update the quantity of an item in the cart.
     *
     * @param cart The cart containing the item
     * @param productId The ID of the product to update
     * @param quantity The new quantity
     * @return The updated cart
     */
    Cart updateItemQuantity(Cart cart, Long productId, int quantity);
    
    /**
     * Remove an item from the cart.
     *
     * @param cart The cart containing the item
     * @param productId The ID of the product to remove
     * @return The updated cart
     */
    Cart removeItem(Cart cart, Long productId);
    
    /**
     * Clear all items from a cart.
     *
     * @param cart The cart to clear
     * @return The updated cart with no items
     */
    Cart clearItems(Cart cart);
    
    /**
     * Transfer items from a guest cart (session-based) to a user cart after login.
     *
     * @param sessionId The session ID of the guest cart
     * @param user The user who logged in
     * @return The updated user cart with merged items
     */
    Cart mergeGuestCartWithUserCart(String sessionId, User user);
    
    /**
     * Delete a cart from the database.
     *
     * @param id The ID of the cart to delete
     * @return true if the cart was deleted, false if it wasn't found
     */
    boolean delete(Long id);
}