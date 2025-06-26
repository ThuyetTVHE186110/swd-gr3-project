package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.model.entity.Order;

import java.util.List;

/**
 * Service interface for order-related operations.
 */
public interface OrderService {
    
    /**
     * Create a new order from a user's cart.
     *
     * @param userId The user ID
     * @param shippingAddress The shipping address
     * @param paymentMethod The payment method
     * @return The created order DTO
     * @throws IllegalArgumentException if user not found or cart is empty
     */
    OrderDTO createOrderFromCart(Long userId, String shippingAddress, String paymentMethod);
    
    /**
     * Create a new order from a guest's cart.
     *
     * @param sessionId The session ID
     * @param customerName The customer name
     * @param customerEmail The customer email
     * @param customerPhone The customer phone
     * @param shippingAddress The shipping address
     * @param paymentMethod The payment method
     * @return The created order DTO
     * @throws IllegalArgumentException if cart is empty
     */
    OrderDTO createOrderFromGuestCart(String sessionId, String customerName, String customerEmail, 
                                     String customerPhone, String shippingAddress, String paymentMethod);
    
    /**
     * Get an order by ID.
     *
     * @param orderId The order ID
     * @return The order DTO
     * @throws IllegalArgumentException if order not found
     */
    OrderDTO getOrderById(Long orderId);
    
    /**
     * Get all orders.
     *
     * @return List of all order DTOs
     */
    List<OrderDTO> getAllOrders();
    
    /**
     * Get orders by customer ID.
     *
     * @param customerId The customer ID
     * @return List of order DTOs for the customer
     */
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    
    /**
     * Get orders by status.
     *
     * @param status The order status
     * @return List of order DTOs with the specified status
     */
    List<OrderDTO> getOrdersByStatus(Order.Status status);
    
    /**
     * Get orders by payment method.
     *
     * @param paymentMethod The payment method
     * @return List of order DTOs with the specified payment method
     */
    List<OrderDTO> getOrdersByPaymentMethod(String paymentMethod);
    
    /**
     * Get orders by payment status.
     *
     * @param paymentStatus The payment status
     * @return List of order DTOs with the specified payment status
     */
    List<OrderDTO> getOrdersByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    /**
     * Get an order by payment transaction ID.
     *
     * @param transactionId The payment transaction ID
     * @return The order DTO or null if not found
     */
    OrderDTO getOrderByPaymentTransactionId(String transactionId);
    
    /**
     * Update an order's status.
     *
     * @param orderId The order ID
     * @param status The new status
     * @return The updated order DTO
     * @throws IllegalArgumentException if order not found
     */
    OrderDTO updateOrderStatus(Long orderId, Order.Status status);
    
    /**
     * Update an order's payment status.
     *
     * @param orderId The order ID
     * @param paymentStatus The new payment status
     * @param transactionId The payment transaction ID (optional)
     * @return The updated order DTO
     * @throws IllegalArgumentException if order not found
     */
    OrderDTO updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus, String transactionId);
    
    /**
     * Update an order's tracking number.
     *
     * @param orderId The order ID
     * @param trackingNumber The tracking number
     * @return The updated order DTO
     * @throws IllegalArgumentException if order not found
     */
    OrderDTO updateTrackingNumber(Long orderId, String trackingNumber);
    
    /**
     * Cancel an order.
     *
     * @param orderId The order ID
     * @return The updated order DTO
     * @throws IllegalArgumentException if order not found or cannot be canceled
     */
    OrderDTO cancelOrder(Long orderId);
    
    /**
     * Calculate shipping fee for an order.
     *
     * @param orderId The order ID
     * @param destinationAddress The destination address
     * @return The calculated shipping fee
     * @throws IllegalArgumentException if order not found
     */
    double calculateShippingFee(Long orderId, String destinationAddress);
    
    /**
     * Generate payment QR code for an order.
     *
     * @param orderId The order ID
     * @return The payment QR code URL
     * @throws IllegalArgumentException if order not found
     */
    String generatePaymentQRCode(Long orderId);
    
    /**
     * Process payment callback from payment provider.
     *
     * @param transactionId The payment transaction ID
     * @param status The payment status
     * @param amount The payment amount
     * @return true if the payment was processed successfully
     */
    boolean processPaymentCallback(String transactionId, String status, double amount);
}