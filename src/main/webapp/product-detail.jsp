<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>${product.name} - PhoneHub</title>
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

                            <button class="cart-icon"
                                onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                                <i class="fas fa-shopping-cart"></i>
                                <span class="cart-count" style="display: none;">0</span>
                            </button>

                            <div class="user-icon">
                                <i class="fas fa-user"></i>
                            </div>

                            <button class="mobile-menu-toggle">
                                <i class="fas fa-bars"></i>
                            </button>
                        </div>
                    </nav>
                </header>

                <main class="container">
                    <!-- Breadcrumb -->
                    <nav class="breadcrumb" style="padding: 2rem 0 1rem; color: var(--text-secondary);">
                        <a href="${pageContext.request.contextPath}/"
                            style="color: var(--text-secondary); text-decoration: none;">Home</a>
                        <span style="margin: 0 0.5rem;">/</span>
                        <a href="${pageContext.request.contextPath}/products"
                            style="color: var(--text-secondary); text-decoration: none;">Products</a>
                        <span style="margin: 0 0.5rem;">/</span>
                        <span style="color: var(--text-primary); font-weight: 600;">${product.name}</span>
                    </nav>

                    <c:if test="${not empty error}">
                        <div class="alert alert-error">
                            <i class="fas fa-exclamation-circle"></i>
                            ${error}
                        </div>
                    </c:if>

                    <c:if test="${not empty product}">
                        <div class="product-detail">
                            <!-- Product Gallery -->
                            <div class="product-gallery">
                                <img src="${not empty product.imageUrl ? product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                    alt="${product.name}" class="main-image" id="mainImage">

                                <div class="thumbnail-list">
                                    <img src="${not empty product.imageUrl ? product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                        alt="View 1" class="thumbnail active">
                                    <!-- Additional images would go here if available -->
                                    <c:if test="${not empty product.imageUrls and product.imageUrls.size() > 0}">
                                        <c:forEach var="additionalImage" items="${product.imageUrls}"
                                            varStatus="status">
                                            <c:if test="${not empty additionalImage}">
                                                <img src="${additionalImage}" alt="View ${status.index + 2}"
                                                    class="thumbnail">
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </div>
                            </div>

                            <!-- Product Details -->
                            <div class="product-details">
                                <h1>${product.name}</h1>

                                <div class="product-rating">
                                    <div class="stars">
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star-half-alt"></i>
                                    </div>
                                    <span style="color: var(--text-secondary);">(4.5 out of 5 based on 128
                                        reviews)</span>
                                </div>

                                <div class="price-section">
                                    <span class="current-price">
                                        $
                                        <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2"
                                            maxFractionDigits="2" />
                                    </span>
                                    <!-- Original price for sale items -->
<%--                                    <c:if test="${product.originalPrice > product.price}">--%>
<%--                                        <span class="original-price">--%>
<%--                                            $--%>
<%--                                            <fmt:formatNumber value="${product.originalPrice}" type="number"--%>
<%--                                                minFractionDigits="2" maxFractionDigits="2" />--%>
<%--                                        </span>--%>
<%--                                    </c:if>--%>
                                </div>

                                <div class="product-description">
                                    <p>${product.description}</p>
                                </div>

                                <!-- Stock Status -->
                                <div class="stock-status" style="margin: 1rem 0;">
                                    <c:choose>
                                        <c:when test="${product.stock > 0}">
                                            <span style="color: var(--success-color); font-weight: 600;">
                                                <i class="fas fa-check-circle"></i>
                                                In Stock (${product.stock} available)
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: var(--error-color); font-weight: 600;">
                                                <i class="fas fa-times-circle"></i>
                                                Out of Stock
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Quantity Selector -->
                                <div class="quantity-selector">
                                    <label for="quantity"
                                        style="font-weight: 600; margin-right: 1rem;">Quantity:</label>
                                    <div class="quantity-controls">
                                        <button type="button" class="quantity-btn"
                                            onclick="updateQuantity(-1)">-</button>
                                        <input type="number" class="quantity-input" id="quantity" value="1" min="1"
                                            max="${product.stock}">
                                        <button type="button" class="quantity-btn"
                                            onclick="updateQuantity(1)">+</button>
                                    </div>
                                </div>

                                <!-- Action Buttons -->
                                <div class="product-actions">
                                    <c:choose>
                                        <c:when test="${product.stock > 0}">
                                            <button class="btn btn-primary add-to-cart-btn"
                                                data-product-id="${product.id}">
                                                <i class="fas fa-cart-plus"></i>
                                                Add to Cart
                                            </button>
                                            <button class="btn btn-accent" onclick="buyNow()">
                                                <i class="fas fa-bolt"></i>
                                                Buy Now
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-secondary" disabled>
                                                <i class="fas fa-ban"></i>
                                                Out of Stock
                                            </button>
                                        </c:otherwise>
                                    </c:choose>

                                    <button class="btn btn-secondary" onclick="addToWishlist()">
                                        <i class="far fa-heart"></i>
                                        Add to Wishlist
                                    </button>
                                </div>

                                <!-- Product Features -->
                                <div class="specifications">
                                    <h3>Key Features</h3>
                                    <ul style="list-style: none; padding: 0;">
                                        <li style="padding: 0.5rem 0; border-bottom: 1px solid var(--border-color);"><i
                                                class="fas fa-check"
                                                style="color: var(--success-color); margin-right: 0.5rem;"></i> Premium
                                            build quality</li>
                                        <li style="padding: 0.5rem 0; border-bottom: 1px solid var(--border-color);"><i
                                                class="fas fa-check"
                                                style="color: var(--success-color); margin-right: 0.5rem;"></i> Latest
                                            Android/iOS</li>
                                        <li style="padding: 0.5rem 0; border-bottom: 1px solid var(--border-color);"><i
                                                class="fas fa-check"
                                                style="color: var(--success-color); margin-right: 0.5rem;"></i> Fast
                                            charging support</li>
                                        <li style="padding: 0.5rem 0; border-bottom: 1px solid var(--border-color);"><i
                                                class="fas fa-check"
                                                style="color: var(--success-color); margin-right: 0.5rem;"></i>
                                            Professional camera system</li>
                                        <li style="padding: 0.5rem 0;"><i class="fas fa-check"
                                                style="color: var(--success-color); margin-right: 0.5rem;"></i> 2-year
                                            warranty included</li>
                                    </ul>
                                </div>

                                <!-- Specifications Table -->
                                <div class="specifications">
                                    <h3>Technical Specifications</h3>
                                    <table class="spec-table">
                                        <tr>
                                            <th>Display</th>
                                            <td>6.1" Super Retina XDR OLED</td>
                                        </tr>
                                        <tr>
                                            <th>Processor</th>
                                            <td>A15 Bionic chip with Neural Engine</td>
                                        </tr>
                                        <tr>
                                            <th>Storage</th>
                                            <td>128GB / 256GB / 512GB</td>
                                        </tr>
                                        <tr>
                                            <th>Camera</th>
                                            <td>Triple 12MP camera system</td>
                                        </tr>
                                        <tr>
                                            <th>Battery</th>
                                            <td>Up to 22 hours video playback</td>
                                        </tr>
                                        <tr>
                                            <th>Operating System</th>
                                            <td>iOS 16</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <!-- Related Products -->
                        <c:if test="${not empty relatedProducts}">
                            <section class="section">
                                <h2 class="section-title">Related Products</h2>
                                <div class="product-grid">
                                    <c:forEach var="relatedProduct" items="${relatedProducts}" begin="0" end="3">
                                        <div class="product-card">
                                            <img src="${not empty relatedProduct.imageUrl ? relatedProduct.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                                alt="${relatedProduct.name}" class="product-image">

                                            <div class="product-info">
                                                <h3 class="product-name">${relatedProduct.name}</h3>
                                                <div class="product-price">
                                                    $
                                                    <fmt:formatNumber value="${relatedProduct.price}" type="number"
                                                        minFractionDigits="2" maxFractionDigits="2" />
                                                </div>

                                                <div class="product-actions">
                                                    <button class="btn btn-primary btn-sm add-to-cart-btn"
                                                        data-product-id="${relatedProduct.id}">
                                                        Add to Cart
                                                    </button>
                                                    <a href="${pageContext.request.contextPath}/product/${relatedProduct.id}"
                                                        class="btn btn-secondary btn-sm">
                                                        View
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </section>
                        </c:if>
                    </c:if>
                </main>

                <!-- Footer -->
                <footer class="footer">
                    <div class="container">
                        <div class="footer-content">
                            <div class="footer-section">
                                <h3><i class="fas fa-mobile-alt"></i> PhoneHub</h3>
                                <p>Your trusted destination for premium mobile devices.</p>
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
                                <h3>Customer Service</h3>
                                <ul>
                                    <li><a href="#"><i class="fas fa-phone"></i> (555) 123-4567</a></li>
                                    <li><a href="#"><i class="fas fa-envelope"></i> support@phonehub.com</a></li>
                                    <li><a href="#"><i class="fas fa-question-circle"></i> FAQ</a></li>
                                </ul>
                            </div>
                        </div>

                        <div class="footer-bottom">
                            <p>&copy; 2025 PhoneHub. All rights reserved.</p>
                        </div>
                    </div>
                </footer>

                <script src="${pageContext.request.contextPath}/js/app.js"></script>
                <script>
                    function updateQuantity(change) {
                        const quantityInput = document.getElementById('quantity');
                        const currentValue = parseInt(quantityInput.value) || 1;
                        const maxValue = parseInt(quantityInput.max) || 999;
                        const newValue = Math.max(1, Math.min(maxValue, currentValue + change));
                        quantityInput.value = newValue;
                    }

                    function buyNow() {
                        const quantity = document.getElementById('quantity').value;
                        // Add to cart and redirect to checkout
                        fetch('/cart/add', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: `productId=${product.id}&quantity=${quantity}`
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    window.location.href = '${pageContext.request.contextPath}/checkout';
                                } else {
                                    showNotification(data.message || 'Failed to add product to cart', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                showNotification('Error processing request', 'error');
                            });
                    }

                    function addToWishlist() {
                        showNotification('Wishlist feature coming soon!', 'info');
                    }

                    // Update cart count on page load
                    document.addEventListener('DOMContentLoaded', function () {
                        <c:if test="${not empty sessionScope.cart}">
                            updateCartCount(${ sessionScope.cart.totalItems });
                        </c:if>
                    });
                </script>
            </body>

            </html>