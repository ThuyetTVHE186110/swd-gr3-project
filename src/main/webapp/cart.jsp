<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Shopping Cart - PhoneHub</title>
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

                            <button class="cart-icon active">
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

                <main class="cart-page">
                    <!-- Breadcrumb -->
                    <nav class="breadcrumb"
                        style="padding: 2rem 0 1rem; color: var(--text-secondary); text-align: center;">
                        <a href="${pageContext.request.contextPath}/"
                            style="color: var(--text-secondary); text-decoration: none;">Home</a>
                        <span style="margin: 0 0.5rem;">/</span>
                        <span style="color: var(--text-primary); font-weight: 600;">Shopping Cart</span>
                    </nav>

                    <h1 class="text-center mb-4">
                        <i class="fas fa-shopping-cart"></i>
                        Your Shopping Cart
                    </h1>

                    <c:choose>
                        <c:when test="${empty cart || empty cart.items}">
                            <!-- Empty Cart -->
                            <div class="empty-state">
                                <i class="fas fa-shopping-cart"
                                    style="font-size: 4rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                                <h3>Your cart is empty</h3>
                                <p>Looks like you haven't added any items to your cart yet.</p>
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary mt-3">
                                    <i class="fas fa-arrow-left"></i>
                                    Continue Shopping
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- Cart Items -->
                            <div class="cart-items">
                                <c:forEach var="item" items="${cart.items}" varStatus="status">
                                    <div class="cart-item">
                                        <img src="${not empty item.product.imageUrl ? item.product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                            alt="${item.product.name}" class="cart-item-image">

                                        <div class="cart-item-info">
                                            <h4 class="cart-item-name">${item.product.name}</h4>
                                            <p class="cart-item-price">
                                                $
                                                <fmt:formatNumber value="${item.unitPrice}" type="number"
                                                    minFractionDigits="2" maxFractionDigits="2" /> each
                                            </p>
                                            <p style="color: var(--text-secondary); font-size: 0.875rem;">
                                                Stock: ${item.product.stock} available
                                            </p>
                                        </div>

                                        <!-- Quantity Controls -->
                                        <div class="quantity-controls">
                                            <button type="button" class="quantity-btn"
                                                onclick="updateQuantity(${item.product.id}, ${item.quantity - 1})">-</button>
                                            <input type="number" class="quantity-input update-quantity"
                                                value="${item.quantity}" min="0" max="${item.product.stock}"
                                                data-product-id="${item.product.id}"
                                                onchange="updateQuantity(${item.product.id}, this.value)">
                                            <button type="button" class="quantity-btn"
                                                onclick="updateQuantity(${item.product.id}, ${item.quantity + 1})">+</button>
                                        </div>

                                        <!-- Item Total -->
                                        <div class="cart-item-total">
                                            <strong>
                                                $
                                                <fmt:formatNumber value="${item.totalPrice}" type="number"
                                                    minFractionDigits="2" maxFractionDigits="2" />
                                            </strong>
                                        </div>

                                        <!-- Remove Button -->
                                        <button class="btn btn-secondary remove-from-cart"
                                            data-product-id="${item.product.id}" title="Remove from cart">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Cart Actions -->
                            <div class="cart-actions"
                                style="display: flex; justify-content: space-between; align-items: center; margin: 2rem 0; padding: 1rem; background: var(--background-color); border-radius: 0.5rem;">
                                <button class="btn btn-secondary" onclick="clearCart()">
                                    <i class="fas fa-trash-alt"></i>
                                    Clear Cart
                                </button>

                                <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i>
                                    Continue Shopping
                                </a>
                            </div>

                            <!-- Cart Summary -->
                            <div class="cart-summary">
                                <h3 style="margin-bottom: 1rem;">
                                    <i class="fas fa-calculator"></i>
                                    Order Summary
                                </h3>

                                <div class="summary-row">
                                    <span>Subtotal (${cart.totalItems} items):</span>
                                    <span>$
                                        <fmt:formatNumber value="${cart.totalAmount}" type="number"
                                            minFractionDigits="2" maxFractionDigits="2" />
                                    </span>
                                </div>

                                <div class="summary-row">
                                    <span>Shipping:</span>
                                    <span>
                                        <c:choose>
                                            <c:when test="${cart.totalAmount >= 500}">
                                                <span style="color: var(--success-color);">FREE</span>
                                            </c:when>
                                            <c:otherwise>
                                                $15.00
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <div class="summary-row">
                                    <span>Tax (8.5%):</span>
                                    <span>
                                        <c:set var="tax" value="${cart.totalAmount * 0.085}" />
                                        $
                                        <fmt:formatNumber value="${tax}" type="number" minFractionDigits="2"
                                            maxFractionDigits="2" />
                                    </span>
                                </div>

                                <div class="summary-total summary-row">
                                    <span>Total:</span>
                                    <span class="cart-total-amount">
                                        <c:set var="shipping" value="${cart.totalAmount >= 500 ? 0 : 15}" />
                                        <c:set var="finalTotal"
                                            value="${cart.totalAmount + shipping + (cart.totalAmount * 0.085)}" />
                                        $
                                        <fmt:formatNumber value="${finalTotal}" type="number" minFractionDigits="2"
                                            maxFractionDigits="2" />
                                    </span>
                                </div>

                                <c:if test="${cart.totalAmount < 500}">
                                    <p style="color: var(--text-secondary); font-size: 0.875rem; margin-top: 0.5rem;">
                                        <i class="fas fa-info-circle"></i>
                                        Add $
                                        <fmt:formatNumber value="${500 - cart.totalAmount}" type="number"
                                            minFractionDigits="2" maxFractionDigits="2" /> more for FREE shipping!
                                    </p>
                                </c:if>

                                <div style="margin-top: 2rem;">
                                    <a href="${pageContext.request.contextPath}/checkout"
                                        class="btn btn-primary w-full">
                                        <i class="fas fa-credit-card"></i>
                                        Proceed to Checkout
                                    </a>
                                </div>

                                <!-- Payment Methods -->
                                <div
                                    style="text-align: center; margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-color);">
                                    <p
                                        style="color: var(--text-secondary); font-size: 0.875rem; margin-bottom: 0.5rem;">
                                        We accept:</p>
                                    <div
                                        style="display: flex; justify-content: center; gap: 1rem; align-items: center;">
                                        <i class="fab fa-cc-visa" style="font-size: 2rem; color: #1a1f71;"></i>
                                        <i class="fab fa-cc-mastercard" style="font-size: 2rem; color: #eb001b;"></i>
                                        <i class="fab fa-cc-amex" style="font-size: 2rem; color: #006fcf;"></i>
                                        <i class="fab fa-paypal" style="font-size: 2rem; color: #003087;"></i>
                                    </div>
                                </div>
                            </div>

                            <!-- Security Badge -->
                            <div class="text-center mt-4" style="color: var(--text-secondary); font-size: 0.875rem;">
                                <i class="fas fa-shield-alt" style="color: var(--success-color);"></i>
                                Secure SSL Encryption | Your data is protected
                            </div>
                        </c:otherwise>
                    </c:choose>
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
                    function updateQuantity(productId, newQuantity) {
                        if (newQuantity < 0) return;

                        fetch('/cart/update', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: `productId=${productId}&quantity=${newQuantity}`
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    location.reload(); // Refresh the page to show updated totals
                                } else {
                                    showNotification(data.message || 'Failed to update quantity', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                showNotification('Error updating quantity', 'error');
                            });
                    }

                    function clearCart() {
                        if (confirm('Are you sure you want to remove all items from your cart?')) {
                            fetch('/cart/clear', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                }
                            })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        location.reload();
                                    } else {
                                        showNotification(data.message || 'Failed to clear cart', 'error');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    showNotification('Error clearing cart', 'error');
                                });
                        }
                    }

                    // Update cart count on page load
                    document.addEventListener('DOMContentLoaded', function () {
                        <c:if test="${not empty cart}">
                            updateCartCount(${ cart.totalItems });
                        </c:if>
                    });
                </script>

                <style>
                    .cart-item {
                        display: grid;
                        grid-template-columns: 80px 1fr auto auto auto;
                        gap: 1rem;
                        align-items: center;
                        padding: 1.5rem;
                        background: var(--surface-color);
                        border-radius: 1rem;
                        margin-bottom: 1rem;
                        box-shadow: var(--shadow);
                    }

                    .cart-item-total {
                        font-size: 1.125rem;
                        font-weight: 600;
                        color: var(--primary-color);
                        text-align: right;
                    }

                    @media (max-width: 768px) {
                        .cart-item {
                            grid-template-columns: 60px 1fr;
                            grid-template-rows: auto auto auto;
                            gap: 0.5rem;
                        }

                        .cart-item-image {
                            grid-row: 1 / -1;
                        }

                        .quantity-controls,
                        .cart-item-total,
                        .remove-from-cart {
                            grid-column: 2;
                            justify-self: end;
                        }
                    }
                </style>
            </body>

            </html>