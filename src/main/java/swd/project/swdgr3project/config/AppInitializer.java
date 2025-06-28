package swd.project.swdgr3project.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContextListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application starting up... Initializing Hibernate SessionFactory.");
        // Khởi tạo SessionFactory khi ứng dụng bắt đầu
        HibernateConfig.getSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application shutting down... Closing Hibernate SessionFactory.");
        // Đóng SessionFactory khi ứng dụng tắt
        HibernateConfig.shutdown();
    }
}