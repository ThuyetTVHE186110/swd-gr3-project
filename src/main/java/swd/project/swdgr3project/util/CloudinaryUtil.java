package swd.project.swdgr3project.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for Cloudinary image upload operations
 */
public class CloudinaryUtil {
    private static final Logger LOGGER = Logger.getLogger(CloudinaryUtil.class.getName());
    private static Cloudinary cloudinary;
    private static boolean initializationFailed = false;
    private static String initializationError = null;
    
    // Private constructor to hide the implicit public one
    private CloudinaryUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    static {
        try {
            initializeCloudinary();
        } catch (Exception e) {
            initializationFailed = true;
            initializationError = e.getMessage();
            LOGGER.log(Level.SEVERE, e, () -> "Failed to initialize Cloudinary: " + e.getMessage());
        }
    }
    
    private static void initializeCloudinary() throws IOException {
        Properties props = new Properties();
        props.load(CloudinaryUtil.class.getClassLoader().getResourceAsStream("cloudinary.properties"));
        
        // Try environment variables first, then fall back to properties file
        String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        String apiKey = System.getenv("CLOUDINARY_API_KEY");
        String apiSecret = System.getenv("CLOUDINARY_API_SECRET");
        
        if (cloudName == null) cloudName = props.getProperty("cloudinary.cloud_name");
        if (apiKey == null) apiKey = props.getProperty("cloudinary.api_key");
        if (apiSecret == null) apiSecret = props.getProperty("cloudinary.api_secret");
        
        if (cloudName == null || apiKey == null || apiSecret == null) {
            throw new IllegalStateException("Cloudinary credentials not properly configured. " +
                "Please set environment variables or update cloudinary.properties file.");
        }
        
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
        
        LOGGER.log(Level.INFO, "Cloudinary initialized successfully with cloud name: {0}", cloudName);
    }
    
    /**
     * Upload an image file to Cloudinary
     * @param filePart The uploaded file part from servlet
     * @param folder The folder to upload to (e.g., "products", "users")
     * @return The secure URL of the uploaded image
     * @throws IOException If upload fails
     */
    public static String uploadImage(Part filePart, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("File part is null or empty");
        }
        
        if (!isConfigured()) {
            throw new IOException("Cloudinary is not properly configured. " + 
                (initializationError != null ? "Error: " + initializationError : 
                "Please check your configuration."));
        }
        
        try {
            LOGGER.info("Uploading image to Cloudinary. File size: " + filePart.getSize() + 
                       " bytes, Content type: " + filePart.getContentType());
            
            // Create a temporary file for the upload
            java.io.File tempFile = java.io.File.createTempFile("upload_", "_temp");
            tempFile.deleteOnExit();
            
            // Write the Part content to the temporary file
            try (java.io.InputStream inputStream = filePart.getInputStream();
                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
                inputStream.transferTo(outputStream);
            }
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", "phonehub/" + folder,
                "resource_type", "image",
                "quality", "auto:good",
                "fetch_format", "auto",
                "transformation", ObjectUtils.asMap(
                    "width", 800,
                    "height", 800,
                    "crop", "limit"
                )
            );
            
            // Upload using the temporary file
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(tempFile, uploadParams);
            String secureUrl = (String) uploadResult.get("secure_url");
            
            // Clean up the temporary file
            cleanupTempFile(tempFile);
            
            LOGGER.log(Level.INFO, "Image uploaded successfully to: {0}", secureUrl);
            return secureUrl;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to upload image to Cloudinary", e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Upload image from byte array
     */
    public static String uploadImage(byte[] imageBytes, String publicId, String folder) throws IOException {
        if (initializationFailed) {
            throw new IOException("Cloudinary not properly configured: " + initializationError);
        }
        
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", folder,
                "overwrite", true,
                "resource_type", "image"
            );
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(imageBytes, params);
            return (String) result.get("secure_url");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to upload image bytes to Cloudinary", e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete an image from Cloudinary using its public ID
     * @param imageUrl The full URL of the image to delete
     * @return true if deletion was successful
     */
    public static boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Extract public ID from URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId == null) {
                LOGGER.log(Level.WARNING, "Could not extract public ID from URL: {0}", imageUrl);
                return false;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            
            LOGGER.log(Level.INFO, "Delete image result for {0}: {1}", new Object[]{publicId, resultStatus});
            return "ok".equals(resultStatus);
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to delete image: " + imageUrl);
            return false;
        }
    }
    
    /**
     * Extract the public ID from a Cloudinary URL (simplified version)
     * @param url The Cloudinary image URL
     * @return The public ID or null if extraction fails
     */
    private static String extractPublicIdFromUrl(String url) {
        try {
            if (!url.contains("cloudinary.com") || !url.contains("/upload/")) {
                return null;
            }
            
            String[] parts = url.split("/");
            int uploadIndex = findUploadIndex(parts);
            if (uploadIndex == -1) {
                return null;
            }
            
            return buildPublicIdFromParts(parts, uploadIndex);
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e, () -> "Error extracting public ID from URL: " + url);
            return null;
        }
    }
    
    private static int findUploadIndex(String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            if ("upload".equals(parts[i])) {
                return i;
            }
        }
        return -1;
    }
    
    private static String buildPublicIdFromParts(String[] parts, int uploadIndex) {
        StringBuilder publicId = new StringBuilder();
        
        for (int i = uploadIndex + 1; i < parts.length; i++) {
            String part = parts[i];
            
            // Skip version numbers
            if (part.matches("^v\\d+$")) {
                continue;
            }
            
            if (publicId.length() > 0) {
                publicId.append("/");
            }
            
            // Remove file extension from the last part
            if (i == parts.length - 1 && part.contains(".")) {
                part = part.substring(0, part.lastIndexOf("."));
            }
            
            publicId.append(part);
        }
        
        return publicId.length() > 0 ? publicId.toString() : null;
    }
    
    /**
     * Extract public ID from Cloudinary URL
     */
    public static String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || !cloudinaryUrl.contains("cloudinary.com")) {
            return null;
        }
        
        try {
            // Extract the public ID from the URL
            String[] urlParts = cloudinaryUrl.split("/");
            if (urlParts.length >= 7) {
                String publicIdWithFormat = urlParts[urlParts.length - 1];
                // Remove file extension
                int dotIndex = publicIdWithFormat.lastIndexOf('.');
                if (dotIndex > 0) {
                    return publicIdWithFormat.substring(0, dotIndex);
                }
                return publicIdWithFormat;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to extract public ID from URL: " + cloudinaryUrl);
        }
        
        return null;
    }
    
    /**
     * Get optimized image URL with specific dimensions
     */
    public static String getOptimizedProductImageUrl(String publicId, int width, int height) {
        if (initializationFailed || publicId == null) {
            return null;
        }
        
        try {
            // Create transformation using the Transformation class
            Transformation transformation = new Transformation()
                .width(width)
                .height(height)
                .crop("fill")
                .quality("auto:good")
                .fetchFormat("auto");
            
            return cloudinary.url()
                .transformation(transformation)
                .generate(publicId);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, () -> "Failed to generate optimized URL for public ID: " + publicId);
            return null;
        }
    }
    
    /**
     * Validate if the uploaded file is a valid image
     * @param filePart The uploaded file part
     * @return true if it's a valid image
     */
    public static boolean isValidImage(Part filePart) {
        if (filePart == null) {
            return false;
        }
        
        String contentType = filePart.getContentType();
        if (contentType == null) {
            return false;
        }
        
        // Check content type
        if (!contentType.startsWith("image/")) {
            return false;
        }
        
        // Check allowed formats
        String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
        for (String allowedType : allowedTypes) {
            if (contentType.equalsIgnoreCase(allowedType)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if Cloudinary is properly configured
     * @return true if configured
     */
    public static boolean isConfigured() {
        return cloudinary != null && !initializationFailed;
    }
    
    /**
     * Get the initialization error message if any
     * @return error message or null if no error
     */
    public static String getInitializationError() {
        return initializationError;
    }
    
    /**
     * Helper method to clean up temporary files
     */
    private static void cleanupTempFile(java.io.File tempFile) {
        try {
            java.nio.file.Files.deleteIfExists(tempFile.toPath());
        } catch (Exception deleteException) {
            LOGGER.log(Level.WARNING, deleteException, () -> "Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }
    }
}
