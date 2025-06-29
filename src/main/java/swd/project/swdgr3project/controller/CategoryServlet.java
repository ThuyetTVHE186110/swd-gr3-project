package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/categories/*", "/admin/categories/*"})
public class CategoryServlet extends HttpServlet {
    
    private final ProductService productService = new ProductServiceImpl();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String pathInfo = req.getPathInfo();
        
        try {
            if (requestURI.contains("/api/categories")) {
                handleApiRequest(req, resp, pathInfo);
            } else if (requestURI.contains("/admin/categories")) {
                handleAdminRequest(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(resp, e, "Failed to process request");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            
            if (name == null || name.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
                return;
            }
            
            ProductCategory category = productService.createCategory(name.trim(), description);
            sendJsonResponse(resp, Map.of("category", category));
            
        } catch (Exception e) {
            handleError(resp, e, "Failed to create category");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID is required");
            return;
        }
        
        try {
            String categoryIdStr = pathInfo.substring(1); // Remove leading slash
            Long categoryId = Long.parseLong(categoryIdStr);
            
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            
            ProductCategory category = productService.updateCategory(categoryId, name, description);
            sendJsonResponse(resp, Map.of("category", category));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
        } catch (Exception e) {
            handleError(resp, e, "Failed to update category");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID is required");
            return;
        }
        
        try {
            String categoryIdStr = pathInfo.substring(1); // Remove leading slash
            Long categoryId = Long.parseLong(categoryIdStr);
            
            boolean success = productService.deleteCategory(categoryId);
            sendJsonResponse(resp, Map.of("success", success));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
        } catch (Exception e) {
            handleError(resp, e, "Failed to delete category");
        }
    }
    
    private void handleApiRequest(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        if (pathInfo == null || pathInfo.equals("/")) {
            // GET /api/categories - return all categories
            List<ProductCategory> categories = productService.getAllCategories();
            sendJsonResponse(resp, Map.of("categories", categories));
        } else {
            // GET /api/categories/{id} - return specific category
            try {
                String categoryIdStr = pathInfo.substring(1); // Remove leading slash
                Long categoryId = Long.parseLong(categoryIdStr);
                
                ProductCategory category = productService.getCategoryById(categoryId);
                sendJsonResponse(resp, Map.of("category", category));
                
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
            } catch (IllegalArgumentException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
            }
        }
    }
    
    private void handleAdminRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Admin requests return JSP pages
        List<ProductCategory> categories = productService.getAllCategories();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/admin/categories.jsp").forward(req, resp);
    }
    
    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }
    
    private void handleError(HttpServletResponse resp, Exception e, String defaultMessage) {
        e.printStackTrace();
        
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", defaultMessage);
            errorResponse.put("message", e.getMessage());
            
            resp.getWriter().write(gson.toJson(errorResponse));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
