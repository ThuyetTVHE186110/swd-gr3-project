package swd.project.swdgr3project.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import swd.project.swdgr3project.entity.User;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Database seeder utility to populate the database with initial data for a phone device shop.
 */
@Slf4j
public class DatabaseSeeder {

    /**
     * Seeds the database with initial data.
     * This method should be called once during application startup.
     */
    public static void seedDatabase() {
        log.info("Starting database seeding process...");

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                // Seed users
                seedUsers(session);

                // Seed other entities
                seedProductCategories(session);
                seedProducts(session);
                // Add more seeding methods as needed

                transaction.commit();
                log.info("Database seeding completed successfully");
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Error during database seeding: {}", e.getMessage(), e);
                throw new RuntimeException("Database seeding failed", e);
            }
        }
    }

    private static void seedUsers(Session session) {
        try {
            // Check if users already exist to prevent duplicate seeding
            Long userCount = (Long) session.createQuery("SELECT COUNT(u) FROM User u").uniqueResult();

            if (userCount == 0) {
                log.info("Seeding users...");

                // Admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@phonestore.com");
                admin.setPassword("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"); // "password" encrypted
                admin.setFullName("Admin");
                admin.setRole("ADMIN");
                admin.setCreatedAt(LocalDateTime.now());
                admin.setActive(true);
                session.persist(admin);

                // Store staff user
                User staff = new User();
                staff.setUsername("staff");
                staff.setEmail("staff@phonestore.com");
                staff.setPassword("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG");
                staff.setFullName("Staff");
                staff.setRole("STAFF");
                staff.setCreatedAt(LocalDateTime.now());
                staff.setActive(true);
                session.persist(staff);

                // Regular customer
                User customer = new User();
                customer.setUsername("customer");
                customer.setEmail("customer@example.com");
                customer.setPassword("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG");
                customer.setFullName("John");
                customer.setRole("CUSTOMER");
                customer.setCreatedAt(LocalDateTime.now());
                customer.setActive(true);
                session.persist(customer);

                log.info("User seeding completed");
            } else {
                log.info("Users already seeded, skipping...");
            }
        } catch (Exception e) {
            log.warn("Error seeding users: {}. Continuing with other seeds.", e.getMessage());
        }
    }

    private static void seedProductCategories(Session session) {
        try {
            // Check if categories already exist
            Long categoryCount = (Long) session.createQuery("SELECT COUNT(c) FROM ProductCategory c").uniqueResult();

            if (categoryCount == 0) {
                log.info("Seeding product categories...");

                // Phone-specific categories
                List<String[]> categories = Arrays.asList(
                    new String[]{"Smartphones", "Modern touchscreen mobile phones"},
                    new String[]{"Feature Phones", "Basic mobile phones with physical keypads"},
                    new String[]{"Phone Accessories", "Cases, screen protectors, and other accessories"},
                    new String[]{"Chargers & Cables", "Power adapters and charging cables"},
                    new String[]{"Headphones & Audio", "Earphones, headphones, and speakers"},
                    new String[]{"Batteries & Power Banks", "Replacement batteries and portable chargers"},
                    new String[]{"Memory & Storage", "Memory cards and storage solutions for mobile devices"}
                );

                for (String[] category : categories) {
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setName(category[0]);
                    productCategory.setDescription(category[1]);
                    session.persist(productCategory);
                }

                log.info("Product category seeding completed");
            } else {
                log.info("Product categories already seeded, skipping...");
            }
        } catch (Exception e) {
            log.warn("Error seeding product categories: {}. Continuing with other seeds.", e.getMessage());
        }
    }

    private static void seedProducts(Session session) {
        try {
            // Check if products already exist
            Long productCount = (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();

            if (productCount == 0) {
                log.info("Seeding products...");

                // Smartphones
                ProductCategory smartphones = getProductCategory(session, "Smartphones");
                if (smartphones != null) {
                    // iPhone models
                    seedProduct(session, "iPhone 15 Pro", "Apple's flagship smartphone with A17 Pro chip, 6.1-inch Super Retina XDR display, and pro-grade camera system", 
                              new BigDecimal("1199.99"), 35, smartphones, "/images/products/iphone15pro.jpg");
                    
                    seedProduct(session, "iPhone 15", "6.1-inch Super Retina XDR display, A16 Bionic chip, and Dynamic Island", 
                              new BigDecimal("899.99"), 50, smartphones, "/images/products/iphone15.jpg");
                    
                    seedProduct(session, "iPhone 14", "6.1-inch Super Retina XDR display, A15 Bionic chip, and advanced camera system", 
                              new BigDecimal("699.99"), 65, smartphones, "/images/products/iphone14.jpg");
                    
                    // Samsung models
                    seedProduct(session, "Samsung Galaxy S24 Ultra", "Samsung's premium smartphone with Snapdragon 8 Gen 3, 6.8-inch Dynamic AMOLED 2X display, and 200MP camera", 
                              new BigDecimal("1199.99"), 30, smartphones, "/images/products/galaxys24ultra.jpg");
                    
                    seedProduct(session, "Samsung Galaxy S24", "6.2-inch Dynamic AMOLED 2X display, Snapdragon 8 Gen 3, and AI-powered features", 
                              new BigDecimal("899.99"), 45, smartphones, "/images/products/galaxys24.jpg");
                    
                    seedProduct(session, "Samsung Galaxy A54", "6.4-inch Super AMOLED display, 5G connectivity, and 50MP main camera", 
                              new BigDecimal("449.99"), 80, smartphones, "/images/products/galaxya54.jpg");
                    
                    // Google models
                    seedProduct(session, "Google Pixel 8 Pro", "Google's flagship with 6.7-inch Super Actua display, Tensor G3 chip, and advanced AI features", 
                              new BigDecimal("999.99"), 25, smartphones, "/images/products/pixel8pro.jpg");
                    
                    seedProduct(session, "Google Pixel 8", "6.2-inch Actua display, Tensor G3 chip, and computational photography features", 
                              new BigDecimal("699.99"), 40, smartphones, "/images/products/pixel8.jpg");
                    
                    // Xiaomi models
                    seedProduct(session, "Xiaomi 14 Ultra", "6.73-inch AMOLED display, Snapdragon 8 Gen 3, and Leica quad-camera system", 
                              new BigDecimal("899.99"), 20, smartphones, "/images/products/xiaomi14ultra.jpg");
                    
                    seedProduct(session, "Xiaomi Redmi Note 13 Pro", "6.67-inch AMOLED display, 120Hz refresh rate, and 200MP camera", 
                              new BigDecimal("329.99"), 60, smartphones, "/images/products/redminote13pro.jpg");
                }

                // Feature Phones
                ProductCategory featurePhones = getProductCategory(session, "Feature Phones");
                if (featurePhones != null) {
                    seedProduct(session, "Nokia 3310 (2023)", "Reimagined classic with long battery life, durable design, and basic mobile functionality", 
                              new BigDecimal("69.99"), 100, featurePhones, "/images/products/nokia3310.jpg");
                    
                    seedProduct(session, "Nokia 8210 4G", "Color display, 4G connectivity, and classic Nokia durability", 
                              new BigDecimal("79.99"), 75, featurePhones, "/images/products/nokia8210.jpg");
                }

                // Phone Accessories
                ProductCategory accessories = getProductCategory(session, "Phone Accessories");
                if (accessories != null) {
                    seedProduct(session, "iPhone 15 Pro Silicone Case", "Official Apple silicone case with MagSafe compatibility", 
                              new BigDecimal("49.99"), 120, accessories, "/images/products/iphone15case.jpg");
                    
                    seedProduct(session, "Samsung Galaxy S24 Ultra Clear Case", "Official Samsung transparent case with reinforced corners", 
                              new BigDecimal("29.99"), 100, accessories, "/images/products/s24ultracase.jpg");
                    
                    seedProduct(session, "Tempered Glass Screen Protector", "9H hardness tempered glass protector with edge-to-edge coverage", 
                              new BigDecimal("14.99"), 200, accessories, "/images/products/screenprotector.jpg");
                    
                    seedProduct(session, "Phone Grip Stand", "Collapsible grip and stand for secure handling and convenient viewing", 
                              new BigDecimal("9.99"), 150, accessories, "/images/products/phonestand.jpg");
                }

                // Chargers & Cables
                ProductCategory chargers = getProductCategory(session, "Chargers & Cables");
                if (chargers != null) {
                    seedProduct(session, "Apple 20W USB-C Power Adapter", "Fast-charging adapter for iPhone and iPad", 
                              new BigDecimal("19.99"), 80, chargers, "/images/products/applecharger.jpg");
                    
                    seedProduct(session, "Samsung 45W Super Fast Charger", "Official Samsung 45W charger with USB-C cable", 
                              new BigDecimal("39.99"), 60, chargers, "/images/products/samsungcharger.jpg");
                    
                    seedProduct(session, "USB-C to Lightning Cable (2m)", "MFi-certified cable for charging iPhones", 
                              new BigDecimal("19.99"), 100, chargers, "/images/products/usbc-lightning.jpg");
                    
                    seedProduct(session, "USB-C to USB-C Cable (2m)", "100W fast-charging cable for Android devices", 
                              new BigDecimal("14.99"), 120, chargers, "/images/products/usbc-usbc.jpg");
                }

                // Headphones & Audio
                ProductCategory audio = getProductCategory(session, "Headphones & Audio");
                if (audio != null) {
                    seedProduct(session, "Apple AirPods Pro 2", "Wireless earbuds with active noise cancellation and spatial audio", 
                              new BigDecimal("249.99"), 40, audio, "/images/products/airpodspro.jpg");
                    
                    seedProduct(session, "Samsung Galaxy Buds 3 Pro", "Wireless earbuds with ANC and 360 Audio", 
                              new BigDecimal("199.99"), 35, audio, "/images/products/galaxybuds.jpg");
                    
                    seedProduct(session, "Bluetooth Speaker", "Portable Bluetooth 5.2 speaker with 24-hour battery life", 
                              new BigDecimal("59.99"), 50, audio, "/images/products/btspeaker.jpg");
                }

                // Batteries & Power Banks
                ProductCategory batteries = getProductCategory(session, "Batteries & Power Banks");
                if (batteries != null) {
                    seedProduct(session, "20000mAh Power Bank", "High-capacity power bank with dual USB-A and USB-C outputs", 
                              new BigDecimal("49.99"), 70, batteries, "/images/products/powerbank.jpg");
                    
                    seedProduct(session, "MagSafe Battery Pack", "Magnetic battery pack for iPhone with MagSafe", 
                              new BigDecimal("99.99"), 40, batteries, "/images/products/magsafebattery.jpg");
                    
                    seedProduct(session, "Replacement Battery for Samsung Galaxy S22", "Original capacity replacement battery", 
                              new BigDecimal("39.99"), 30, batteries, "/images/products/s22battery.jpg");
                }

                // Memory & Storage
                ProductCategory memory = getProductCategory(session, "Memory & Storage");
                if (memory != null) {
                    seedProduct(session, "SanDisk 128GB microSD Card", "Class 10 microSD card with adapter", 
                              new BigDecimal("24.99"), 85, memory, "/images/products/microsd.jpg");
                    
                    seedProduct(session, "512GB USB-C Flash Drive", "High-speed USB-C flash drive for phones and computers", 
                              new BigDecimal("59.99"), 45, memory, "/images/products/flashdrive.jpg");
                }

                log.info("Product seeding completed");
            } else {
                log.info("Products already seeded, skipping...");
            }
        } catch (Exception e) {
            log.warn("Error seeding products: {}. Continuing with other seeds.", e.getMessage());
        }
    }

    /**
     * Helper method to create and persist a product
     */
    private static void seedProduct(Session session, String name, String description, BigDecimal price, 
                                  int stockQuantity, ProductCategory category, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stockQuantity);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setCreatedAt(LocalDateTime.now());
        session.persist(product);
    }

    /**
     * Helper method to get a product category by name
     */
    private static ProductCategory getProductCategory(Session session, String name) {
        try {
            return (ProductCategory) session.createQuery("FROM ProductCategory WHERE name = :name")
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            log.error("Error retrieving product category '{}': {}", name, e.getMessage());
            return null;
        }
    }
}