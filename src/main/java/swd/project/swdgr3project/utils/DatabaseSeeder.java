package swd.project.swdgr3project.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
// SỬA LỖI: Import đúng các Entity class mà bạn đang sử dụng
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.entity.User; // Giả sử bạn có User entity

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

        // Sửa lỗi: Sử dụng HibernateConfig thay vì HibernateUtils nếu đó là tên đúng
        // try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        try (Session session = swd.project.swdgr3project.config.HibernateConfig.getSessionFactory().openSession()) {
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
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Error during database seeding: {}", e.getMessage(), e);
                throw new RuntimeException("Database seeding failed", e);
            }
        }
    }

    private static void seedUsers(Session session) {
        // Kiểm tra xem User entity có được map đúng không
        // SỬA LỖI: Sử dụng tên class 'User' nếu đúng, hoặc tên đúng của entity user của bạn
        Long userCount = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();

        if (userCount == 0) {
            log.info("Seeding users...");

            // Create and persist user entities
            // Ví dụ:
            // User admin = new User();
            // admin.setUsername("admin");
            // admin.setEmail("admin@example.com");
            // admin.setPassword("hashed_password"); // Luôn mã hóa mật khẩu
            // session.persist(admin);
        } else {
            log.info("Users already seeded, skipping...");
        }
    }

    private static void seedCategories(Session session) {
        // SỬA LỖI: Thay thế 'Category' bằng 'ProductCategory'
        Long categoryCount = session.createQuery("SELECT COUNT(c) FROM ProductCategory c", Long.class).getSingleResult();

        if (categoryCount == 0) {
            log.info("Seeding categories...");

            // Create and persist category entities
            // Ví dụ:
            // ProductCategory electronics = new ProductCategory();
            // electronics.setName("Electronics");
            // electronics.setDescription("Electronic devices and accessories");
            // session.persist(electronics);
        } else {
            log.info("Categories already seeded, skipping...");
        }
    }

    private static void seedProducts(Session session) {
        // SỬA LỖI: Thay thế 'Product' bằng tên entity product của bạn (giả sử là Product)
        Long productCount = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();

        if (productCount == 0) {
            log.info("Seeding products...");

            // Create and persist product entities
            // Ví dụ:
            // ProductCategory category = getCategory(session, "Electronics");
            // if (category != null) {
            //     Product laptop = new Product();
            //     laptop.setName("Laptop");
            //     laptop.setDescription("High-performance laptop");
            //     laptop.setPrice(new java.math.BigDecimal("999.99"));
            //     laptop.setCategory(category);
            //     session.persist(laptop);
            // }
        } else {
            log.info("Products already seeded, skipping...");
        }
    }

    /**
     * Helper method to get a category by name
     */
    // SỬA LỖI: Thay thế kiểu trả về và tên entity
    private static ProductCategory getCategory(Session session, String name) {
        // Adjust the query to match your entity and field names
        return session.createQuery("FROM ProductCategory WHERE name = :name", ProductCategory.class)
                .setParameter("name", name)
                .uniqueResult(); // Sử dụng uniqueResult() để an toàn hơn khi có thể không có kết quả
    }
}