package swd.project.swdgr3project.controller;

import com.google.gson.Gson;
import swd.project.swdgr3project.model.dto.ProductDTO;
import swd.project.swdgr3project.model.dto.UserDTO;
import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.service.ProductService;
import swd.project.swdgr3project.service.UserService;
import swd.project.swdgr3project.service.impl.ProductServiceImpl;
import swd.project.swdgr3project.service.impl.UserServiceImpl;
import swd.project.swdgr3project.util.CloudinaryUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/admin", "/admin/*"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class AdminServlet extends HttpServlet {
    
    private final ProductService productService = new ProductServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final Gson gson = new Gson();
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize UserService here - for now using a placeholder
        // You would inject this through dependency injection in a real application
        // this.userService = new UserServiceImpl();
        
        // Test Cloudinary configuration on startup
        try {
            if (CloudinaryUtil.isConfigured()) {
                System.out.println("✓ Cloudinary is properly configured and ready for image uploads");
            } else {
                System.err.println("⚠ Cloudinary is NOT configured. Image uploads will fall back to URL input only.");
                String error = CloudinaryUtil.getInitializationError();
                if (error != null) {
                    System.err.println("   Configuration error: " + error);
                }
                System.err.println("   Please check CLOUDINARY_SETUP_GUIDE.md for setup instructions.");
            }
        } catch (Exception e) {
            System.err.println("⚠ Error testing Cloudinary configuration: " + e.getMessage());
            System.err.println("   Image uploads will fall back to URL input only.");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        System.out.println("AdminServlet - doGet called with pathInfo: " + pathInfo);
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                System.out.println("Showing admin dashboard");
                showAdminDashboard(req, resp);
            } else if (pathInfo.equals("/products")) {
                System.out.println("Showing product management");
                showProductManagement(req, resp);
            } else if (pathInfo.equals("/orders")) {
                System.out.println("Showing order management");
                showOrderManagement(req, resp);
            } else if (pathInfo.equals("/users")) {
                System.out.println("Showing user management");
                showUserManagement(req, resp);
            } else if (pathInfo.startsWith("/users/api")) {
                handleUserAPI(req, resp);
            } else if (pathInfo.startsWith("/product/")) {
                String action = pathInfo.substring("/product/".length());
                if (action.equals("new")) {
                    showProductForm(req, resp, null);
                } else {
                    try {
                        Long productId = Long.parseLong(action);
                        ProductDTO product = productService.getProductById(productId);
                        showProductForm(req, resp, product);
                    } catch (NumberFormatException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
                    }
                }
            } else {
                System.out.println("Path not found: " + pathInfo);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error in AdminServlet doGet: " + e.getMessage());
            e.printStackTrace();
            
            // Check if response is already committed before forwarding
            if (!resp.isCommitted()) {
                req.setAttribute("error", "Internal server error: " + e.getMessage());
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
            } else {
                // Response already committed, just log the error
                System.err.println("Cannot forward to error page - response already committed");
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set request encoding to handle UTF-8 characters properly
        req.setCharacterEncoding("UTF-8");
        
        String pathInfo = req.getPathInfo();
        
        System.out.println("AdminServlet doPost - pathInfo: " + pathInfo);
        System.out.println("AdminServlet doPost - contentType: " + req.getContentType());
        
        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        try {
            if (pathInfo.equals("/product/save")) {
                saveProduct(req, resp);
            } else if (pathInfo.equals("/product/delete")) {
                deleteProduct(req, resp);
            } else if (pathInfo.equals("/user/save")) {
                saveUser(req, resp);
            } else if (pathInfo.equals("/user/update")) {
                updateUser(req, resp);
            } else if (pathInfo.equals("/user/delete")) {
                deleteUserFromAdmin(req, resp);
            } else if (pathInfo.equals("/user/toggle-status")) {
                toggleUserStatus(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error in AdminServlet doPost: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(resp, "Internal server error: " + e.getMessage());
        }
    }
    
    private void showAdminDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Get dashboard statistics
            List<ProductDTO> products = productService.getAllProducts(1, 10);
            
            req.setAttribute("totalProducts", products.size());
            req.setAttribute("recentProducts", products);
            
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            // Only forward if response hasn't been committed
            if (!resp.isCommitted()) {
                req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
                req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
            }
        }
    }
    
    private void showProductManagement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("ProductManagement - Starting to load products");
            
            String pageParam = req.getParameter("page");
            String limitParam = req.getParameter("limit");
            
            int page = 1;
            int limit = 20;
            
            try {
                if (pageParam != null) page = Integer.parseInt(pageParam);
                if (limitParam != null) limit = Integer.parseInt(limitParam);
            } catch (NumberFormatException e) {
                System.out.println("Using default page/limit values due to invalid parameters");
            }
            
            System.out.println("Loading products with page=" + page + ", limit=" + limit);
            
            List<ProductDTO> products = productService.getAllProducts(page, limit);
            System.out.println("Loaded " + products.size() + " products");
            
            req.setAttribute("products", products);
            req.setAttribute("currentPage", page);
            
            System.out.println("Forwarding to /adminpage/products.jsp");
            req.getRequestDispatcher("/adminpage/products.jsp").forward(req, resp);
            
        } catch (Exception e) {
            System.err.println("Error in showProductManagement: " + e.getMessage());
            e.printStackTrace();
            
            // Only forward if response hasn't been committed
            if (!resp.isCommitted()) {
                req.setAttribute("error", "Failed to load products: " + e.getMessage());
                req.getRequestDispatcher("/adminpage/products.jsp").forward(req, resp);
            }
        }
    }
    
    private void showOrderManagement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: Implement order management
        req.getRequestDispatcher("/adminpage/orders.jsp").forward(req, resp);
    }
    
    private void showUserManagement(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pageParam = req.getParameter("page");
            int page = 1;
            
            try {
                if (pageParam != null) page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<UserDTO> users = userService.getUsersWithPagination(page, 20);
            
            req.setAttribute("users", users);
            req.setAttribute("currentPage", page);
            
            req.getRequestDispatcher("/adminpage/users.jsp").forward(req, resp);
            
        } catch (Exception e) {
            // Only forward if response hasn't been committed
            if (!resp.isCommitted()) {
                req.setAttribute("error", "Failed to load users: " + e.getMessage());
                req.getRequestDispatcher("/adminpage/users.jsp").forward(req, resp);
            }
        }
    }
    


    private void showProductForm(HttpServletRequest req, HttpServletResponse resp, ProductDTO product) throws ServletException, IOException {
        req.setAttribute("product", product);
        req.getRequestDispatcher("/adminpage/product-form.jsp").forward(req, resp);
    }
    
    private void saveProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // Debug: Log all parameters
            System.out.println("=== SaveProduct Debug Info ===");
            System.out.println("Content-Type: " + req.getContentType());
            System.out.println("Method: " + req.getMethod());
            
            String idParam, name, description, priceParam, stockParam, imageUrl, categoryName;
            Part imagePart = null;
            
            // Check if this is a multipart request
            String contentType = req.getContentType();
            boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");
            
            if (isMultipart) {
                System.out.println("Processing multipart form data");
                // Extract parameters from multipart request
                idParam = getPartValue(req, "id");
                name = getPartValue(req, "name");
                description = getPartValue(req, "description");
                priceParam = getPartValue(req, "price");
                stockParam = getPartValue(req, "stock");
                imageUrl = getPartValue(req, "imageUrl");
                categoryName = getPartValue(req, "category");
                
                // Get image file part
                try {
                    imagePart = req.getPart("imageFile");
                    if (imagePart != null) {
                        System.out.println("Image file uploaded - Size: " + imagePart.getSize() + 
                                         " bytes, Content-Type: " + imagePart.getContentType());
                    }
                } catch (Exception e) {
                    System.out.println("No image file part found or error getting part: " + e.getMessage());
                }
                
            } else {
                System.out.println("Processing regular form data");
                // Extract parameters from regular request
                idParam = req.getParameter("id");
                name = req.getParameter("name");
                description = req.getParameter("description");
                priceParam = req.getParameter("price");
                stockParam = req.getParameter("stock");
                imageUrl = req.getParameter("imageUrl");
                categoryName = req.getParameter("category");
            }
            
            System.out.println("Extracted values:");
            System.out.println("  name: '" + name + "'");
            System.out.println("  price: '" + priceParam + "'");
            System.out.println("  stock: '" + stockParam + "'");
            System.out.println("  category: '" + categoryName + "'");
            System.out.println("  imageUrl: '" + imageUrl + "'");
            System.out.println("  imagePart: " + (imagePart != null ? "present" : "null"));
            System.out.println("================================");
            
            // Validation
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Validation failed: Product name is missing or empty");
                sendErrorResponse(resp, "Product name is required");
                return;
            }
            
            if (priceParam == null || priceParam.trim().isEmpty()) {
                System.out.println("Validation failed: Product price is missing or empty");
                sendErrorResponse(resp, "Product price is required");
                return;
            }
            
            if (stockParam == null || stockParam.trim().isEmpty()) {
                System.out.println("Validation failed: Product stock is missing or empty");
                sendErrorResponse(resp, "Product stock is required");
                return;
            }
            
            BigDecimal price;
            int stock;
            
            try {
                price = new BigDecimal(priceParam);
                stock = Integer.parseInt(stockParam);
            } catch (NumberFormatException e) {
                sendErrorResponse(resp, "Invalid price or stock value");
                return;
            }
            
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                sendErrorResponse(resp, "Price must be greater than 0");
                return;
            }
            
            if (stock < 0) {
                sendErrorResponse(resp, "Stock cannot be negative");
                return;
            }
            
            // Handle image upload to Cloudinary
            String finalImageUrl = imageUrl;
            if (imagePart != null && imagePart.getSize() > 0) {
                try {
                    // Validate the image file
                    if (CloudinaryUtil.isValidImage(imagePart)) {
                        System.out.println("Uploading image to Cloudinary...");
                        String cloudinaryUrl = CloudinaryUtil.uploadImage(imagePart, "products");
                        finalImageUrl = cloudinaryUrl;
                        System.out.println("Image uploaded successfully: " + finalImageUrl);
                    } else {
                        sendErrorResponse(resp, "Invalid image file. Please upload a valid image (JPG, PNG, GIF, WebP)");
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Failed to upload image to Cloudinary: " + e.getMessage());
                    // Don't fail the entire operation, just log the error and use the URL if provided
                    if (imageUrl == null || imageUrl.trim().isEmpty()) {
                        sendErrorResponse(resp, "Failed to upload image. Please try again or provide an image URL.");
                        return;
                    }
                }
            }
            
            ProductDTO productDTO = new ProductDTO();
            if (idParam != null && !idParam.trim().isEmpty()) {
                productDTO.setId(Long.parseLong(idParam));
            }
            productDTO.setName(name.trim());
            productDTO.setDescription(description != null ? description.trim() : "");
            productDTO.setPrice(price);
            productDTO.setStockQuantity(stock);
            productDTO.setMainImageUrl(finalImageUrl != null ? finalImageUrl.trim() : "");
            productDTO.setActive(true);
            productDTO.setCategoryName(categoryName != null ? categoryName.trim() : "General");
            
            System.out.println("About to save product: " + productDTO.getName());
            
            ProductDTO savedProduct;
            try {
                if (productDTO.getId() != null) {
                    System.out.println("Updating existing product with ID: " + productDTO.getId());
                    savedProduct = productService.updateProduct(productDTO);
                } else {
                    System.out.println("Creating new product");
                    savedProduct = productService.createProduct(productDTO);
                }
                System.out.println("Product saved successfully with ID: " + savedProduct.getId());
            } catch (Exception serviceException) {
                System.err.println("Error in ProductService: " + serviceException.getMessage());
                serviceException.printStackTrace();
                sendErrorResponse(resp, "Failed to save product to database: " + serviceException.getMessage());
                return;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product saved successfully");
            response.put("product", savedProduct);
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid product ID");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to save product: " + e.getMessage());
        }
    }
    
    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String productIdStr = req.getParameter("id");
            
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                sendErrorResponse(resp, "Product ID is required");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            
            boolean deleted = productService.deleteProduct(productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", deleted);
            response.put("message", deleted ? "Product deleted successfully" : "Failed to delete product");
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid product ID");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to delete product: " + e.getMessage());
        }
    }
    
    private void handleUserAPI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo.equals("/users/api")) {
            // Return all users as JSON
            try {
                List<UserDTO> users = userService.getAllUsers();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("users", users);
                response.put("totalPages", 1);
                sendJsonResponse(resp, response);
            } catch (Exception e) {
                sendErrorResponse(resp, "Failed to load users");
            }
        } else if (pathInfo.startsWith("/users/api/")) {
            // Get specific user by ID
            String userIdStr = pathInfo.substring("/users/api/".length());
            try {
                Long userId = Long.parseLong(userIdStr);
                UserDTO user = userService.getUserById(userId);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("user", user);
                sendJsonResponse(resp, response);
            } catch (NumberFormatException e) {
                sendErrorResponse(resp, "Invalid user ID");
            } catch (IllegalArgumentException e) {
                sendErrorResponse(resp, "User not found");
            }
        }
    }
    
    private void saveUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON request body
            StringBuilder jsonBody = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }
            
            // Parse JSON to Map
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = gson.fromJson(jsonBody.toString(), Map.class);
            
            // Validation
            String fullName = (String) userData.get("fullName");
            String username = (String) userData.get("username");
            String email = (String) userData.get("email");
            String password = (String) userData.get("password");
            
            if (fullName == null || fullName.trim().isEmpty()) {
                sendErrorResponse(resp, "Full name is required");
                return;
            }
            
            if (username == null || username.trim().isEmpty()) {
                sendErrorResponse(resp, "Username is required");
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                sendErrorResponse(resp, "Email is required");
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                sendErrorResponse(resp, "Password is required");
                return;
            }
            
            // Create user using UserService
            UserDTO newUser = userService.registerUser(username, email, password, fullName);
            
            // Update additional fields
            String phone = (String) userData.get("phone");
            String address = (String) userData.get("address");
            String profileImageUrl = (String) userData.get("profileImageUrl");
            
            if (phone != null || address != null || profileImageUrl != null) {
                userService.updateUserProfile(newUser.getId(), null, phone, address, profileImageUrl);
                // Reload user with updated profile
                newUser = userService.getUserById(newUser.getId());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User created successfully");
            response.put("user", newUser);
            
            sendJsonResponse(resp, response);
            
        } catch (IllegalArgumentException e) {
            sendErrorResponse(resp, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to save user: " + e.getMessage());
        }
    }
    
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON request body
            StringBuilder jsonBody = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }
            
            // Parse JSON to Map
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = gson.fromJson(jsonBody.toString(), Map.class);
            
            String userIdStr = (String) userData.get("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                sendErrorResponse(resp, "User ID is required");
                return;
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // Update user profile
            String fullName = (String) userData.get("fullName");
            String phone = (String) userData.get("phone");
            String address = (String) userData.get("address");
            String profileImageUrl = (String) userData.get("profileImageUrl");
            
            UserDTO updatedUser = userService.updateUserProfile(userId, fullName, phone, address, profileImageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("user", updatedUser);
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid user ID");
        } catch (IllegalArgumentException e) {
            sendErrorResponse(resp, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to update user: " + e.getMessage());
        }
    }
    
    private void deleteUserFromAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON request body
            StringBuilder jsonBody = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }
            
            // Parse JSON to Map
            @SuppressWarnings("unchecked")
            Map<String, Object> requestData = gson.fromJson(jsonBody.toString(), Map.class);
            
            String userIdStr = (String) requestData.get("id");
            
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                sendErrorResponse(resp, "User ID is required");
                return;
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            boolean deleted = userService.deleteUser(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", deleted);
            response.put("message", deleted ? "User deleted successfully" : "Failed to delete user");
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid user ID");
        } catch (IllegalArgumentException e) {
            sendErrorResponse(resp, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to delete user: " + e.getMessage());
        }
    }
    
    private void toggleUserStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON request body
            StringBuilder jsonBody = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBody.append(line);
                }
            }
            
            // Parse JSON to Map
            @SuppressWarnings("unchecked")
            Map<String, Object> requestData = gson.fromJson(jsonBody.toString(), Map.class);
            
            String userIdStr = (String) requestData.get("id");
            Boolean active = (Boolean) requestData.get("active");
            
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                sendErrorResponse(resp, "User ID is required");
                return;
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            boolean success;
            String action;
            if (Boolean.TRUE.equals(active)) {
                success = userService.activateUser(userId);
                action = "activated";
            } else {
                success = userService.deactivateUser(userId);
                action = "deactivated";
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "User " + action + " successfully" : "Failed to update user status");
            
            sendJsonResponse(resp, response);
            
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, "Invalid user ID");
        } catch (IllegalArgumentException e) {
            sendErrorResponse(resp, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Failed to update user status: " + e.getMessage());
        }
    }
    
    // Mock data helpers - replace with real service calls when UserService is fully implemented
    private List<UserDTO> createMockUsers() {
        List<UserDTO> users = new java.util.ArrayList<>();
        
        UserDTO admin = new UserDTO();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setEmail("admin@phonehub.com");
        admin.setFullName("System Administrator");
        admin.setPhone("+1-555-0001");
        admin.setAddress("123 Admin Street, Tech City");
        admin.setRole("ADMIN");
        admin.setAuthProvider("LOCAL");
        admin.setActive(true);
        admin.setCreatedAt(java.time.LocalDateTime.now().minusDays(30));
        users.add(admin);
        
        UserDTO manager = new UserDTO();
        manager.setId(2L);
        manager.setUsername("manager");
        manager.setEmail("manager@phonehub.com");
        manager.setFullName("Store Manager");
        manager.setPhone("+1-555-0002");
        manager.setAddress("456 Manager Ave, Business District");
        manager.setRole("MANAGER");
        manager.setAuthProvider("LOCAL");
        manager.setActive(true);
        manager.setCreatedAt(java.time.LocalDateTime.now().minusDays(20));
        users.add(manager);
        
        UserDTO sales = new UserDTO();
        sales.setId(3L);
        sales.setUsername("salesrep");
        sales.setEmail("sales@phonehub.com");
        sales.setFullName("Sales Representative");
        sales.setPhone("+1-555-0003");
        sales.setAddress("789 Sales Blvd, Commerce Center");
        sales.setRole("SALES");
        sales.setAuthProvider("LOCAL");
        sales.setActive(true);
        sales.setCreatedAt(java.time.LocalDateTime.now().minusDays(15));
        users.add(sales);
        
        UserDTO customer = new UserDTO();
        customer.setId(4L);
        customer.setUsername("johndoe");
        customer.setEmail("john.doe@email.com");
        customer.setFullName("John Doe");
        customer.setPhone("+1-555-0004");
        customer.setAddress("321 Customer Lane, Residential Area");
        customer.setRole("CUSTOMER");
        customer.setAuthProvider("GOOGLE");
        customer.setActive(true);
        customer.setCreatedAt(java.time.LocalDateTime.now().minusDays(5));
        users.add(customer);
        
        UserDTO customer2 = new UserDTO();
        customer2.setId(5L);
        customer2.setUsername("janesmith");
        customer2.setEmail("jane.smith@email.com");
        customer2.setFullName("Jane Smith");
        customer2.setPhone("+1-555-0005");
        customer2.setAddress("654 Customer Drive, Suburban Area");
        customer2.setRole("CUSTOMER");
        customer2.setAuthProvider("LOCAL");
        customer2.setActive(false);
        customer2.setCreatedAt(java.time.LocalDateTime.now().minusDays(2));
        users.add(customer2);
        
        return users;
    }
    
    /**
     * Helper method to extract value from multipart form data
     */
    private String getPartValue(HttpServletRequest req, String partName) {
        try {
            Part part = req.getPart(partName);
            if (part == null) {
                return null;
            }
            
            // Read the part content as string
            java.util.Scanner scanner = new java.util.Scanner(part.getInputStream(), "UTF-8");
            String value = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
            scanner.close();
            
            return value.trim().isEmpty() ? null : value.trim();
        } catch (Exception e) {
            System.err.println("Error extracting part '" + partName + "': " + e.getMessage());
            return null;
        }
    }
    
    private UserDTO getMockUserById(Long id) {
        return createMockUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
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
