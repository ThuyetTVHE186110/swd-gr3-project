package swd.project.swdgr3project.dao;

import swd.project.swdgr3project.entity.Order;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order save(Order order);
    Order update(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByCustomerId(Long customerId);

    // Sửa tất cả các phương thức dưới đây để dùng String
    List<Order> findByStatus(String status);
    List<Order> findByPaymentMethod(String paymentMethod);
    List<Order> findByPaymentStatus(String paymentStatus);
    Optional<Order> findByPaymentTransactionId(String transactionId);

    // Các phương thức update cũng dùng String
    boolean updateStatus(Long id, String status);
    boolean updatePaymentStatus(Long id, String paymentStatus);
    boolean updateTrackingNumber(Long id, String trackingNumber);
}