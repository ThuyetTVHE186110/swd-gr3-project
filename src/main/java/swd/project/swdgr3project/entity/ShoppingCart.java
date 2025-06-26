package swd.project.swdgr3project.entity;

import lombok.Data;
import java.math.BigDecimal; // Import BigDecimal
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A transient (non-persistent) object to hold cart items within a user's session.
 * FINAL CORRECTED VERSION using BigDecimal for subtotal calculation.
 */
@Data
public class ShoppingCart {

    private List<CartItem> items = new ArrayList<>();

    // A more robust implementation that merges quantities if the product already exists.
    public void addItem(CartItem newItem) {
        if (newItem == null || newItem.getProduct() == null || newItem.getProduct().getId() == null) {
            return; // Do not add invalid items
        }

        for (CartItem existingItem : items) {
            // Check if product already exists in the cart
            if (Objects.equals(existingItem.getProduct().getId(), newItem.getProduct().getId())) {
                // Update quantity and return
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        // If product is not in the cart, add it as a new item
        items.add(newItem);
    }

    /**
     * Calculates the subtotal of all items in the cart.
     * @return The subtotal as a BigDecimal.
     */
    public BigDecimal getSubtotal() { // SỬA LỖI: Thay đổi kiểu trả về thành BigDecimal
        if (items == null) {
            return BigDecimal.ZERO;
        }

        // SỬA LỖI: Sử dụng map và reduce để tính tổng các giá trị BigDecimal
        return items.stream()
                .map(CartItem::getSubtotal) // .map() sẽ tạo ra một Stream<BigDecimal>
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Bắt đầu từ 0 và cộng dồn các giá trị
    }

    // Bạn có thể giữ lại phiên bản trả về double nếu cần cho việc hiển thị,
    // nhưng nên dùng phiên bản BigDecimal cho các logic tính toán.
    public double getSubtotalAsDouble() {
        return getSubtotal().doubleValue();
    }
}