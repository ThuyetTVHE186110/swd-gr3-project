package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.CartDTO;
import swd.project.swdgr3project.model.dto.CartDTO.CartItemDTO;

import java.util.List;

/**
 * Service interface for shopping cart operations.
 */
public interface CartService {
    
    /**
     * Create a new cart for a user.
     *
     * @param userId The user ID
     * @return The created cart DTO
     */
    CartDTO createCartForUser(Long userId);
    
    /**
     * Create a new cart for a guest session.
     *
     * @param sessionId The session ID
     * @return The created cart DTO
     */
    CartDTO createCartForGuest(String sessionId);
    
    /**
     * Get a cart by ID.
     *
     * @param cartId The cart ID
     * @return The cart DTO
     * @throws IllegalArgumentException if cart not found
     */
    CartDTO getCartById(Long cartId);
    
    /**
     * Get a user's cart.
     *
     * @param userId The user ID
     * @return The cart DTO or null if no cart exists
     */
    CartDTO getCartByUserId(Long userId);
    
    /**
     * Get a guest's cart by session ID.
     *
     * @param sessionId The session ID
     * @return The cart DTO or null if no cart exists
     */
    CartDTO getCartBySessionId(String sessionId);
    
    /**
     * Add an item to a cart.
     *
     * @param cartId The cart ID
     * @param productId The product ID
     * @param quantity The quantity to add
     * @return The updated cart DTO
     * @throws IllegalArgumentException if cart or product not found
     */
    CartDTO addItemToCart(Long cartId, Long productId, int quantity);
    
    /**
     * Add an item to a user's cart.
     *
     * @param userId The user ID
     * @param productId The product ID
     * @param quantity The quantity to add
     * @return The updated cart DTO
     * @throws IllegalArgumentException if user or product not found
     */
    CartDTO addItemToUserCart(Long userId, Long productId, int quantity);
    
    /**
     * Add an item to a guest's cart.
     *
     * @param sessionId The session ID
     * @param productId The product ID
     * @param quantity The quantity to add
     * @return The updated cart DTO
     * @throws IllegalArgumentException if product not found
     */
    CartDTO addItemToGuestCart(String sessionId, Long productId, int quantity);
    
    /**
     * Update the quantity of an item in a cart.
     *
     * @param cartId The cart ID
     * @param itemId The cart item ID
     * @param quantity The new quantity
     * @return The updated cart DTO
     * @throws IllegalArgumentException if cart or item not found
     */
    CartDTO updateCartItemQuantity(Long cartId, Long itemId, int quantity);
    
    /**
     * Remove an item from a cart.
     *
     * @param cartId The cart ID
     * @param itemId The cart item ID
     * @return The updated cart DTO
     * @throws IllegalArgumentException if cart or item not found
     */
    CartDTO removeItemFromCart(Long cartId, Long itemId);
    
    /**
     * Clear all items from a cart.
     *
     * @param cartId The cart ID
     * @return The updated cart DTO
     * @throws IllegalArgumentException if cart not found
     */
    CartDTO clearCart(Long cartId);
    
    /**
     * Merge a guest cart with a user cart after login.
     *
     * @param userId The user ID
     * @param sessionId The session ID
     * @return The merged cart DTO
     * @throws IllegalArgumentException if user not found
     */
    CartDTO mergeGuestCartWithUserCart(Long userId, String sessionId);
    
    /**
     * Delete a cart.
     *
     * @param cartId The cart ID
     * @return true if the cart was deleted
     */
    boolean deleteCart(Long cartId);
    
    /**
     * Get all items in a cart.
     *
     * @param cartId The cart ID
     * @return List of cart item DTOs
     * @throws IllegalArgumentException if cart not found
     */
    List<CartItemDTO> getCartItems(Long cartId);
    
    /**
     * Calculate the total price of a cart.
     *
     * @param cartId The cart ID
     * @return The total price
     * @throws IllegalArgumentException if cart not found
     */
    double calculateCartTotal(Long cartId);
}