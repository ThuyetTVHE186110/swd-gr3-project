package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.OrderDTO;
import java.util.List;

public interface OrderService {
    OrderDTO createOrderFromGuestCart(String sessionId, String customerName, String customerEmail,
                                      String customerPhone, String shippingAddress, String paymentMethod);

    OrderDTO createOrderFromCart(Long userId, String shippingAddress, String paymentMethod);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByCustomerId(Long customerId);

    // Sửa các phương thức dưới đây để dùng String
    List<OrderDTO> getOrdersByStatus(String status);
    List<OrderDTO> getOrdersByPaymentMethod(String paymentMethod);
    List<OrderDTO> getOrdersByPaymentStatus(String paymentStatus);
    OrderDTO getOrderByPaymentTransactionId(String transactionId);

    OrderDTO updateOrderStatus(Long orderId, String status);
    OrderDTO updatePaymentStatus(Long orderId, String paymentStatus, String transactionId);
    OrderDTO updateTrackingNumber(Long orderId, String trackingNumber);
    OrderDTO cancelOrder(Long orderId);

    double calculateShippingFee(Long orderId, String destinationAddress);
    String generatePaymentQRCode(Long orderId);
    boolean processPaymentCallback(String transactionId, String status, double amount);
}