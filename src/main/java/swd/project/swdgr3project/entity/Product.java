package swd.project.swdgr3project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*; // Import các annotation của Jakarta Persistence (thay cho javax.persistence nếu dùng Tomcat 10+)
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a product in the system.
 * FINAL CORRECTED VERSION with JPA annotations.
 */
@Data
@Entity // <-- ANNOTATION QUAN TRỌNG NHẤT
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_additional_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> additionalImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductCategory category;

    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Lombok's @Data annotation handles constructors, getters, and setters.

    // Helper methods
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