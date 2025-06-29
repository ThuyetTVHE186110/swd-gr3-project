<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Database Management - PhoneHub Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            <style>
                .db-management {
                    max-width: 800px;
                    margin: 40px auto;
                    padding: 20px;
                }

                .action-card {
                    background: white;
                    border-radius: 12px;
                    padding: 24px;
                    margin-bottom: 20px;
                    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
                    border-left: 4px solid var(--primary-color);
                }

                .action-card h3 {
                    color: var(--primary-color);
                    margin-bottom: 12px;
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }

                .action-card p {
                    color: #666;
                    margin-bottom: 20px;
                    line-height: 1.6;
                }

                .action-buttons {
                    display: flex;
                    gap: 12px;
                    flex-wrap: wrap;
                }

                .btn-primary {
                    background: var(--primary-color);
                    color: white;
                    border: none;
                    padding: 12px 24px;
                    border-radius: 8px;
                    cursor: pointer;
                    text-decoration: none;
                    display: inline-flex;
                    align-items: center;
                    gap: 8px;
                    font-weight: 500;
                    transition: all 0.2s ease;
                }

                .btn-primary:hover {
                    background: #0056b3;
                    transform: translateY(-1px);
                }

                .btn-warning {
                    background: #ffc107;
                    color: #212529;
                }

                .btn-warning:hover {
                    background: #e0a800;
                }

                .btn-danger {
                    background: #dc3545;
                    color: white;
                }

                .btn-danger:hover {
                    background: #c82333;
                }

                .status-info {
                    background: #e3f2fd;
                    border: 1px solid #2196f3;
                    color: #1976d2;
                    padding: 15px;
                    border-radius: 8px;
                    margin-bottom: 20px;
                }

                .warning-box {
                    background: #fff3cd;
                    border: 1px solid #ffc107;
                    color: #856404;
                    padding: 15px;
                    border-radius: 8px;
                    margin-bottom: 20px;
                }
            </style>
        </head>

        <body>
            <header class="header">
                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="logo">
                        <i class="fas fa-mobile-alt"></i> PhoneHub Admin
                    </a>
                    <ul class="nav-menu">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Dashboard</a>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Products</a>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories"
                                class="nav-link">Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/database"
                                class="nav-link active">Database</a></li>
                    </ul>
                </nav>
            </header>

            <main class="db-management">
                <h1><i class="fas fa-database"></i> Database Management</h1>

                <div class="status-info">
                    <strong><i class="fas fa-info-circle"></i> Database Status:</strong>
                    Use these tools to manage your database content and structure.
                </div>

                <div class="action-card">
                    <h3><i class="fas fa-seedling"></i> Seed Database</h3>
                    <p>
                        Populate the database with sample data including product categories, products, and user
                        accounts.
                        This is useful for testing and initial setup. The seeding process will skip items that already
                        exist.
                    </p>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/init-data" class="btn-primary">
                            <i class="fas fa-play"></i> Seed Database
                        </a>
                    </div>
                </div>

                <div class="action-card">
                    <h3><i class="fas fa-eye"></i> Database Statistics</h3>
                    <p>
                        View current database statistics including number of products, categories, users, and orders.
                        This helps you understand the current state of your data.
                    </p>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/stats" class="btn-primary">
                            <i class="fas fa-chart-bar"></i> View Statistics
                        </a>
                    </div>
                </div>

                <div class="action-card">
                    <h3><i class="fas fa-download"></i> Backup & Export</h3>
                    <p>
                        Export your data for backup purposes or migration to another system.
                        Choose from various export formats.
                    </p>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/export" class="btn-primary">
                            <i class="fas fa-download"></i> Export Data
                        </a>
                    </div>
                </div>

                <div class="warning-box">
                    <strong><i class="fas fa-exclamation-triangle"></i> Warning:</strong>
                    Database operations can affect your live data. Always backup your data before performing bulk
                    operations.
                </div>

                <div class="action-card">
                    <h3><i class="fas fa-broom"></i> Database Cleanup</h3>
                    <p>
                        Clean up orphaned records, reset demo data, or clear test data.
                        <strong>Use with caution in production environments.</strong>
                    </p>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/cleanup" class="btn-warning">
                            <i class="fas fa-broom"></i> Cleanup Data
                        </a>
                    </div>
                </div>

                <div class="action-card">
                    <h3><i class="fas fa-tools"></i> Advanced Tools</h3>
                    <p>
                        Advanced database tools for developers and administrators. Includes schema validation,
                        performance analysis, and maintenance operations.
                    </p>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/tools" class="btn-primary">
                            <i class="fas fa-tools"></i> Open Tools
                        </a>
                    </div>
                </div>

                <div style="margin-top: 40px; text-align: center;">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-primary">
                        <i class="fas fa-arrow-left"></i> Back to Dashboard
                    </a>
                </div>
            </main>
        </body>

        </html>