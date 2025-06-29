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

    @Override
    public List<ProductDTO> getAllProducts(int page, int limit) {
        List<ProductDTO> allProducts = getAllProducts();
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allProducts.size());
        
        if (start >= allProducts.size()) {
            return List.of();
        }
        
        return allProducts.subList(start, end);
    }

    @Override
    public List<ProductDTO> getAllActiveProducts(int page, int limit) {
        List<ProductDTO> allActiveProducts = getAllActiveProducts();
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allActiveProducts.size());
        
        if (start >= allActiveProducts.size()) {
            return List.of();
        }
        
        return allActiveProducts.subList(start, end);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String categoryName, int page, int limit) {
        // Find category by name first
        List<ProductCategory> categories = getAllCategories();
        ProductCategory category = categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + categoryName));
        
        return getProductsByCategory(category.getId(), page, limit);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId, int page, int limit) {
        List<ProductDTO> allCategoryProducts = getProductsByCategory(categoryId);
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allCategoryProducts.size());
        
        if (start >= allCategoryProducts.size()) {
            return List.of();
        }
        
        return allCategoryProducts.subList(start, end);
    }

    @Override
    public List<ProductDTO> getActiveProductsByCategory(Long categoryId, int page, int limit) {
        List<ProductDTO> allActiveCategoryProducts = getActiveProductsByCategory(categoryId);
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allActiveCategoryProducts.size());
        
        if (start >= allActiveCategoryProducts.size()) {
            return List.of();
        }
        
        return allActiveCategoryProducts.subList(start, end);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword, int page, int limit) {
        List<ProductDTO> allSearchResults = searchProducts(keyword);
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, allSearchResults.size());
        
        if (start >= allSearchResults.size()) {
            return List.of();
        }
        
        return allSearchResults.subList(start, end);
    }
    
    // Additional methods needed by AdminServlet
    
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            // Find or create category
            ProductCategory category = null;
            if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().trim().isEmpty()) {
                List<ProductCategory> categories = productDAO.findAllCategories();
                category = categories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(productDTO.getCategoryName().trim()))
                        .findFirst()
                        .orElse(null);
                
                if (category == null) {
                    category = new ProductCategory();
                    category.setName(productDTO.getCategoryName().trim());
                    category.setDescription("Auto-created category: " + productDTO.getCategoryName().trim());
                    category = productDAO.saveCategory(category);
                }
            }

            // Create new product
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStock(productDTO.getStockQuantity());
            product.setImageUrl(productDTO.getMainImageUrl());
            product.setActive(productDTO.isActive());
            product.setCategory(category);

            Product savedProduct = productDAO.save(product);
            return ProductDTO.fromEntity(savedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        try {
            if (productDTO.getId() == null) {
                throw new IllegalArgumentException("Product ID is required for update");
            }

            Product existingProduct = productDAO.findById(productDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productDTO.getId()));

            // Update product fields
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setStock(productDTO.getStockQuantity());
            existingProduct.setImageUrl(productDTO.getMainImageUrl());
            existingProduct.setActive(productDTO.isActive());

            // Update category if provided
            if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().trim().isEmpty()) {
                List<ProductCategory> categories = productDAO.findAllCategories();
                ProductCategory category = categories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(productDTO.getCategoryName().trim()))
                        .findFirst()
                        .orElse(null);
                
                if (category == null) {
                    category = new ProductCategory();
                    category.setName(productDTO.getCategoryName().trim());
                    category.setDescription("Auto-created category: " + productDTO.getCategoryName().trim());
                    category = productDAO.saveCategory(category);
                }
                existingProduct.setCategory(category);
            }

            Product updatedProduct = productDAO.update(existingProduct);
            return ProductDTO.fromEntity(updatedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }
}