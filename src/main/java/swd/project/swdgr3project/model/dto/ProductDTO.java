package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Đảm bảo import đúng các lớp entity
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for transferring product data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private List<String> imageUrls;
    private String mainImageUrl;
    private ProductCategoryDTO category;
    private boolean active;

    // Helper method to get category name
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    // Backward compatibility methods
    public int getStock() {
        return this.stockQuantity;
    }

    public void setStock(int stock) {
        this.stockQuantity = stock;
    }

    public String getImageUrl() {
        return this.mainImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mainImageUrl = imageUrl;
    }

    /**
     * Helper method to set category by name (creates a simple category DTO).
     */
    public void setCategoryName(String categoryName) {
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            ProductCategoryDTO categoryDTO = new ProductCategoryDTO();
            categoryDTO.setName(categoryName.trim());
            this.category = categoryDTO;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCategoryDTO {
        private Long id;
        private String name;
        private String description;

        public static ProductCategoryDTO fromEntity(ProductCategory category) {
            if (category == null) return null;

            ProductCategoryDTO dto = new ProductCategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());

            return dto;
        }
    }

    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStock());
        
        // With EAGER fetching, additional images should be safely accessible
        List<String> additionalImages = product.getAdditionalImages();
        if (additionalImages != null) {
            // Create a new list to avoid any potential Hibernate proxy issues
            dto.setImageUrls(new java.util.ArrayList<>(additionalImages));
        } else {
            dto.setImageUrls(new java.util.ArrayList<>());
        }
        
        dto.setMainImageUrl(product.getImageUrl());
        dto.setCategory(ProductCategoryDTO.fromEntity(product.getCategory()));
        dto.setActive(product.isActive());

        return dto;
    }
}