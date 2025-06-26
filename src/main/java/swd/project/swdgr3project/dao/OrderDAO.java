package swd.project.swdgr3project.dao;

import swd.project.swdgr3project.model.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Order entities.
 */
public interface OrderDAO {
    
    /**
     * Save a new order to the database.
     *
     * @param order The order to save
     * @return The saved order with generated ID
     */
    Order save(Order order);
    
    /**
     * Update an existing order in the database.
     *
     * @param order The order to update
     * @return The updated order
     */
    Order update(Order order);
    
    /**
     * Find an order by its ID.
     *
     * @param id The ID of the order to find
     * @return An Optional containing the found order, or empty if not found
     */
    Optional<Order> findById(Long id);
    
    /**
     * Find all orders in the database.
     *
     * @return A list of all orders
     */
    List<Order> findAll();
    
    /**
     * Find all orders for a specific customer.
     *
     * @param customerId The ID of the customer
     * @return A list of orders for the customer
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * Find orders by their status.
     *
     * @param status The order status to filter by
     * @return A list of orders with the specified status
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    /**
     * Find orders by payment method.
     *
     * @param paymentMethod The payment method to filter by
     * @return A list of orders with the specified payment method
     */
    List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod);
    
    /**
     * Find orders by payment status.
     *
     * @param paymentStatus The payment status to filter by
     * @return A list of orders with the specified payment status
     */
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    /**
     * Find an order by its payment transaction ID.
     *
     * @param transactionId The payment transaction ID
     * @return An Optional containing the found order, or empty if not found
     */
    Optional<Order> findByPaymentTransactionId(String transactionId);
    
    /**
     * Delete an order by its ID.
     *
     * @param id The ID of the order to delete
     * @return true if the order was deleted, false if it wasn't found
     */
    boolean delete(Long id);
    
    /**
     * Update the status of an order.
     *
     * @param id The ID of the order to update
     * @param status The new status
     * @return true if the order was updated, false if it wasn't found
     */
    boolean updateStatus(Long id, Order.OrderStatus status);
    
    /**
     * Update the payment status of an order.
     *
     * @param id The ID of the order to update
     * @param paymentStatus The new payment status
     * @return true if the order was updated, false if it wasn't found
     */
    boolean updatePaymentStatus(Long id, Order.PaymentStatus paymentStatus);
    
    /**
     * Update the tracking number of an order.
     *
     * @param id The ID of the order to update
     * @param trackingNumber The new tracking number
     * @return true if the order was updated, false if it wasn't found
     */
    boolean updateTrackingNumber(Long id, String trackingNumber);
}