package swd.project.swdgr3project.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import swd.project.swdgr3project.utils.HibernateUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet(urlPatterns = {"/admin/stats"})
public class DatabaseStatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Long> stats = getDatabaseStats();
            
            resp.setContentType("text/html");
            resp.getWriter().write(generateStatsHTML(stats));
            
        } catch (Exception e) {
            log.error("Error retrieving database statistics", e);
            resp.setContentType("text/html");
            resp.getWriter().write(generateErrorHTML(e.getMessage()));
        }
    }
    
    private Map<String, Long> getDatabaseStats() {
        Map<String, Long> stats = new HashMap<>();
        
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Count products
            Long productCount = (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();
            stats.put("products", productCount != null ? productCount : 0);
            
            // Count active products
            Long activeProductCount = (Long) session.createQuery("SELECT COUNT(p) FROM Product p WHERE p.active = true").uniqueResult();
            stats.put("activeProducts", activeProductCount != null ? activeProductCount : 0);
            
            // Count categories
            Long categoryCount = (Long) session.createQuery("SELECT COUNT(c) FROM ProductCategory c").uniqueResult();
            stats.put("categories", categoryCount != null ? categoryCount : 0);
            
            // Count users
            Long userCount = (Long) session.createQuery("SELECT COUNT(u) FROM User u").uniqueResult();
            stats.put("users", userCount != null ? userCount : 0);
            
            // Count active users
            Long activeUserCount = (Long) session.createQuery("SELECT COUNT(u) FROM User u WHERE u.active = true").uniqueResult();
            stats.put("activeUsers", activeUserCount != null ? activeUserCount : 0);
            
            // Count orders (if Order entity exists)
            try {
                Long orderCount = (Long) session.createQuery("SELECT COUNT(o) FROM Order o").uniqueResult();
                stats.put("orders", orderCount != null ? orderCount : 0);
            } catch (Exception e) {
                stats.put("orders", 0L);
                log.debug("Order entity not found or accessible");
            }
            
        } catch (Exception e) {
            log.error("Error querying database stats", e);
            throw new RuntimeException("Failed to retrieve database statistics", e);
        }
        
        return stats;
    }
    
    private String generateStatsHTML(Map<String, Long> stats) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Database Statistics - PhoneHub Admin</title>
                <link rel="stylesheet" href="%s/css/style.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
                <style>
                    .stats-container {
                        max-width: 1000px;
                        margin: 40px auto;
                        padding: 20px;
                    }
                    .stats-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                        gap: 20px;
                        margin-bottom: 30px;
                    }
                    .stat-card {
                        background: white;
                        padding: 24px;
                        border-radius: 12px;
                        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
                        text-align: center;
                        border-left: 4px solid var(--primary-color);
                    }
                    .stat-number {
                        font-size: 2.5rem;
                        font-weight: bold;
                        color: var(--primary-color);
                        margin-bottom: 8px;
                    }
                    .stat-label {
                        color: #666;
                        font-size: 1.1rem;
                        text-transform: uppercase;
                        letter-spacing: 0.5px;
                    }
                    .stat-icon {
                        font-size: 2rem;
                        color: var(--primary-color);
                        margin-bottom: 16px;
                    }
                    .btn {
                        padding: 12px 24px;
                        background: var(--primary-color);
                        color: white;
                        text-decoration: none;
                        border-radius: 8px;
                        display: inline-flex;
                        align-items: center;
                        gap: 8px;
                        margin: 10px 5px;
                    }
                    .btn:hover {
                        background: #0056b3;
                    }
                </style>
            </head>
            <body>
                <header class="header">
                    <nav class="navbar">
                        <a href="%s/" class="logo">
                            <i class="fas fa-mobile-alt"></i> PhoneHub Admin
                        </a>
                    </nav>
                </header>
                
                <main class="stats-container">
                    <h1><i class="fas fa-chart-bar"></i> Database Statistics</h1>
                    
                    <div class="stats-grid">
                        <div class="stat-card">
                            <i class="fas fa-mobile-alt stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Total Products</div>
                        </div>
                        
                        <div class="stat-card">
                            <i class="fas fa-check-circle stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Active Products</div>
                        </div>
                        
                        <div class="stat-card">
                            <i class="fas fa-tags stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Categories</div>
                        </div>
                        
                        <div class="stat-card">
                            <i class="fas fa-users stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Total Users</div>
                        </div>
                        
                        <div class="stat-card">
                            <i class="fas fa-user-check stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Active Users</div>
                        </div>
                        
                        <div class="stat-card">
                            <i class="fas fa-shopping-cart stat-icon"></i>
                            <div class="stat-number">%d</div>
                            <div class="stat-label">Orders</div>
                        </div>
                    </div>
                    
                    <div style="text-align: center; margin-top: 40px;">
                        <a href="%s/admin/database" class="btn">
                            <i class="fas fa-database"></i> Database Management
                        </a>
                        <a href="%s/admin/dashboard" class="btn">
                            <i class="fas fa-arrow-left"></i> Back to Dashboard
                        </a>
                        <a href="javascript:location.reload()" class="btn">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </a>
                    </div>
                </main>
            </body>
            </html>
            """.formatted(
                "${pageContext.request.contextPath}",
                "${pageContext.request.contextPath}",
                stats.get("products"),
                stats.get("activeProducts"),
                stats.get("categories"),
                stats.get("users"),
                stats.get("activeUsers"),
                stats.get("orders"),
                "${pageContext.request.contextPath}",
                "${pageContext.request.contextPath}"
            );
    }
    
    private String generateErrorHTML(String error) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Database Statistics Error</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; }
                        .error { color: #dc3545; }
                        .btn { padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    </style>
                </head>
                <body>
                    <h1 class="error">❌ Error Loading Statistics</h1>
                    <p>Error: %s</p>
                    <a href="${pageContext.request.contextPath}/admin/database" class="btn">Back to Database Management</a>
                </body>
                </html>
                """.formatted(error);
    }
}