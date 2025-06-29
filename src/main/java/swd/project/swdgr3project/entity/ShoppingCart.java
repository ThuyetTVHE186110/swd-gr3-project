package swd.project.swdgr3project.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ShoppingCart {

    private List<CartItem> items = new ArrayList<>();
    private BigDecimal shippingFee = new BigDecimal("30000"); // Đặt phí vận chuyển cố định là 30.000đ

    // CHÚ Ý QUAN TRỌNG:
    // Các trường rawTotal và discount KHÔNG CÓ sẵn trong Product/CartItem/ShoppingCart của bạn.
    // Để hiển thị chúng chính xác, bạn cần:
    // 1. Thêm trường 'oldPrice' vào Product.java (nếu có giá gốc)
    // 2. Thêm logic tính toán 'rawTotal' và 'discount' vào ShoppingCart.
    // Hiện tại, tôi sẽ giả định:
    //    - 'productsTotal' là tổng tiền các sản phẩm (tức là tổng price * quantity của CartItem)
    //    - 'rawTotal' = 'productsTotal' (vì không có oldPrice cao hơn)
    //    - 'discount' = 0 (vì không có logic chiết khấu rõ ràng)
    //    - 'finalTotal' = productsTotal + shippingFee

    private BigDecimal productsTotal; // Tổng tiền của các sản phẩm (trước phí vận chuyển/chiết khấu)
    private BigDecimal rawTotal;      // Tổng tiền hàng (có thể là giá gốc nếu có oldPrice)
    private BigDecimal discount;      // Tổng chiết khấu
    private BigDecimal finalTotal;    // Tổng tiền cuối cùng phải trả

    public ShoppingCart() {
        this.items = new ArrayList<>();
        // Khởi tạo các giá trị BigDecimal về 0
        this.productsTotal = BigDecimal.ZERO;
        this.rawTotal = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.finalTotal = BigDecimal.ZERO;
    }

    public void addItem(CartItem newItem) {
        if (newItem == null || newItem.getProduct() == null || newItem.getProduct().getId() == null) {
            return;
        }

        for (CartItem existingItem : items) {
            if (Objects.equals(existingItem.getProduct().getId(), newItem.getProduct().getId())) {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                // Cập nhật lại price của CartItem nếu cần (nếu giá sản phẩm có thể thay đổi trong DB)
                existingItem.setPrice(newItem.getProduct().getPrice()); // Cập nhật giá mới nhất từ Product
                recalculateTotals(); // Tính toán lại sau khi cập nhật
                return;
            }
        }
        items.add(newItem);
        recalculateTotals(); // Tính toán lại sau khi thêm item mới
    }

    // Phương thức tính toán tổng tiền của các sản phẩm (subtotal)
    public BigDecimal calculateProductsTotal() {
        if (items == null) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Phương thức recalculateTotals() sẽ cập nhật tất cả các trường tổng tiền
    public void recalculateTotals() {
        this.productsTotal = calculateProductsTotal(); // Tổng tiền của các sản phẩm

        // Với cấu trúc Product bạn cung cấp (không có oldPrice):
        this.rawTotal = this.productsTotal; // Tổng tiền hàng ban đầu coi như bằng tổng tiền sản phẩm
        this.discount = BigDecimal.ZERO;    // Không có logic chiết khấu cụ thể, nên đặt là 0

        // Final total = tổng sản phẩm + phí vận chuyển - chiết khấu
        this.finalTotal = this.productsTotal.add(this.shippingFee).subtract(this.discount);

        // Đảm bảo finalTotal không âm
        if (this.finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            this.finalTotal = BigDecimal.ZERO;
        }
    }

    // Getters đã được Lombok @Data tạo ra: getItems(), getShippingFee(), getProductsTotal(), getRawTotal(), getDiscount(), getFinalTotal()

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the total number of items in the cart.
     * @return total number of items
     */
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /**
     * Gets the total amount (same as finalTotal).
     * @return total amount
     */
    public BigDecimal getTotalAmount() {
        return this.finalTotal;
    }

    /**
     * Updates the totals (calls recalculateTotals).
     */
    public void updateTotal() {
        recalculateTotals();
    }

    /**
     * Removes an item from the cart by product ID.
     * @param productId the product ID to remove
     */
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        recalculateTotals();
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
        recalculateTotals();
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "items=" + items.size() +
                ", productsTotal=" + productsTotal +
                ", shippingFee=" + shippingFee +
                ", rawTotal=" + rawTotal +
                ", discount=" + discount +
                ", finalTotal=" + finalTotal +
                '}';
    }
}