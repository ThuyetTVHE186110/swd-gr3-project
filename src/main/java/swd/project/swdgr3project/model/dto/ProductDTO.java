package swd.project.swdgr3project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.project.swdgr3project.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO class for transferring product data between layers.
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
    private CategoryDTO category;
    private boolean active;
    
    /**
     * DTO class for transferring category data
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDTO {
        private Long id;
        private String name;
        private String description;
        
        /**
         * Converts a Product.Category entity to a CategoryDTO
         * @param category The Category entity to convert
         * @return A CategoryDTO
         */
        public static CategoryDTO fromEntity(Product.Category category) {
            if (category == null) return null;
            
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());
            
            return dto;
        }
    }
    
    /**
     * Converts a Product entity to a ProductDTO
     * @param product The Product entity to convert
     * @return A ProductDTO
     */
    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setImageUrls(product.getImageUrls());
        dto.setMainImageUrl(product.getMainImageUrl());
        dto.setCategory(CategoryDTO.fromEntity(product.getCategory()));
        dto.setActive(product.isActive());
        
        return dto;
    }
}