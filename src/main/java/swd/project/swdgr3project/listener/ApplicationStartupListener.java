package swd.project.swdgr3project.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import swd.project.swdgr3project.utils.DatabaseSeeder;

@WebListener(value = "ApplicationStartupListener")
public class ApplicationStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize Hibernate
        try {
            // Seed the database with initial data
            DatabaseSeeder.seedDatabase();
            System.out.println("Database initialization completed successfully");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed
    }
}
