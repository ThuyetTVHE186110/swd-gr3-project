package swd.project.swdgr3project.dao;

import swd.project.swdgr3project.model.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product entity.
 * Provides methods to interact with the product data in the database.
 */
public interface ProductDAO {
    
    /**
     * Save a new product to the database
     * 
     * @param product The product to save
     * @return The saved product with generated ID
     */
    Product save(Product product);
    
    /**
     * Update an existing product in the database
     * 
     * @param product The product to update
     * @return The updated product
     */
    Product update(Product product);
    
    /**
     * Find a product by its ID
     * 
     * @param id The ID of the product to find
     * @return An Optional containing the product if found, or empty if not found
     */
    Optional<Product> findById(Long id);
    
    /**
     * Get all products from the database
     * 
     * @return A list of all products
     */
    List<Product> findAll();
    
    /**
     * Get all active products from the database
     * 
     * @return A list of all active products
     */
    List<Product> findAllActive();
    
    /**
     * Find products by category ID
     * 
     * @param categoryId The category ID to search for
     * @return A list of products in the specified category
     */
    List<Product> findByCategory(Long categoryId);
    
    /**
     * Find active products by category ID
     * 
     * @param categoryId The category ID to search for
     * @return A list of active products in the specified category
     */
    List<Product> findActiveByCategoryId(Long categoryId);
    
    /**
     * Search for products by name or description
     * 
     * @param keyword The keyword to search for in product names and descriptions
     * @return A list of products matching the search criteria
     */
    List<Product> search(String keyword);
    
    /**
     * Delete a product from the database
     * 
     * @param id The ID of the product to delete
     * @return true if the product was deleted, false otherwise
     */
    boolean delete(Long id);
    
    /**
     * Deactivate a product (set active flag to false)
     * 
     * @param id The ID of the product to deactivate
     * @return true if the product was deactivated, false otherwise
     */
    boolean deactivate(Long id);
    
    /**
     * Activate a product (set active flag to true)
     * 
     * @param id The ID of the product to activate
     * @return true if the product was activated, false otherwise
     */
    boolean activate(Long id);
    
    /**
     * Save a new product category
     * 
     * @param category The category to save
     * @return The saved category with generated ID
     */
    Product.Category saveCategory(Product.Category category);
    
    /**
     * Update an existing product category
     * 
     * @param category The category to update
     * @return The updated category
     */
    Product.Category updateCategory(Product.Category category);
    
    /**
     * Find a category by its ID
     * 
     * @param id The ID of the category to find
     * @return An Optional containing the category if found, or empty if not found
     */
    Optional<Product.Category> findCategoryById(Long id);
    
    /**
     * Get all product categories
     * 
     * @return A list of all product categories
     */
    List<Product.Category> findAllCategories();
    
    /**
     * Delete a product category
     * 
     * @param id The ID of the category to delete
     * @return true if the category was deleted, false otherwise
     */
    boolean deleteCategory(Long id);
}