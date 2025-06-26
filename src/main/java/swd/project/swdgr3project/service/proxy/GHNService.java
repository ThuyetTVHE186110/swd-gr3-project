package swd.project.swdgr3project.service.proxy;

/**
 * Service interface for GHN shipping operations.
 * This service acts as a proxy to the GHN API.
 */
public interface GHNService {
    
    /**
     * Calculate shipping fee based on destination address.
     *
     * @param destinationAddress The destination address
     * @return The calculated shipping fee
     */
    double calculateShippingFee(String destinationAddress);
    
    /**
     * Calculate shipping fee based on source and destination addresses.
     *
     * @param sourceAddress The source address
     * @param destinationAddress The destination address
     * @param weight The package weight in grams
     * @return The calculated shipping fee
     */
    double calculateShippingFee(String sourceAddress, String destinationAddress, int weight);
    
    /**
     * Create a shipping order.
     *
     * @param orderCode The order code
     * @param senderName The sender name
     * @param senderPhone The sender phone
     * @param senderAddress The sender address
     * @param receiverName The receiver name
     * @param receiverPhone The receiver phone
     * @param receiverAddress The receiver address
     * @param weight The package weight in grams
     * @param cod The COD amount (if any)
     * @return The tracking code
     */
    String createShippingOrder(String orderCode, String senderName, String senderPhone, String senderAddress,
                              String receiverName, String receiverPhone, String receiverAddress,
                              int weight, double cod);
    
    /**
     * Get shipping order status.
     *
     * @param trackingCode The tracking code
     * @return The shipping status
     */
    String getShippingStatus(String trackingCode);
    
    /**
     * Cancel a shipping order.
     *
     * @param trackingCode The tracking code
     * @return true if the order was canceled successfully
     */
    boolean cancelShippingOrder(String trackingCode);
}