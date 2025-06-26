package swd.project.swdgr3project.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Database seeder utility to populate the database with initial data.
 * This class uses the Hibernate SessionFactory from HibernateUtils.
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
                seedCategories(session);
                seedProducts(session);
                // Add more seeding methods as needed

                transaction.commit();
                log.info("Database seeding completed successfully");
            } catch (Exception e) {
                transaction.rollback();
                log.error("Error during database seeding: {}", e.getMessage(), e);
                throw new RuntimeException("Database seeding failed", e);
            }
        }
    }

    private static void seedUsers(Session session) {
        // Check if users already exist to prevent duplicate seeding
        Long userCount = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();

        if (userCount == 0) {
            log.info("Seeding users...");

            // Create and persist user entities
            // Example:
            // User admin = new User("admin", "admin@example.com", passwordEncoder.encode("password"));
            // session.persist(admin);
        } else {
            log.info("Users already seeded, skipping...");
        }
    }

    private static void seedCategories(Session session) {
        // Check if categories already exist
        Long categoryCount = session.createQuery("SELECT COUNT(c) FROM Category c", Long.class).getSingleResult();

        if (categoryCount == 0) {
            log.info("Seeding categories...");

            // Create and persist category entities
            // Example:
            // Category electronics = new Category("Electronics", "Electronic devices and accessories");
            // session.persist(electronics);
        } else {
            log.info("Categories already seeded, skipping...");
        }
    }

    private static void seedProducts(Session session) {
        // Check if products already exist
        Long productCount = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();

        if (productCount == 0) {
            log.info("Seeding products...");

            // Create and persist product entities
            // Example:
            // Product laptop = new Product("Laptop", "High-performance laptop", 999.99, getCategory(session, "Electronics"));
            // session.persist(laptop);
        } else {
            log.info("Products already seeded, skipping...");
        }
    }

    /**
     * Helper method to get a category by name
     */
    private static Object getCategory(Session session, String name) {
        // Adjust the query to match your entity and field names
        return session.createQuery("FROM Category WHERE name = :name")
                .setParameter("name", name)
                .getSingleResult();
    }
}