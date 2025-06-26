package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.model.entity.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for transferring shopping cart data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private UserDTO user; // Null for guest carts
    private List<CartItemDTO> items;
    private BigDecimal total;
    
    /**
     * DTO class for transferring cart item data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long id;
        private ProductDTO product;
        private int quantity;
        private BigDecimal subtotal;
        
        /**
         * Converts a Cart.CartItem entity to a CartItemDTO
         * @param item The CartItem entity to convert
         * @return A CartItemDTO
         */
        public static CartItemDTO fromEntity(Cart.CartItem item) {
            if (item == null) return null;
            
            CartItemDTO dto = new CartItemDTO();
            dto.setId(item.getId());
            dto.setProduct(ProductDTO.fromEntity(item.getProduct()));
            dto.setQuantity(item.getQuantity());
            dto.setSubtotal(item.getSubtotal());
            
            return dto;
        }
    }
    
    /**
     * Converts a Cart entity to a CartDTO
     * @param cart The Cart entity to convert
     * @return A CartDTO
     */
    public static CartDTO fromEntity(Cart cart) {
        if (cart == null) return null;
        
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUser(UserDTO.fromEntity(cart.getUser()));
        
        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(CartItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        dto.setTotal(cart.getTotal());
        
        return dto;
    }
}