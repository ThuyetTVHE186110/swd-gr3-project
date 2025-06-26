package swd.project.swdgr3project.service.impl;

import swd.project.swdgr3project.dao.ProductDAO;
import swd.project.swdgr3project.dao.impl.ProductDAOImpl;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory; // Correct import
import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Complete and concrete implementation of the ProductService interface that matches the provided code.
 */
public class ProductServiceImpl implements ProductService {

    // In a Spring app, this would be injected. Here, we instantiate it.
    private final ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public ProductDTO createProduct(String name, String description, double price, int stockQuantity, Long categoryId, List<String> imageUrls, String mainImageUrl) {
        ProductCategory category = productDAO.findCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stockQuantity);
        product.setCategory(category);
        product.setImageUrl(mainImageUrl);
        product.setAdditionalImages(imageUrls);
        product.setActive(true);

        Product savedProduct = productDAO.save(product);
        return ProductDTO.fromEntity(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long productId, String name, String description, Double price, Integer stockQuantity, Long categoryId, List<String> imageUrls, String mainImageUrl) {
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (name != null) product.setName(name);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(BigDecimal.valueOf(price));
        if (stockQuantity != null) product.setStock(stockQuantity);
        if (mainImageUrl != null) product.setImageUrl(mainImageUrl);
        if (imageUrls != null) product.setAdditionalImages(imageUrls);
        if (categoryId != null) {
            ProductCategory category = productDAO.findCategoryById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
            product.setCategory(category);
        }

        Product updatedProduct = productDAO.update(product);
        return ProductDTO.fromEntity(updatedProduct);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        return ProductDTO.fromEntity(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productDAO.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllActiveProducts() {
        return productDAO.findAllActive().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productDAO.findByCategory(categoryId).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getActiveProductsByCategory(Long categoryId) {
        return productDAO.findActiveByCategoryId(categoryId).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        return productDAO.search(keyword).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deactivateProduct(Long productId) {
        if (productDAO.findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        return productDAO.deactivate(productId);
    }

    @Override
    public boolean activateProduct(Long productId) {
        if (productDAO.findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        return productDAO.activate(productId);
    }

    @Override
    public boolean deleteProduct(Long productId) {
        if (productDAO.findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        return productDAO.delete(productId);
    }

    @Override
    public ProductDTO updateProductStock(Long productId, int quantity) {
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        product.setStock(quantity);
        Product updatedProduct = productDAO.update(product);
        return ProductDTO.fromEntity(updatedProduct);
    }

    @Override
    public ProductCategory createCategory(String name, String description) {
        ProductCategory category = new ProductCategory();
        category.setName(name);
        category.setDescription(description);
        return productDAO.saveCategory(category);
    }

    @Override
    public ProductCategory updateCategory(Long categoryId, String name, String description) {
        ProductCategory category = productDAO.findCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
        if (name != null) category.setName(name);
        if (description != null) category.setDescription(description);
        return productDAO.updateCategory(category);
    }

    @Override
    public ProductCategory getCategoryById(Long categoryId) {
        return productDAO.findCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return productDAO.findAllCategories();
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        if (productDAO.findCategoryById(categoryId).isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
        return productDAO.deleteCategory(categoryId);
    }
}