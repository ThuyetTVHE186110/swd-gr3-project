package swd.project.swdgr3project.service;

import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.model.entity.Product;

import java.util.List;

/**
 * Service interface for product-related operations.
 */
public interface ProductService {
    
    /**
     * Create a new product.
     *
     * @param name The product name
     * @param description The product description
     * @param price The product price
     * @param stockQuantity The initial stock quantity
     * @param categoryId The category ID
     * @param imageUrls List of image URLs (from Cloudinary)
     * @param mainImageUrl The main image URL
     * @return The created product DTO
     */
    ProductDTO createProduct(String name, String description, double price, int stockQuantity, 
                            Long categoryId, List<String> imageUrls, String mainImageUrl);
    
    /**
     * Update an existing product.
     *
     * @param productId The product ID
     * @param name The new name (or null to keep current)
     * @param description The new description (or null to keep current)
     * @param price The new price (or null to keep current)
     * @param stockQuantity The new stock quantity (or null to keep current)
     * @param categoryId The new category ID (or null to keep current)
     * @param imageUrls The new image URLs (or null to keep current)
     * @param mainImageUrl The new main image URL (or null to keep current)
     * @return The updated product DTO
     * @throws IllegalArgumentException if product not found
     */
    ProductDTO updateProduct(Long productId, String name, String description, Double price, 
                            Integer stockQuantity, Long categoryId, List<String> imageUrls, String mainImageUrl);
    
    /**
     * Get a product by ID.
     *
     * @param productId The product ID
     * @return The product DTO
     * @throws IllegalArgumentException if product not found
     */
    ProductDTO getProductById(Long productId);
    
    /**
     * Get all products.
     *
     * @return List of all product DTOs
     */
    List<ProductDTO> getAllProducts();
    
    /**
     * Get all active products.
     *
     * @return List of all active product DTOs
     */
    List<ProductDTO> getAllActiveProducts();
    
    /**
     * Get products by category.
     *
     * @param categoryId The category ID
     * @return List of product DTOs in the specified category
     */
    List<ProductDTO> getProductsByCategory(Long categoryId);
    
    /**
     * Get active products by category.
     *
     * @param categoryId The category ID
     * @return List of active product DTOs in the specified category
     */
    List<ProductDTO> getActiveProductsByCategory(Long categoryId);
    
    /**
     * Search for products by keyword in name or description.
     *
     * @param keyword The search keyword
     * @return List of matching product DTOs
     */
    List<ProductDTO> searchProducts(String keyword);
    
    /**
     * Deactivate a product.
     *
     * @param productId The product ID
     * @return true if the product was deactivated
     * @throws IllegalArgumentException if product not found
     */
    boolean deactivateProduct(Long productId);
    
    /**
     * Activate a product.
     *
     * @param productId The product ID
     * @return true if the product was activated
     * @throws IllegalArgumentException if product not found
     */
    boolean activateProduct(Long productId);
    
    /**
     * Delete a product.
     *
     * @param productId The product ID
     * @return true if the product was deleted
     * @throws IllegalArgumentException if product not found
     */
    boolean deleteProduct(Long productId);
    
    /**
     * Update product stock quantity.
     *
     * @param productId The product ID
     * @param quantity The new quantity
     * @return The updated product DTO
     * @throws IllegalArgumentException if product not found
     */
    ProductDTO updateProductStock(Long productId, int quantity);
    
    /**
     * Create a new product category.
     *
     * @param name The category name
     * @param description The category description
     * @return The created category
     */
    Product.Category createCategory(String name, String description);
    
    /**
     * Update an existing product category.
     *
     * @param categoryId The category ID
     * @param name The new name (or null to keep current)
     * @param description The new description (or null to keep current)
     * @return The updated category
     * @throws IllegalArgumentException if category not found
     */
    Product.Category updateCategory(Long categoryId, String name, String description);
    
    /**
     * Get a category by ID.
     *
     * @param categoryId The category ID
     * @return The category
     * @throws IllegalArgumentException if category not found
     */
    Product.Category getCategoryById(Long categoryId);
    
    /**
     * Get all product categories.
     *
     * @return List of all categories
     */
    List<Product.Category> getAllCategories();
    
    /**
     * Delete a product category.
     *
     * @param categoryId The category ID
     * @return true if the category was deleted
     * @throws IllegalArgumentException if category not found or has associated products
     */
    boolean deleteCategory(Long categoryId);
}