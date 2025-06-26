package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO class for transferring order data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private UserDTO customer;
    private List<OrderItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private String shippingAddress;
    private String recipientName;
    private String recipientPhone;
    private Order.OrderStatus status;
    private Order.PaymentMethod paymentMethod;
    private Order.PaymentStatus paymentStatus;
    private String trackingNumber;
    private LocalDateTime createdAt;
    
    /**
     * DTO class for transferring order item data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private ProductDTO product;
        private int quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
        
        /**
         * Converts an Order.OrderItem entity to an OrderItemDTO
         * @param item The OrderItem entity to convert
         * @return An OrderItemDTO
         */
        public static OrderItemDTO fromEntity(Order.OrderItem item) {
            if (item == null) return null;
            
            OrderItemDTO dto = new OrderItemDTO();
            dto.setId(item.getId());
            dto.setProduct(ProductDTO.fromEntity(item.getProduct()));
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            dto.setSubtotal(item.getSubtotal());
            
            return dto;
        }
    }
    
    /**
     * Converts an Order entity to an OrderDTO
     * @param order The Order entity to convert
     * @return An OrderDTO
     */
    public static OrderDTO fromEntity(Order order) {
        if (order == null) return null;
        
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomer(UserDTO.fromEntity(order.getCustomer()));
        
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(OrderItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingFee(order.getShippingFee());
        dto.setTotal(order.getTotal());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setRecipientName(order.getRecipientName());
        dto.setRecipientPhone(order.getRecipientPhone());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCreatedAt(order.getCreatedAt());
        
        return dto;
    }
}