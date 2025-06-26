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
        dto.setImageUrls(product.getAdditionalImages());
        dto.setMainImageUrl(product.getImageUrl());
        dto.setCategory(ProductCategoryDTO.fromEntity(product.getCategory()));
        dto.setActive(product.isActive());

        return dto;
    }
}