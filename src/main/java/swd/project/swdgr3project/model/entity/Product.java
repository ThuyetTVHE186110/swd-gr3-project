package swd.project.swdgr3project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a product in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private List<String> imageUrls; // Cloudinary URLs
    private String mainImageUrl; // Main product image
    private Category category;
    private boolean active; // Whether the product is visible to customers
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Entity class representing a product category
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;
        private String description;
    }
}