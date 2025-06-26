package swd.project.swdgr3project.utils;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.URL;

public class HibernateUtils {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Try to get the configuration file
            URL url = HibernateUtils.class.getClassLoader().getResource("hibernate.cfg.xml");
            if (url == null) {
                System.err.println("Warning: hibernate.cfg.xml not found in classpath!");
                return new Configuration().configure().buildSessionFactory();
            }
            return new Configuration().configure(url).buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            ex.printStackTrace(); // Print the full stack trace for better debugging
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Add a shutdown method to properly close the SessionFactory
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}