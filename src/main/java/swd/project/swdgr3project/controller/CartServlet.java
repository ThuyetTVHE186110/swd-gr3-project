package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import swd.project.swdgr3project.entity.CartItem;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ShoppingCart;
import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends HttpServlet {
    
    private final ProductService productService = new ProductServiceImpl();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            showCartPage(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        switch (pathInfo) {
            case "/add":
                addToCart(req, resp);
                break;
            case "/remove":
                removeFromCart(req, resp);
                break;
            case "/update":
                updateCartQuantity(req, resp);
                break;
            case "/clear":
                clearCart(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void showCartPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }
    
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String productIdStr = req.getParameter("productId");
            String quantityStr = req.getParameter("quantity");
            
            if (productIdStr == null || quantityStr == null) {
                sendErrorResponse(resp, "Missing required parameters");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                sendErrorResponse(resp, "Quantity must be greater than 0");
                return;
            }
            
            ProductDTO productDTO = productService.getProductById(productId);
            if (productDTO == null) {
                sendErrorResponse(resp, "Product not found");
                return;
            }
            
            // Check stock availability
            if (productDTO.getStockQuantity() < quantity) {
                sendErrorResponse(resp, "Not enough stock available");
                return;
            }
            
            // Convert DTO to Entity for cart
            Product product = convertToEntity(productDTO);
            
            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                cart = new ShoppingCart();
                session.setAttribute("cart", cart);
            }
            
            // Check if product already exists in cart
            CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
            
            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + quantity;
                if (productDTO.getStockQuantity() < newQuantity) {
                    sendErrorResponse(resp, "Not enough stock available");
                    return;
                }
                existingItem.setQuantity(newQuantity);
                existingItem.updateTotalPrice();
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(product.getPrice());
                cartItem.updateTotalPrice();
                cart.addItem(cartItem);
            }
            
            cart.updateTotal();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product added to cart successfully");
            response.put("cartCount", cart.getTotalItems());
            response.put("cartTotal", cart.getTotalAmount());
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid product ID or quantity");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to add product to cart");
        }
    }
    
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String productIdStr = req.getParameter("productId");
            
            if (productIdStr == null) {
                sendErrorResponse(resp, "Missing product ID");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            
            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            
            if (cart != null) {
                cart.removeItem(productId);
                cart.updateTotal();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product removed from cart");
            response.put("cartCount", cart != null ? cart.getTotalItems() : 0);
            response.put("cartTotal", cart != null ? cart.getTotalAmount() : BigDecimal.ZERO);
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid product ID");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to remove product from cart");
        }
    }
    
    private void updateCartQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String productIdStr = req.getParameter("productId");
            String quantityStr = req.getParameter("quantity");
            
            if (productIdStr == null || quantityStr == null) {
                sendErrorResponse(resp, "Missing required parameters");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 0) {
                sendErrorResponse(resp, "Quantity cannot be negative");
                return;
            }
            
            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            
            if (cart != null) {
                if (quantity == 0) {
                    cart.removeItem(productId);
                } else {
                    // Check stock availability
                    ProductDTO productDTO = productService.getProductById(productId);
                    if (productDTO != null && productDTO.getStockQuantity() < quantity) {
                        sendErrorResponse(resp, "Not enough stock available");
                        return;
                    }
                    
                    CartItem item = cart.getItems().stream()
                        .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                        .findFirst()
                        .orElse(null);
                    
                    if (item != null) {
                        item.setQuantity(quantity);
                        item.updateTotalPrice();
                    }
                }
                cart.updateTotal();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cart updated successfully");
            response.put("cartCount", cart != null ? cart.getTotalItems() : 0);
            response.put("cartTotal", cart != null ? cart.getTotalAmount() : BigDecimal.ZERO);
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid product ID or quantity");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to update cart");
        }
    }
    
    private void clearCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            HttpSession session = req.getSession();
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            
            if (cart != null) {
                cart.clear();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cart cleared successfully");
            response.put("cartCount", 0);
            response.put("cartTotal", BigDecimal.ZERO);
            
            sendJsonResponse(resp, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to clear cart");
        }
    }
    
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }
    
    private void sendJsonResponse(HttpServletResponse resp, Map<String, Object> data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }
    
    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        sendJsonResponse(resp, response);
    }
}
