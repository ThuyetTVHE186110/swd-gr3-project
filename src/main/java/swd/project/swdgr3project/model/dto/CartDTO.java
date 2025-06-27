package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.entity.Cart;
import swd.project.swdgr3project.entity.CartItem;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for transferring shopping cart data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private UserDTO user;
    private List<CartItemDTO> items;
    private BigDecimal total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long id;
        private ProductDTO product;
        private int quantity;
        // SỬA LỖI: Thêm trường productName vào DTO vì nó sẽ được lấy từ CartItem entity
        private String productName;
        private BigDecimal subtotal;

        public static CartItemDTO fromEntity(CartItem item) {
            if (item == null) return null;

            CartItemDTO dto = new CartItemDTO();
            dto.setId(item.getId());
            dto.setProduct(ProductDTO.fromEntity(item.getProduct()));
            dto.setQuantity(item.getQuantity());
            // SỬA LỖI: Lấy productName từ CartItem entity
            dto.setProductName(item.getProductName());
            dto.setSubtotal(item.getSubtotal());

            return dto;
        }
    }

    public static CartDTO fromEntity(Cart cart) {
        if (cart == null) return null;

        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUser(UserDTO.fromEntity(cart.getUser()));

        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(CartItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(Collections.emptyList());
        }

        dto.setTotal(cart.calculateTotal());

        return dto;
    }
}