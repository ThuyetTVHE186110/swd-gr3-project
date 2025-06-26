package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.ProductDTO;
// CORRECTED IMPORT
import swd.project.swdgr3project.entity.ProductCategory;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(String name, String description, double price, int stockQuantity,
                             Long categoryId, List<String> imageUrls, String mainImageUrl);

    ProductDTO updateProduct(Long productId, String name, String description, Double price,
                             Integer stockQuantity, Long categoryId, List<String> imageUrls, String mainImageUrl);

    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllActiveProducts();

    List<ProductDTO> getProductsByCategory(Long categoryId);

    List<ProductDTO> getActiveProductsByCategory(Long categoryId);

    List<ProductDTO> searchProducts(String keyword);

    boolean deactivateProduct(Long productId);

    boolean activateProduct(Long productId);

    boolean deleteProduct(Long productId);

    ProductDTO updateProductStock(Long productId, int quantity);

    // --- METHODS BELOW ARE CORRECTED ---

    /**
     * Create a new product category.
     */
    ProductCategory createCategory(String name, String description);

    /**
     * Update an existing product category.
     */
    ProductCategory updateCategory(Long categoryId, String name, String description);

    /**
     * Get a category by ID.
     */
    ProductCategory getCategoryById(Long categoryId);

    /**
     * Get all product categories.
     */
    List<ProductCategory> getAllCategories();

    /**
     * Delete a product category.
     */
    boolean deleteCategory(Long categoryId);
}