package swd.project.swdgr3project.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import swd.project.swdgr3project.entity.District;
import swd.project.swdgr3project.entity.Product;
import swd.project.swdgr3project.entity.ProductCategory;
import swd.project.swdgr3project.entity.Province;
import swd.project.swdgr3project.entity.Ward;
import swd.project.swdgr3project.entity.CartItem; // Thêm nếu CartItem là Entity

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                // Load hibernate.cfg.xml from classpath (src/main/resources)
                configuration.configure("hibernate.cfg.xml");

                // Add annotated classes (your entities)
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(ProductCategory.class);
                // Thêm các entity địa lý
                configuration.addAnnotatedClass(Province.class);
                configuration.addAnnotatedClass(District.class);
                configuration.addAnnotatedClass(Ward.class);
                // Thêm CartItem nếu nó là một Entity và cần được mapping
                // Theo code bạn cung cấp, CartItem là Entity
                configuration.addAnnotatedClass(CartItem.class);


                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                System.err.println("Initial SessionFactory creation failed." + e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null; // Giải phóng tài nguyên
            System.out.println("Hibernate SessionFactory closed.");
        }
    }
}