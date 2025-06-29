<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Admin Dashboard - PhoneHub</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            </head>

            <body style="margin: 0; background: var(--background-color);">
                <!-- Admin Sidebar -->
                <aside class="admin-sidebar">
                    <div style="padding: 2rem 1rem; border-bottom: 1px solid var(--border-color);">
                        <h2 style="color: var(--primary-color); margin: 0;">
                            <i class="fas fa-mobile-alt"></i>
                            PhoneHub Admin
                        </h2>
                    </div>

                    <nav>
                        <ul class="admin-nav">
                            <li>
                                <a href="${pageContext.request.contextPath}/admin" class="active">
                                    <i class="fas fa-tachometer-alt"></i>
                                    Dashboard
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/products">
                                    <i class="fas fa-mobile-alt"></i>
                                    Products
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/orders">
                                    <i class="fas fa-shopping-cart"></i>
                                    Orders
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/users">
                                    <i class="fas fa-users"></i>
                                    Users
                                </a>
                            </li>
                            <li style="margin-top: 2rem; border-top: 1px solid var(--border-color); padding-top: 1rem;">
                                <a href="${pageContext.request.contextPath}/">
                                    <i class="fas fa-arrow-left"></i>
                                    Back to Store
                                </a>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <!-- Admin Content -->
                <main class="admin-content">
                    <header style="margin-bottom: 2rem;">
                        <h1 style="color: var(--text-primary); margin-bottom: 0.5rem;">
                            <i class="fas fa-tachometer-alt"></i>
                            Dashboard
                        </h1>
                        <p style="color: var(--text-secondary);">Welcome to the PhoneHub admin panel</p>
                    </header>

                    <c:if test="${not empty error}">
                        <div class="alert alert-error">
                            <i class="fas fa-exclamation-circle"></i>
                            ${error}
                        </div>
                    </c:if>

                    <!-- Statistics Cards -->
                    <div class="product-grid" style="margin-bottom: 3rem;">
                        <div class="product-card">
                            <div class="product-info text-center">
                                <i class="fas fa-mobile-alt"
                                    style="font-size: 3rem; color: var(--primary-color); margin-bottom: 1rem;"></i>
                                <h3 class="product-name">Total Products</h3>
                                <div class="product-price">${totalProducts != null ? totalProducts : 0}</div>
                                <a href="${pageContext.request.contextPath}/admin/products"
                                    class="btn btn-primary btn-sm">
                                    Manage Products
                                </a>
                            </div>
                        </div>

                        <div class="product-card">
                            <div class="product-info text-center">
                                <i class="fas fa-shopping-cart"
                                    style="font-size: 3rem; color: var(--accent-color); margin-bottom: 1rem;"></i>
                                <h3 class="product-name">Total Orders</h3>
                                <div class="product-price">42</div>
                                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-accent btn-sm">
                                    Manage Orders
                                </a>
                            </div>
                        </div>

                        <div class="product-card">
                            <div class="product-info text-center">
                                <i class="fas fa-users"
                                    style="font-size: 3rem; color: var(--success-color); margin-bottom: 1rem;"></i>
                                <h3 class="product-name">Total Users</h3>
                                <div class="product-price">128</div>
                                <a href="${pageContext.request.contextPath}/admin/users"
                                    class="btn btn-secondary btn-sm">
                                    Manage Users
                                </a>
                            </div>
                        </div>

                        <div class="product-card">
                            <div class="product-info text-center">
                                <i class="fas fa-dollar-sign"
                                    style="font-size: 3rem; color: var(--error-color); margin-bottom: 1rem;"></i>
                                <h3 class="product-name">Total Revenue</h3>
                                <div class="product-price">$45,230</div>
                                <a href="#" class="btn btn-secondary btn-sm">
                                    View Reports
                                </a>
                            </div>
                        </div>
                    </div>

                    <!-- Quick Actions -->
                    <section
                        style="background: var(--surface-color); padding: 2rem; border-radius: 1rem; box-shadow: var(--shadow); margin-bottom: 2rem;">
                        <h2 style="margin-bottom: 1.5rem; color: var(--text-primary);">
                            <i class="fas fa-bolt"></i>
                            Quick Actions
                        </h2>

                        <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                            <a href="${pageContext.request.contextPath}/admin/product/new" class="btn btn-primary">
                                <i class="fas fa-plus"></i>
                                Add New Product
                            </a>

                            <button class="btn btn-secondary" onclick="exportData()">
                                <i class="fas fa-download"></i>
                                Export Data
                            </button>

                            <button class="btn btn-accent" onclick="viewAnalytics()">
                                <i class="fas fa-chart-bar"></i>
                                View Analytics
                            </button>

                            <button class="btn btn-secondary" onclick="manageInventory()">
                                <i class="fas fa-boxes"></i>
                                Manage Inventory
                            </button>
                        </div>
                    </section>

                    <!-- Recent Products -->
                    <c:if test="${not empty recentProducts}">
                        <section
                            style="background: var(--surface-color); padding: 2rem; border-radius: 1rem; box-shadow: var(--shadow);">
                            <h2 style="margin-bottom: 1.5rem; color: var(--text-primary);">
                                <i class="fas fa-clock"></i>
                                Recent Products
                            </h2>

                            <div class="data-table" style="overflow-x: auto;">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Price</th>
                                            <th>Stock</th>
                                            <th>Category</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="product" items="${recentProducts}" begin="0" end="9">
                                            <tr>
                                                <td>${product.id}</td>
                                                <td>
                                                    <div style="display: flex; align-items: center; gap: 0.5rem;">
                                                        <img src="${not empty product.imageUrl ? product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                                            alt="${product.name}"
                                                            style="width: 40px; height: 40px; object-fit: cover; border-radius: 0.25rem;">
                                                        <span>${product.name}</span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <strong style="color: var(--primary-color);">
                                                        $
                                                        <fmt:formatNumber value="${product.price}" type="number"
                                                            minFractionDigits="2" maxFractionDigits="2" />
                                                    </strong>
                                                </td>
                                                <td>
                                                    <span
                                                        style="color: ${product.stock > 10 ? 'var(--success-color)' : product.stock > 0 ? 'var(--warning-color)' : 'var(--error-color)'};">
                                                        ${product.stock}
                                                    </span>
                                                </td>
                                                <td>
                                                    <span
                                                        style="background: var(--background-color); padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-size: 0.875rem;">
                                                        ${product.categoryName != null ? product.categoryName :
                                                        'General'}
                                                    </span>
                                                </td>
                                                <td>
                                                    <div style="display: flex; gap: 0.5rem;">
                                                        <a href="${pageContext.request.contextPath}/admin/product/${product.id}"
                                                            class="btn btn-secondary"
                                                            style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/product/${product.id}"
                                                            class="btn btn-primary"
                                                            style="padding: 0.25rem 0.5rem; font-size: 0.75rem;"
                                                            target="_blank">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div style="text-align: center; margin-top: 1rem;">
                                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-primary">
                                    <i class="fas fa-arrow-right"></i>
                                    View All Products
                                </a>
                            </div>
                        </section>
                    </c:if>
                </main>

                <script>
                    function exportData() {
                        showNotification('Export feature coming soon!', 'info');
                    }

                    function viewAnalytics() {
                        showNotification('Analytics feature coming soon!', 'info');
                    }

                    function manageInventory() {
                        window.location.href = '${pageContext.request.contextPath}/admin/products';
                    }

                    function showNotification(message, type = 'info') {
                        const notification = document.createElement('div');
                        notification.className = `notification alert alert-${type}`;
                        notification.textContent = message;
                        notification.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 10000;
                min-width: 300px;
                animation: slideIn 0.3s ease-out;
            `;

                        document.body.appendChild(notification);

                        setTimeout(() => {
                            notification.style.animation = 'slideOut 0.3s ease-out';
                            setTimeout(() => notification.remove(), 300);
                        }, 3000);
                    }

                    // Highlight current nav item
                    document.addEventListener('DOMContentLoaded', function () {
                        const currentPath = window.location.pathname;
                        const navLinks = document.querySelectorAll('.admin-nav a');

                        navLinks.forEach(link => {
                            link.classList.remove('active');
                            if (link.getAttribute('href') === currentPath) {
                                link.classList.add('active');
                            }
                        });
                    });
                </script>
            </body>

            </html>