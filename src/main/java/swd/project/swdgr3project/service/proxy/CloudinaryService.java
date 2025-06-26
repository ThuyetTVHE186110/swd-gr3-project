package swd.project.swdgr3project.service.proxy;

import java.util.List;

/**
 * Service interface for Cloudinary image operations.
 * This service acts as a proxy to the Cloudinary API.
 */
public interface CloudinaryService {
    
    /**
     * Upload an image to Cloudinary.
     *
     * @param base64Image The base64-encoded image data
     * @param folder The folder to upload to (optional)
     * @return The URL of the uploaded image
     * @throws Exception if upload fails
     */
    String uploadImage(String base64Image, String folder) throws Exception;
    
    /**
     * Upload multiple images to Cloudinary.
     *
     * @param base64Images List of base64-encoded image data
     * @param folder The folder to upload to (optional)
     * @return List of URLs of the uploaded images
     * @throws Exception if upload fails
     */
    List<String> uploadImages(List<String> base64Images, String folder) throws Exception;
    
    /**
     * Delete an image from Cloudinary.
     *
     * @param imageUrl The URL of the image to delete
     * @return true if the image was deleted successfully
     * @throws Exception if deletion fails
     */
    boolean deleteImage(String imageUrl) throws Exception;
    
    /**
     * Generate a transformation URL for an image.
     *
     * @param imageUrl The original image URL
     * @param width The desired width
     * @param height The desired height
     * @param crop The crop mode (e.g., "fill", "crop")
     * @return The transformed image URL
     */
    String generateTransformationUrl(String imageUrl, int width, int height, String crop);
}