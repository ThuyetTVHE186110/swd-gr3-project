package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a product in the system.
 * FINAL CORRECTED VERSION with JPA annotations and minor adjustments.
 */
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    private String imageUrl;

    // Đối với một danh sách các chuỗi đơn giản, LAZY là mặc định và thường là tốt.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_additional_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> additionalImages = new ArrayList<>();

    // SỬA LỖI 1: Thêm nullable = false
    // SỬA LỖI 2: Tạm thời đổi sang EAGER để giải quyết LazyInitializationException trong luồng hiện tại
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false) // Đảm bảo category_id không được null
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductCategory category;

    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Lombok's @Data annotation handles constructors, getters, and setters.

    // Helper methods (giữ nguyên, chúng đã đúng)
    public void addAdditionalImage(String imageUrl) {
        if (this.additionalImages == null) {
            this.additionalImages = new ArrayList<>();
        }
        this.additionalImages.add(imageUrl);
    }

    public void removeAdditionalImage(String imageUrl) {
        if (this.additionalImages != null) {
            this.additionalImages.remove(imageUrl);
        }
    }

    public boolean decreaseStock(int quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
            return true;
        }
        return false;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}