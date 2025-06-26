package swd.project.swdgr3project.service.proxy;

import java.util.Map;

/**
 * Service interface for payment processing operations.
 * This service acts as a proxy to payment gateways like PayOS and VnPay.
 */
public interface PaymentService {
    
    /**
     * Generate a payment QR code for an order.
     *
     * @param orderId The order ID
     * @param orderNumber The order number
     * @param amount The payment amount
     * @param description The payment description
     * @return The URL of the generated QR code
     */
    String generatePaymentQRCode(Long orderId, String orderNumber, double amount, String description);
    
    /**
     * Create a payment link for an order.
     *
     * @param orderId The order ID
     * @param orderNumber The order number
     * @param amount The payment amount
     * @param description The payment description
     * @param returnUrl The URL to redirect to after payment
     * @return The payment link URL
     */
    String createPaymentLink(Long orderId, String orderNumber, double amount, String description, String returnUrl);
    
    /**
     * Process a payment callback from the payment gateway.
     *
     * @param callbackParams The callback parameters from the payment gateway
     * @return true if the callback was processed successfully
     */
    boolean processPaymentCallback(Map<String, String> callbackParams);
    
    /**
     * Check the status of a payment.
     *
     * @param transactionId The payment transaction ID
     * @return The payment status (e.g., "success", "pending", "failed")
     */
    String checkPaymentStatus(String transactionId);
    
    /**
     * Refund a payment.
     *
     * @param transactionId The payment transaction ID
     * @param amount The amount to refund (can be partial)
     * @param reason The reason for the refund
     * @return true if the refund was processed successfully
     */
    boolean refundPayment(String transactionId, double amount, String reason);
}