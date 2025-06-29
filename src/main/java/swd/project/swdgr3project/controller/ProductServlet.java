package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/products", "/product/*", "/api/products/*"})
public class ProductServlet extends HttpServlet {
    
    private final ProductService productService = new ProductServiceImpl();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String pathInfo = req.getPathInfo();
        
        try {
            if (requestURI.contains("/api/products")) {
                handleApiRequest(req, resp);
            } else if (requestURI.endsWith("/products")) {
                handleProductList(req, resp);
            } else if (pathInfo != null && pathInfo.startsWith("/")) {
                handleProductDetail(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to process request");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        
        try {
            if (requestURI.contains("/api/products")) {
                handleApiCreate(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to create product");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        
        try {
            if (requestURI.contains("/api/products")) {
                handleUpdate(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to update product");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        
        try {
            if (requestURI.contains("/api/products")) {
                handleDelete(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to delete product");
        }
    }
    
    private void handleApiRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // GET /api/products - return all products as JSON
            handleApiProductList(req, resp);
        } else if (pathInfo.startsWith("/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2) {
                String action = parts[1];
                
                switch (action) {
                    case "categories":
                        handleApiCategories(req, resp);
                        break;
                    case "search":
                        handleApiSearch(req, resp);
                        break;
                    default:
                        // Assume it's a product ID
                        try {
                            Long productId = Long.parseLong(action);
                            handleApiProductDetail(req, resp, productId);
                        } catch (NumberFormatException e) {
                            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
                        }
                        break;
                }
            }
        }
    }
    
    private void handleProductList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String category = req.getParameter("category");
            String search = req.getParameter("q");
            String pageParam = req.getParameter("page");
            String limitParam = req.getParameter("limit");
            
            int page = parseIntParameter(pageParam, 1);
            int limit = parseIntParameter(limitParam, 12);
            
            List<ProductDTO> products;
            
            if (search != null && !search.trim().isEmpty()) {
                products = productService.searchProducts(search.trim(), page, limit);
            } else if (category != null && !category.trim().isEmpty()) {
                try {
                    Long categoryId = Long.parseLong(category);
                    products = productService.getActiveProductsByCategory(categoryId, page, limit);
                } catch (NumberFormatException e) {
                    products = productService.getProductsByCategory(category.trim(), page, limit);
                }
            } else {
                products = productService.getAllActiveProducts(page, limit);
            }
            
            // Get categories for filter
            List<ProductCategory> categories = productService.getAllCategories();
            
            // Check if this is an AJAX request
            String ajaxHeader = req.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                sendJsonResponse(resp, Map.of(
                    "products", products,
                    "page", page,
                    "hasMore", products.size() == limit
                ));
            } else {
                // Return JSP page for regular requests
                req.setAttribute("products", products);
                req.setAttribute("categories", categories);
                req.setAttribute("currentPage", page);
                req.setAttribute("category", category);
                req.setAttribute("search", search);
                
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            }
            
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to load products");
        }
    }
    
    private void handleProductDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            String productIdStr = pathInfo.substring(1); // Remove leading slash
            
            Long productId = Long.parseLong(productIdStr);
            ProductDTO product = productService.getProductById(productId);
            
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            
            // Get related products from the same category
            List<ProductDTO> relatedProducts = List.of();
            if (product.getCategory() != null) {
                relatedProducts = productService.getActiveProductsByCategory(
                    product.getCategory().getId(), 1, 4);
            }
            
            req.setAttribute("product", product);
            req.setAttribute("relatedProducts", relatedProducts);
            
            req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
        } catch (Exception e) {
            handleError(req, resp, e, "Failed to load product details");
        }
    }
    
    private void handleApiProductList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String category = req.getParameter("category");
        String search = req.getParameter("q");
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        
        int page = parseIntParameter(pageParam, 1);
        int limit = parseIntParameter(limitParam, 12);
        
        List<ProductDTO> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search.trim(), page, limit);
        } else if (category != null && !category.trim().isEmpty()) {
            try {
                Long categoryId = Long.parseLong(category);
                products = productService.getActiveProductsByCategory(categoryId, page, limit);
            } catch (NumberFormatException e) {
                products = productService.getProductsByCategory(category.trim(), page, limit);
            }
        } else {
            products = productService.getAllActiveProducts(page, limit);
        }
        
        sendJsonResponse(resp, Map.of(
            "products", products,
            "page", page,
            "hasMore", products.size() == limit
        ));
    }
    
    private void handleApiCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ProductCategory> categories = productService.getAllCategories();
        sendJsonResponse(resp, Map.of("categories", categories));
    }
    
    private void handleApiSearch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String keyword = req.getParameter("q");
        if (keyword == null || keyword.trim().isEmpty()) {
            sendJsonResponse(resp, Map.of("products", List.of()));
            return;
        }
        
        int page = parseIntParameter(req.getParameter("page"), 1);
        int limit = parseIntParameter(req.getParameter("limit"), 12);
        
        List<ProductDTO> products = productService.searchProducts(keyword.trim(), page, limit);
        sendJsonResponse(resp, Map.of(
            "products", products,
            "page", page,
            "hasMore", products.size() == limit
        ));
    }
    
    private void handleApiProductDetail(HttpServletRequest req, HttpServletResponse resp, Long productId) throws IOException {
        try {
            ProductDTO product = productService.getProductById(productId);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            
            // Get related products
            List<ProductDTO> relatedProducts = List.of();
            if (product.getCategory() != null) {
                relatedProducts = productService.getActiveProductsByCategory(
                    product.getCategory().getId(), 1, 4);
            }
            
            sendJsonResponse(resp, Map.of(
                "product", product,
                "relatedProducts", relatedProducts
            ));
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
        }
    }
    
    private void handleApiCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String priceStr = req.getParameter("price");
            String stockStr = req.getParameter("stock");
            String categoryIdStr = req.getParameter("categoryId");
            String mainImageUrl = req.getParameter("mainImageUrl");
            String additionalImagesStr = req.getParameter("additionalImages");
            
            if (name == null || description == null || priceStr == null || 
                stockStr == null || categoryIdStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }
            
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            Long categoryId = Long.parseLong(categoryIdStr);
            
            List<String> additionalImages = List.of();
            if (additionalImagesStr != null && !additionalImagesStr.trim().isEmpty()) {
                additionalImages = Arrays.asList(additionalImagesStr.split(","));
            }
            
            ProductDTO product = productService.createProduct(
                name, description, price, stock, categoryId, additionalImages, mainImageUrl);
            
            sendJsonResponse(resp, Map.of("product", product));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
    
    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
            return;
        }
        
        try {
            String productIdStr = pathInfo.substring(1); // Remove leading slash
            Long productId = Long.parseLong(productIdStr);
            
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String priceStr = req.getParameter("price");
            String stockStr = req.getParameter("stock");
            String categoryIdStr = req.getParameter("categoryId");
            String mainImageUrl = req.getParameter("mainImageUrl");
            String additionalImagesStr = req.getParameter("additionalImages");
            
            Double price = priceStr != null ? Double.parseDouble(priceStr) : null;
            Integer stock = stockStr != null ? Integer.parseInt(stockStr) : null;
            Long categoryId = categoryIdStr != null ? Long.parseLong(categoryIdStr) : null;
            
            List<String> additionalImages = null;
            if (additionalImagesStr != null && !additionalImagesStr.trim().isEmpty()) {
                additionalImages = Arrays.asList(additionalImagesStr.split(","));
            }
            
            ProductDTO product = productService.updateProduct(
                productId, name, description, price, stock, categoryId, additionalImages, mainImageUrl);
            
            sendJsonResponse(resp, Map.of("product", product));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
    
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
            return;
        }
        
        try {
            String productIdStr = pathInfo.substring(1); // Remove leading slash
            Long productId = Long.parseLong(productIdStr);
            
            String action = req.getParameter("action");
            boolean success;
            
            if ("deactivate".equals(action)) {
                success = productService.deactivateProduct(productId);
            } else if ("activate".equals(action)) {
                success = productService.activateProduct(productId);
            } else {
                success = productService.deleteProduct(productId);
            }
            
            sendJsonResponse(resp, Map.of("success", success));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
    
    // Utility methods
    private int parseIntParameter(String param, int defaultValue) {
        if (param == null || param.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }
    
    private void handleError(HttpServletRequest req, HttpServletResponse resp, Exception e, String defaultMessage) {
        e.printStackTrace();
        
        try {
            String ajaxHeader = req.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", defaultMessage);
                errorResponse.put("message", e.getMessage());
                
                resp.getWriter().write(gson.toJson(errorResponse));
            } else {
                req.setAttribute("error", defaultMessage + ": " + e.getMessage());
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            }
        } catch (IOException | ServletException ex) {
            ex.printStackTrace();
        }
    }
}
