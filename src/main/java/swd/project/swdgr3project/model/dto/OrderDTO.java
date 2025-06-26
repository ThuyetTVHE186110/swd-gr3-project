package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.entity.Order;
import swd.project.swdgr3project.entity.OrderItem;

import java.math.BigDecimal; // Import BigDecimal
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * DTO class for transferring order data.
 * FINAL CORRECTED VERSION using BigDecimal for all monetary values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private String guestEmail;
    private String recipientName;
    private String recipientPhone;
    private List<OrderItemDTO> items;

    // --- FIELDS CORRECTED TO USE BIGDECIMAL ---
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal total;

    private String shippingAddress;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String trackingNumber;
    private LocalDateTime createdAt;

    // private UserDTO customer; // You can uncomment this if you have a UserDTO

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private String productName;
        private int quantity;

        // --- FIELDS CORRECTED TO USE BIGDECIMAL ---
        private BigDecimal price;
        private BigDecimal subtotal;

        public static OrderItemDTO fromEntity(OrderItem item) {
            if (item == null) return null;

            OrderItemDTO dto = new OrderItemDTO();
            dto.setId(item.getId());
            dto.setProductName(item.getProductName());
            dto.setQuantity(item.getQuantity());

            // NO ERROR: item.getPrice() now returns BigDecimal
            dto.setPrice(item.getPrice());
            // NO ERROR: item.getSubtotal() now returns BigDecimal
            dto.setSubtotal(item.getSubtotal());

            return dto;
        }
    }

    public static OrderDTO fromEntity(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setGuestEmail(order.getGuestEmail());
        dto.setRecipientPhone(order.getGuestPhone());

        if (order.getShippingNote() != null && order.getShippingNote().startsWith("Người nhận: ")) {
            dto.setRecipientName(order.getShippingNote().substring("Người nhận: ".length()));
        }

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(OrderItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        // --- ASSIGNMENT CORRECTED TO USE BIGDECIMAL ---
        // NO ERROR: order.getSubtotal() now returns BigDecimal
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingFee(order.getShippingFee());
        dto.setTotal(order.getTotal());

        dto.setShippingAddress(
                String.join(", ",
                        order.getShippingAddress(),
                        order.getShippingWard(),
                        order.getShippingDistrict(),
                        order.getShippingCity())
        );
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCreatedAt(order.getCreatedAt());

        return dto;
    }
}