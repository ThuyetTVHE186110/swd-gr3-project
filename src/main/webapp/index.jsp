<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>PhoneHub - Premium Mobile Devices</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            </head>

            <body>
                <!-- Header -->
                <header class="header">
                    <nav class="navbar">
                        <a href="${pageContext.request.contextPath}/" class="logo">
                            <i class="fas fa-mobile-alt"></i> PhoneHub
                        </a>

                        <ul class="nav-menu">
                            <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                            <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/about" class="nav-link">About</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact" class="nav-link">Contact</a></li>
                        </ul>

                        <div class="nav-actions">
                            <div class="search-box">
                                <input type="text" placeholder="Search phones..." class="search-input" id="searchInput">
                                <button class="search-btn" id="searchBtn">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>

                            <div class="cart-icon"
                                onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                                <i class="fas fa-shopping-cart"></i>
                                <span class="cart-count" style="display: none;">0</span>
                            </div>

                            <div class="user-icon">
                                <i class="fas fa-user"></i>
                            </div>

                            <button class="mobile-menu-toggle">
                                <i class="fas fa-bars"></i>
                            </button>
                        </div>
                    </nav>

                    <!-- Mobile Menu -->
                    <div class="mobile-menu">
                        <ul class="nav-menu">
                            <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                            <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/about" class="nav-link">About</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact" class="nav-link">Contact</a></li>
                        </ul>
                    </div>
                </header>

                <!-- Hero Section -->
                <section class="hero">
                    <div class="container">
                        <h1>Discover Premium Mobile Devices</h1>
                        <p>Find the perfect smartphone for your lifestyle with our curated collection of the latest and
                            greatest mobile technology.</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-accent">
                            <i class="fas fa-mobile-alt"></i>
                            Shop Now
                        </a>
                    </div>
                </section>

                <!-- Main Content -->
                <main class="container">
                    <!-- Categories -->
                    <section class="section">
                        <h2 class="section-title">Shop by Category</h2>
                        <div class="categories">
                            <a href="${pageContext.request.contextPath}/products" class="category-btn active"
                                data-category="">All Phones</a>
                            <a href="${pageContext.request.contextPath}/products?category=flagship" class="category-btn"
                                data-category="flagship">Flagship</a>
                            <a href="${pageContext.request.contextPath}/products?category=midrange" class="category-btn"
                                data-category="midrange">Mid-Range</a>
                            <a href="${pageContext.request.contextPath}/products?category=budget" class="category-btn"
                                data-category="budget">Budget</a>
                            <a href="${pageContext.request.contextPath}/products?category=gaming" class="category-btn"
                                data-category="gaming">Gaming</a>
                            <a href="${pageContext.request.contextPath}/products?category=business" class="category-btn"
                                data-category="business">Business</a>
                        </div>
                    </section>

                    <!-- Products Section -->
                    <section class="section">
                        <h2 class="section-title">Featured Products</h2>

                        <c:if test="${not empty error}">
                            <div class="alert alert-error">
                                <i class="fas fa-exclamation-circle"></i>
                                ${error}
                            </div>
                        </c:if>

                        <div class="product-grid">
                            <c:choose>
                                <c:when test="${empty products}">
                                    <div class="empty-state">
                                        <i class="fas fa-mobile-alt"
                                            style="font-size: 4rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                                        <h3>No products available</h3>
                                        <p>Check back soon for new arrivals!</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="product" items="${products}">
                                        <div class="product-card"
                                            onclick="window.location.href='${pageContext.request.contextPath}/product/${product.id}'">
                                            <img src="${not empty product.imageUrl ? product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                                alt="${product.name}" class="product-image"
                                                onerror="this.src='${pageContext.request.contextPath}/images/placeholder-phone.jpg'">

                                            <div class="product-info">
                                                <h3 class="product-name">${product.name}</h3>
                                                <p class="product-description">
                                                    <c:choose>
                                                        <c:when test="${product.description.length() > 100}">
                                                            ${product.description.substring(0, 100)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${product.description}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>

                                                <div class="product-price">
                                                    $
                                                    <fmt:formatNumber value="${product.price}" type="number"
                                                        minFractionDigits="2" maxFractionDigits="2" />
                                                </div>

                                                <div class="product-actions">
                                                    <button class="btn btn-primary btn-sm add-to-cart-btn"
                                                        data-product-id="${product.id}"
                                                        onclick="event.stopPropagation()">
                                                        <i class="fas fa-cart-plus"></i>
                                                        Add to Cart
                                                    </button>
                                                    <a href="${pageContext.request.contextPath}/product/${product.id}"
                                                        class="btn btn-secondary btn-sm"
                                                        onclick="event.stopPropagation()">
                                                        <i class="fas fa-eye"></i>
                                                        View
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Load More Button -->
                        <c:if test="${not empty products and products.size() == 12}">
                            <div class="text-center mt-4">
                                <button class="btn btn-secondary" id="loadMoreBtn">
                                    <i class="fas fa-plus"></i>
                                    Load More Products
                                </button>
                            </div>
                        </c:if>
                    </section>

                    <!-- Features Section -->
                    <section class="section">
                        <h2 class="section-title">Why Choose PhoneHub?</h2>
                        <div class="product-grid">
                            <div class="product-card">
                                <div class="product-info text-center">
                                    <i class="fas fa-shipping-fast"
                                        style="font-size: 3rem; color: var(--primary-color); margin-bottom: 1rem;"></i>
                                    <h3 class="product-name">Free Shipping</h3>
                                    <p class="product-description">Free shipping on all orders over $500. Fast and
                                        secure delivery worldwide.</p>
                                </div>
                            </div>

                            <div class="product-card">
                                <div class="product-info text-center">
                                    <i class="fas fa-shield-alt"
                                        style="font-size: 3rem; color: var(--primary-color); margin-bottom: 1rem;"></i>
                                    <h3 class="product-name">2 Year Warranty</h3>
                                    <p class="product-description">Comprehensive warranty coverage on all devices. Peace
                                        of mind guaranteed.</p>
                                </div>
                            </div>

                            <div class="product-card">
                                <div class="product-info text-center">
                                    <i class="fas fa-headset"
                                        style="font-size: 3rem; color: var(--primary-color); margin-bottom: 1rem;"></i>
                                    <h3 class="product-name">24/7 Support</h3>
                                    <p class="product-description">Round-the-clock customer support. We're here to help
                                        whenever you need us.</p>
                                </div>
                            </div>
                        </div>
                    </section>
                </main>

                <!-- Footer -->
                <footer class="footer">
                    <div class="container">
                        <div class="footer-content">
                            <div class="footer-section">
                                <h3><i class="fas fa-mobile-alt"></i> PhoneHub</h3>
                                <p>Your trusted destination for premium mobile devices. We bring you the latest
                                    technology with unbeatable prices and exceptional service.</p>
                            </div>

                            <div class="footer-section">
                                <h3>Quick Links</h3>
                                <ul>
                                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                                    <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                                    <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>
                                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                                </ul>
                            </div>

                            <div class="footer-section">
                                <h3>Categories</h3>
                                <ul>
                                    <li><a href="${pageContext.request.contextPath}/products?category=flagship">Flagship
                                            Phones</a></li>
                                    <li><a href="${pageContext.request.contextPath}/products?category=midrange">Mid-Range
                                            Phones</a></li>
                                    <li><a href="${pageContext.request.contextPath}/products?category=budget">Budget
                                            Phones</a></li>
                                    <li><a href="${pageContext.request.contextPath}/products?category=gaming">Gaming
                                            Phones</a></li>
                                </ul>
                            </div>

                            <div class="footer-section">
                                <h3>Customer Service</h3>
                                <ul>
                                    <li><a href="#"><i class="fas fa-phone"></i> (555) 123-4567</a></li>
                                    <li><a href="#"><i class="fas fa-envelope"></i> support@phonehub.com</a></li>
                                    <li><a href="#"><i class="fas fa-map-marker-alt"></i> Store Locator</a></li>
                                    <li><a href="#"><i class="fas fa-question-circle"></i> FAQ</a></li>
                                </ul>
                            </div>
                        </div>

                        <div class="footer-bottom">
                            <p>&copy; 2025 PhoneHub. All rights reserved. | Privacy Policy | Terms of Service</p>
                        </div>
                    </div>
                </footer>

                <script src="${pageContext.request.contextPath}/js/app.js"></script>
                <script>
                    // Update cart count on page load
                    document.addEventListener('DOMContentLoaded', function () {
                        // Simulate getting cart count from session
                        <c:if test="${not empty sessionScope.cart}">
                            updateCartCount(${ sessionScope.cart.totalItems });
                        </c:if>
                    });
                </script>
            </body>

            </html>