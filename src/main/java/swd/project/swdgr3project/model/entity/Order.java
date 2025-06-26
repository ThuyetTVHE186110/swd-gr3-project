package swd.project.swdgr3project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing an order in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private User customer;
    private List<OrderItem> items;
    private BigDecimal subtotal; // Sum of all items
    private BigDecimal shippingFee; // Calculated via GHN API
    private BigDecimal total; // subtotal + shippingFee
    private String shippingAddress;
    private String recipientName;
    private String recipientPhone;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentTransactionId; // From PayOS or VnPay
    private String trackingNumber; // For shipping
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Entity class representing an item in an order
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private Long id;
        private Product product;
        private int quantity;
        private BigDecimal price; // Price at the time of purchase
        private BigDecimal subtotal; // price * quantity
    }
    
    /**
     * Enum representing the status of an order
     */
    public enum OrderStatus {
        PENDING, // Order created but not confirmed
        CONFIRMED, // Order confirmed by sales
        PROCESSING, // Order is being processed
        SHIPPED, // Order has been shipped
        DELIVERED, // Order has been delivered
        CANCELLED // Order has been cancelled
    }
    
    /**
     * Enum representing the payment method
     */
    public enum PaymentMethod {
        PAYOS,
        VNPAY
    }
    
    /**
     * Enum representing the payment status
     */
    public enum PaymentStatus {
        PENDING, // Payment not yet made
        COMPLETED, // Payment completed
        FAILED, // Payment failed
        REFUNDED // Payment refunded
    }
}