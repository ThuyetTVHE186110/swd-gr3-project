package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import swd.project.swdgr3project.util.CloudinaryUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/upload/*", "/admin/upload/*"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ImageUploadServlet extends HttpServlet {
    
    private final Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo != null && pathInfo.startsWith("/product")) {
                handleProductImageUpload(req, resp);
            } else if (pathInfo != null && pathInfo.startsWith("/user")) {
                handleUserImageUpload(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Upload endpoint not found");
            }
        } catch (Exception e) {
            handleError(resp, e, "Upload failed");
        }
    }
    
    private void handleProductImageUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Check if Cloudinary is configured
        if (!CloudinaryUtil.isConfigured()) {
            sendErrorResponse(resp, "Image upload service is not configured");
            return;
        }
        
        Part imagePart = req.getPart("image");
        if (imagePart == null) {
            sendErrorResponse(resp, "No image file provided");
            return;
        }
        
        // Validate file type
        String contentType = imagePart.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            sendErrorResponse(resp, "Invalid file type. Only JPG, PNG, and WebP are allowed");
            return;
        }
        
        // Validate file size
        long fileSize = imagePart.getSize();
        if (fileSize > 5 * 1024 * 1024) { // 5MB limit
            sendErrorResponse(resp, "File size too large. Maximum size is 5MB");
            return;
        }
        
        try {
            // Read image bytes
            byte[] imageBytes = readPartBytes(imagePart);
            
            // Generate public ID
            String publicId = generateProductImagePublicId(req.getParameter("productId"));
            
            // Upload to Cloudinary
            String imageUrl = CloudinaryUtil.uploadImage(imageBytes, publicId, "phonehub/products");
            
            if (imageUrl != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("imageUrl", imageUrl);
                response.put("publicId", publicId);
                response.put("message", "Image uploaded successfully");
                
                sendJsonResponse(resp, response);
            } else {
                sendErrorResponse(resp, "Failed to upload image to cloud storage");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error processing image: " + e.getMessage());
        }
    }
    
    private void handleUserImageUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Check if Cloudinary is configured
        if (!CloudinaryUtil.isConfigured()) {
            sendErrorResponse(resp, "Image upload service is not configured");
            return;
        }
        
        Part imagePart = req.getPart("image");
        if (imagePart == null) {
            sendErrorResponse(resp, "No image file provided");
            return;
        }
        
        // Validate file type
        String contentType = imagePart.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            sendErrorResponse(resp, "Invalid file type. Only JPG, PNG, and WebP are allowed");
            return;
        }
        
        // Validate file size
        long fileSize = imagePart.getSize();
        if (fileSize > 2 * 1024 * 1024) { // 2MB limit for user images
            sendErrorResponse(resp, "File size too large. Maximum size is 2MB for user images");
            return;
        }
        
        try {
            // Read image bytes
            byte[] imageBytes = readPartBytes(imagePart);
            
            // Generate public ID
            String userId = req.getParameter("userId");
            String publicId = generateUserImagePublicId(userId);
            
            // Upload to Cloudinary
            String imageUrl = CloudinaryUtil.uploadImage(imageBytes, publicId, "phonehub/users");
            
            if (imageUrl != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("imageUrl", imageUrl);
                response.put("publicId", publicId);
                response.put("message", "User image uploaded successfully");
                
                sendJsonResponse(resp, response);
            } else {
                sendErrorResponse(resp, "Failed to upload image to cloud storage");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error processing image: " + e.getMessage());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle image optimization requests
        String pathInfo = req.getPathInfo();
        
        if (pathInfo != null && pathInfo.startsWith("/optimize")) {
            handleImageOptimization(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method not supported for this endpoint");
        }
    }
    
    private void handleImageOptimization(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String imageUrl = req.getParameter("url");
        String widthParam = req.getParameter("width");
        String heightParam = req.getParameter("height");
        
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            sendErrorResponse(resp, "Image URL is required");
            return;
        }
        
        if (!imageUrl.contains("cloudinary.com")) {
            sendErrorResponse(resp, "Only Cloudinary URLs are supported for optimization");
            return;
        }
        
        try {
            int width = widthParam != null ? Integer.parseInt(widthParam) : 400;
            int height = heightParam != null ? Integer.parseInt(heightParam) : 400;
            
            String publicId = CloudinaryUtil.extractPublicId(imageUrl);
            if (publicId == null) {
                sendErrorResponse(resp, "Invalid Cloudinary URL");
                return;
            }
            
            String optimizedUrl = CloudinaryUtil.getOptimizedProductImageUrl(publicId, width, height);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("originalUrl", imageUrl);
            response.put("optimizedUrl", optimizedUrl);
            response.put("width", width);
            response.put("height", height);
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid width or height parameter");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error optimizing image: " + e.getMessage());
        }
    }
    
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
               contentType.equals("image/jpg") ||
               contentType.equals("image/png") ||
               contentType.equals("image/webp");
    }
    
    private byte[] readPartBytes(Part part) throws IOException {
        try (InputStream inputStream = part.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
    
    private String generateProductImagePublicId(String productId) {
        if (productId != null && !productId.trim().isEmpty()) {
            return "product_" + productId + "_" + System.currentTimeMillis();
        }
        return "product_new_" + System.currentTimeMillis();
    }
    
    private String generateUserImagePublicId(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            return "user_" + userId + "_" + System.currentTimeMillis();
        }
        return "user_new_" + System.currentTimeMillis();
    }
    
    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
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
    
    private void handleError(HttpServletResponse resp, Exception e, String defaultMessage) throws IOException {
        e.printStackTrace();
        sendErrorResponse(resp, defaultMessage + ": " + e.getMessage());
    }
}
