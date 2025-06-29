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
    
    // Add pagination methods
    List<ProductDTO> getAllProducts(int page, int limit);

    List<ProductDTO> getAllActiveProducts();
    
    List<ProductDTO> getAllActiveProducts(int page, int limit);

    List<ProductDTO> getProductsByCategory(Long categoryId);
    
    // Add overload for category name
    List<ProductDTO> getProductsByCategory(String categoryName, int page, int limit);
    
    List<ProductDTO> getProductsByCategory(Long categoryId, int page, int limit);

    List<ProductDTO> getActiveProductsByCategory(Long categoryId);
    
    List<ProductDTO> getActiveProductsByCategory(Long categoryId, int page, int limit);

    List<ProductDTO> searchProducts(String keyword);
    
    // Add pagination for search
    List<ProductDTO> searchProducts(String keyword, int page, int limit);

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
    
    // Additional methods needed by AdminServlet
    
    /**
     * Create a product using ProductDTO.
     */
    ProductDTO createProduct(ProductDTO productDTO);
    
    /**
     * Update a product using ProductDTO.
     */
    ProductDTO updateProduct(ProductDTO productDTO);
}