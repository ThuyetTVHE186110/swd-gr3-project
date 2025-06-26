package swd.project.swdgr3project.dao;

// CORRECTED IMPORTS
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    Product save(Product product);
    Product update(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findAllActive();
    List<Product> findByCategory(Long categoryId);
    List<Product> findActiveByCategoryId(Long categoryId);
    List<Product> search(String keyword);
    boolean delete(Long id);
    boolean deactivate(Long id);
    boolean activate(Long id);

    // --- METHODS BELOW ARE CORRECTED ---

    /**
     * Save a new product category
     */
    ProductCategory saveCategory(ProductCategory category);

    /**
     * Update an existing product category
     */
    ProductCategory updateCategory(ProductCategory category);

    /**
     * Find a category by its ID
     */
    Optional<ProductCategory> findCategoryById(Long id);

    /**
     * Get all product categories
     */
    List<ProductCategory> findAllCategories();

    /**
     * Delete a product category
     */
    boolean deleteCategory(Long id);
}