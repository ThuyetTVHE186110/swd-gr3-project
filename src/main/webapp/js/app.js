// Modern Phone Store JavaScript
document.addEventListener('DOMContentLoaded', function () {
    initializeApp();
});

function initializeApp() {
    initializeSearch();
    initializeCart();
    initializeProductGrid();
    initializeProductDetail();
    initializeMobileMenu();
}

// Search functionality
function initializeSearch() {
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');

    if (searchInput) {
        searchInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                performSearch(this.value);
            }
        });
    }

    if (searchBtn) {
        searchBtn.addEventListener('click', function () {
            const searchValue = searchInput.value;
            performSearch(searchValue);
        });
    }
}

function performSearch(query) {
    if (!query.trim()) return;

    showLoading();

    // Simulate API call - replace with actual servlet call
    setTimeout(() => {
        window.location.href = `/search?q=${encodeURIComponent(query)}`;
    }, 300);
}

// Shopping Cart functionality
function initializeCart() {
    updateCartCount();

    // Add to cart buttons
    document.addEventListener('click', function (e) {
        if (e.target.matches('.add-to-cart-btn')) {
            e.preventDefault();
            const productId = e.target.dataset.productId;
            const quantity = getQuantity(e.target);
            addToCart(productId, quantity);
        }

        if (e.target.matches('.remove-from-cart')) {
            e.preventDefault();
            const productId = e.target.dataset.productId;
            removeFromCart(productId);
        }

        if (e.target.matches('.update-quantity')) {
            const productId = e.target.dataset.productId;
            const newQuantity = e.target.value;
            updateCartQuantity(productId, newQuantity);
        }
    });
}

function getQuantity(element) {
    const quantityInput = element.closest('.product-card, .product-detail')
        ?.querySelector('.quantity-input');
    return quantityInput ? parseInt(quantityInput.value) || 1 : 1;
}

function addToCart(productId, quantity = 1) {
    showNotification('Adding to cart...', 'info');

    fetch('/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}&quantity=${quantity}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showNotification('Product added to cart!', 'success');
                updateCartCount(data.cartCount);
                animateCartIcon();
            } else {
                showNotification(data.message || 'Failed to add product to cart', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Error adding product to cart', 'error');
        });
}

function removeFromCart(productId) {
    fetch('/cart/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showNotification('Product removed from cart', 'success');
                updateCartCount(data.cartCount);
                location.reload(); // Refresh cart page
            } else {
                showNotification(data.message || 'Failed to remove product', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Error removing product', 'error');
        });
}

function updateCartQuantity(productId, quantity) {
    if (quantity < 1) {
        removeFromCart(productId);
        return;
    }

    fetch('/cart/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}&quantity=${quantity}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                updateCartCount(data.cartCount);
                updateCartTotal(data.cartTotal);
            } else {
                showNotification(data.message || 'Failed to update quantity', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Error updating quantity', 'error');
        });
}

function updateCartCount(count) {
    const cartCountElement = document.querySelector('.cart-count');
    if (cartCountElement) {
        if (count && count > 0) {
            cartCountElement.textContent = count;
            cartCountElement.style.display = 'flex';
        } else {
            cartCountElement.style.display = 'none';
        }
    }
}

function animateCartIcon() {
    const cartIcon = document.querySelector('.cart-icon');
    if (cartIcon) {
        cartIcon.style.transform = 'scale(1.2)';
        setTimeout(() => {
            cartIcon.style.transform = 'scale(1)';
        }, 200);
    }
}

// Product grid functionality
function initializeProductGrid() {
    const categoryBtns = document.querySelectorAll('.category-btn');

    categoryBtns.forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();

            // Update active state
            categoryBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const category = this.dataset.category;
            filterProductsByCategory(category);
        });
    });

    // Infinite scroll
    let isLoading = false;
    let currentPage = 1;

    window.addEventListener('scroll', function () {
        if (isLoading) return;

        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        const windowHeight = window.innerHeight;
        const documentHeight = document.documentElement.scrollHeight;

        if (scrollTop + windowHeight >= documentHeight - 1000) {
            loadMoreProducts();
        }
    });

    function loadMoreProducts() {
        isLoading = true;
        currentPage++;

        fetch(`/products?page=${currentPage}`, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.products && data.products.length > 0) {
                    appendProducts(data.products);
                }
                isLoading = false;
            })
            .catch(error => {
                console.error('Error loading more products:', error);
                isLoading = false;
            });
    }
}

function filterProductsByCategory(category) {
    showLoading();

    fetch(`/products?category=${encodeURIComponent(category)}`, {
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => response.json())
        .then(data => {
            hideLoading();
            updateProductGrid(data.products);
        })
        .catch(error => {
            console.error('Error filtering products:', error);
            hideLoading();
        });
}

function updateProductGrid(products) {
    const productGrid = document.querySelector('.product-grid');
    if (!productGrid) return;

    productGrid.innerHTML = '';

    if (products.length === 0) {
        productGrid.innerHTML = `
            <div class="empty-state">
                <h3>No products found</h3>
                <p>Try a different category or search term.</p>
            </div>
        `;
        return;
    }

    products.forEach(product => {
        const productCard = createProductCard(product);
        productGrid.appendChild(productCard);
    });
}

function createProductCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';
    card.innerHTML = `
        <img src="${product.imageUrl || '/images/placeholder.jpg'}" 
             alt="${product.name}" 
             class="product-image"
             onerror="this.src='/images/placeholder.jpg'">
        <div class="product-info">
            <h3 class="product-name">${product.name}</h3>
            <p class="product-description">${product.description || ''}</p>
            <div class="product-price">$${formatPrice(product.price)}</div>
            <div class="product-actions">
                <button class="btn btn-primary btn-sm add-to-cart-btn" 
                        data-product-id="${product.id}">
                    Add to Cart
                </button>
                <a href="/product/${product.id}" class="btn btn-secondary btn-sm">
                    View Details
                </a>
            </div>
        </div>
    `;

    return card;
}

// Product detail page functionality
function initializeProductDetail() {
    const thumbnails = document.querySelectorAll('.thumbnail');
    const mainImage = document.querySelector('.main-image');

    thumbnails.forEach(thumb => {
        thumb.addEventListener('click', function () {
            if (mainImage) {
                mainImage.src = this.src;
                thumbnails.forEach(t => t.classList.remove('active'));
                this.classList.add('active');
            }
        });
    });

    // Quantity controls
    const quantityBtns = document.querySelectorAll('.quantity-btn');
    const quantityInput = document.querySelector('.quantity-input');

    quantityBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            if (!quantityInput) return;

            const isIncrement = this.textContent === '+';
            const currentValue = parseInt(quantityInput.value) || 1;
            const newValue = isIncrement ? currentValue + 1 : Math.max(1, currentValue - 1);

            quantityInput.value = newValue;
        });
    });

    if (quantityInput) {
        quantityInput.addEventListener('change', function () {
            const value = parseInt(this.value) || 1;
            this.value = Math.max(1, value);
        });
    }
}

// Mobile menu functionality
function initializeMobileMenu() {
    const menuToggle = document.querySelector('.mobile-menu-toggle');
    const mobileMenu = document.querySelector('.mobile-menu');

    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', function () {
            mobileMenu.classList.toggle('active');
        });

        // Close menu when clicking outside
        document.addEventListener('click', function (e) {
            if (!mobileMenu.contains(e.target) && !menuToggle.contains(e.target)) {
                mobileMenu.classList.remove('active');
            }
        });
    }
}

// Utility functions
function showLoading() {
    const existingLoader = document.querySelector('.page-loader');
    if (existingLoader) return;

    const loader = document.createElement('div');
    loader.className = 'page-loader loading';
    loader.innerHTML = '<div class="spinner"></div>';
    document.body.appendChild(loader);
}

function hideLoading() {
    const loader = document.querySelector('.page-loader');
    if (loader) {
        loader.remove();
    }
}

function showNotification(message, type = 'info') {
    // Remove existing notifications
    const existingNotifications = document.querySelectorAll('.notification');
    existingNotifications.forEach(notification => notification.remove());

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

    // Auto remove after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

function formatPrice(price) {
    return parseFloat(price).toFixed(2);
}

function updateCartTotal(total) {
    const totalElement = document.querySelector('.cart-total-amount');
    if (totalElement) {
        totalElement.textContent = `$${formatPrice(total)}`;
    }
}

// CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
    
    .page-loader {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.8);
        z-index: 9999;
    }
    
    .mobile-menu {
        display: none;
        position: fixed;
        top: 64px;
        left: 0;
        width: 100%;
        background: white;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        z-index: 1000;
        padding: 1rem;
    }
    
    .mobile-menu.active {
        display: block;
    }
    
    @media (max-width: 768px) {
        .nav-menu {
            display: none;
        }
        
        .mobile-menu-toggle {
            display: block;
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
        }
    }
    
    @media (min-width: 769px) {
        .mobile-menu-toggle {
            display: none;
        }
    }
`;
document.head.appendChild(style);
