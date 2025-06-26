package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// SỬA LỖI: Import đúng các lớp entity từ package '...entity'
import swd.project.swdgr3project.entity.Cart;
import swd.project.swdgr3project.entity.CartItem;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for transferring shopping cart data.
 * FINAL CORRECTED VERSION.
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
     * DTO for transferring cart item data.
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
         * Converts a CartItem entity to a CartItemDTO.
         * @param item The CartItem entity to convert.
         * @return A CartItemDTO.
         */
        // SỬA LỖI: Phương thức này phải nhận vào lớp CartItem riêng biệt, không phải Cart.CartItem
        public static CartItemDTO fromEntity(CartItem item) {
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
     * Converts a Cart entity to a CartDTO.
     * @param cart The Cart entity to convert.
     * @return A CartDTO.
     */
    // SỬA LỖI: Phương thức này phải nhận vào lớp Cart riêng biệt
    public static CartDTO fromEntity(Cart cart) {
        if (cart == null) return null;

        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUser(UserDTO.fromEntity(cart.getUser()));

        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(CartItemDTO::fromEntity) // Dùng method reference, giờ đã đúng
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        // SỬA LỖI: Gọi đúng phương thức tính tổng trong entity Cart
        dto.setTotal(cart.calculateTotal());

        return dto;
    }
}