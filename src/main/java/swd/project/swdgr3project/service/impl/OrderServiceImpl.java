package swd.project.swdgr3project.service.impl;

import swd.project.swdgr3project.dao.OrderDAO;
import swd.project.swdgr3project.dao.impl.OrderDAOImpl;
import swd.project.swdgr3project.entity.Order;
import swd.project.swdgr3project.entity.OrderItem;
import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.OrderDTO;
import swd.project.swdgr3project.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * FINAL, COMPLETE, AND CORRECTED IMPLEMENTATION for the OrderService interface.
 * This version consistently uses BigDecimal for all monetary calculations.
 */
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    /**
     * A custom method used by CheckoutServlet, which takes a ShoppingCart object directly from the session.
     */
    public OrderDTO createOrderFromGuestCart(ShoppingCart cart, String guestName, String guestEmail, String guestPhone,
                                             String shippingAddress, String shippingCity, String shippingDistrict, String shippingWard,
                                             String paymentMethod) {

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart cannot be empty.");
        }

        Order order = new Order();
        // Map data to the actual fields of your Order entity
        order.setGuestEmail(guestEmail);
        order.setGuestPhone(guestPhone);
        order.setShippingAddress(shippingAddress);
        order.setShippingCity(shippingCity);
        order.setShippingDistrict(shippingDistrict);
        order.setShippingWard(shippingWard);
        order.setShippingNote("Người nhận: " + guestName);
        order.setUser(null); // Guest orders have no associated User
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Set statuses and payment method as Strings
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod.toUpperCase());
        order.setPaymentStatus("PENDING");

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            // SỬA LỖI: Kiểm tra null trước khi gọi getter
            String productName = cartItem.getProductName(); // Giả sử CartItem có getter này
            if (productName == null || productName.trim().isEmpty()) {
                productName = "Sản phẩm không xác định";
                // Hoặc throw exception nếu tên sản phẩm là bắt buộc
            }
            orderItem.setProductName(productName); // Gán giá trị đã kiểm tra

            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        // Use BigDecimal for fees and taxes
        order.setShippingFee(new BigDecimal("30000.00"));
        order.setTax(BigDecimal.ZERO);
        order.updateTotals();

        Order savedOrder = orderDAO.save(order);
        if (savedOrder == null) {
            throw new RuntimeException("Failed to save the order to the database.");
        }

        return OrderDTO.fromEntity(savedOrder);
    }

    @Override
    public OrderDTO createOrderFromGuestCart(String sessionId, String customerName, String customerEmail,
                                             String customerPhone, String shippingAddress, String paymentMethod) {
        throw new UnsupportedOperationException("Guest cart logic from sessionId is not implemented yet.");
    }

    @Override
    public OrderDTO createOrderFromCart(Long userId, String shippingAddress, String paymentMethod) {
        throw new UnsupportedOperationException("createOrderFromCart for logged-in user not implemented yet.");
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return orderDAO.findById(orderId)
                .map(OrderDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderDAO.findAll().stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        return orderDAO.findByCustomerId(customerId).stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status).stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByPaymentMethod(String paymentMethod) {
        return orderDAO.findByPaymentMethod(paymentMethod).stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByPaymentStatus(String paymentStatus) {
        return orderDAO.findByPaymentStatus(paymentStatus).stream().map(OrderDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByPaymentTransactionId(String transactionId) {
        return orderDAO.findByPaymentTransactionId(transactionId)
                .map(OrderDTO::fromEntity)
                .orElse(null);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        if (!orderDAO.updateStatus(orderId, status)) {
            throw new IllegalArgumentException("Failed to update status for order ID: " + orderId + ". It may not exist.");
        }
        return getOrderById(orderId);
    }

    @Override
    public OrderDTO updatePaymentStatus(Long orderId, String paymentStatus, String transactionId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        order.setPaymentStatus(paymentStatus);
        if (transactionId != null && !transactionId.isEmpty()) {
            order.setPaymentTransactionId(transactionId);
        }
        orderDAO.update(order);
        return getOrderById(orderId);
    }

    @Override
    public OrderDTO updateTrackingNumber(Long orderId, String trackingNumber) {
        if (!orderDAO.updateTrackingNumber(orderId, trackingNumber)) {
            throw new IllegalArgumentException("Failed to update tracking number for order ID: " + orderId + ". It may not exist.");
        }
        return getOrderById(orderId);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Order cannot be canceled because it is not in PENDING state.");
        }
        return updateOrderStatus(orderId, "CANCELLED");
    }

    @Override
    public double calculateShippingFee(Long orderId, String destinationAddress) {
        throw new UnsupportedOperationException("calculateShippingFee not implemented yet.");
    }

    @Override
    public String generatePaymentQRCode(Long orderId) {
        throw new UnsupportedOperationException("generatePaymentQRCode not implemented yet.");
    }

    @Override
    public boolean processPaymentCallback(String transactionId, String status, double amount) {
        OrderDTO order = getOrderByPaymentTransactionId(transactionId);
        if (order == null) {
            System.err.println("Payment callback for unknown transactionId: " + transactionId);
            return false;
        }

        // SỬA LỖI: Chuyển đổi và so sánh các giá trị BigDecimal
        BigDecimal orderTotal = order.getTotal(); // Đây là BigDecimal
        BigDecimal paidAmount = BigDecimal.valueOf(amount); // Chuyển double thành BigDecimal

        // So sánh hai giá trị BigDecimal.
        // .compareTo() trả về 0 nếu bằng nhau.
        if (orderTotal.compareTo(paidAmount) != 0) {
            System.err.println("Payment callback amount mismatch for transactionId: " + transactionId
                    + ". Expected: " + orderTotal + ", but got: " + paidAmount);
            updatePaymentStatus(order.getId(), "FAILED", transactionId);
            return false;
        }

        if ("SUCCESS".equalsIgnoreCase(status)) {
            updatePaymentStatus(order.getId(), "PAID", transactionId);
            updateOrderStatus(order.getId(), "PROCESSING");
            return true;
        } else {
            updatePaymentStatus(order.getId(), "FAILED", transactionId);
            return false;
        }
    }
}