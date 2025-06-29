package swd.project.swdgr3project.util;

import java.util.logging.Logger;

/**
 * Utility class to test and validate Cloudinary setup
 */
public class CloudinaryTestUtil {
    private static final Logger LOGGER = Logger.getLogger(CloudinaryTestUtil.class.getName());
    
    /**
     * Test if Cloudinary is properly configured
     * @return true if configuration is valid
     */
    public static boolean testConfiguration() {
        try {
            LOGGER.info("Testing Cloudinary configuration...");
            
            if (!CloudinaryUtil.isConfigured()) {
                LOGGER.severe("Cloudinary is not configured properly");
                return false;
            }
            
            LOGGER.info("Cloudinary configuration test passed!");
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Cloudinary configuration test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Print configuration status and setup instructions
     */
    public static void printSetupInstructions() {
        System.out.println("=== Cloudinary Setup Instructions ===");
        System.out.println("1. Create a free Cloudinary account at https://cloudinary.com");
        System.out.println("2. Get your credentials from the dashboard:");
        System.out.println("   - Cloud Name");
        System.out.println("   - API Key");
        System.out.println("   - API Secret");
        System.out.println("3. Set environment variables (recommended):");
        System.out.println("   CLOUDINARY_CLOUD_NAME=your-cloud-name");
        System.out.println("   CLOUDINARY_API_KEY=your-api-key");
        System.out.println("   CLOUDINARY_API_SECRET=your-api-secret");
        System.out.println("4. OR update src/main/resources/cloudinary.properties");
        System.out.println("5. Restart your application");
        System.out.println("=====================================");
    }
}
