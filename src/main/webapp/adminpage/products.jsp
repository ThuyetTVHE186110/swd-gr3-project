<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Product Management - PhoneHub Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            </head>

            <body style="margin: 0; background: var(--background-color);">
                <!-- Loading Spinner -->
                <div id="loadingSpinner" class="loading-spinner" aria-label="Loading">
                    <div class="spinner"></div>
                    <span class="sr-only">Loading products...</span>
                </div>

                <!-- Notification Container -->
                <div id="notificationContainer" class="notification-container" aria-live="polite"></div>

                <!-- Admin Sidebar -->
                <nav class="admin-sidebar" aria-label="Admin navigation">
                    <div style="padding: 2rem 1rem; border-bottom: 1px solid var(--border-color);">
                        <h2 style="color: var(--primary-color); margin: 0;">
                            <i class="fas fa-mobile-alt" aria-hidden="true"></i>
                            PhoneHub Admin
                        </h2>
                    </div>

                    <nav aria-label="Admin navigation menu">
                        <ul class="admin-nav">
                            <li>
                                <a href="${pageContext.request.contextPath}/admin" aria-label="Dashboard">
                                    <i class="fas fa-tachometer-alt" aria-hidden="true"></i>
                                    Dashboard
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/products" class="active"
                                    aria-current="page" aria-label="Products management">
                                    <i class="fas fa-mobile-alt" aria-hidden="true"></i>
                                    Products
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/orders"
                                    aria-label="Orders management">
                                    <i class="fas fa-shopping-cart" aria-hidden="true"></i>
                                    Orders
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/users" aria-label="Users management">
                                    <i class="fas fa-users" aria-hidden="true"></i>
                                    Users
                                </a>
                            </li>
                            <li style="margin-top: 2rem; border-top: 1px solid var(--border-color); padding-top: 1rem;">
                                <a href="${pageContext.request.contextPath}/" aria-label="Return to main store">
                                    <i class="fas fa-arrow-left" aria-hidden="true"></i>
                                    Back to Store
                                </a>
                            </li>
                        </ul>
                    </nav>
                </nav>

                <!-- Admin Main Wrapper -->
                <div class="admin-main">
                    <!-- Admin Content -->
                    <main class="admin-content" role="main">
                        <!-- Enhanced Header Section -->
                        <header class="page-header">
                            <div class="header-content">
                                <div class="header-info">
                                    <div class="breadcrumb" aria-label="Breadcrumb navigation">
                                        <nav aria-label="Page navigation breadcrumb">
                                            <ol class="breadcrumb-list">
                                                <li><a href="${pageContext.request.contextPath}/admin"
                                                        class="breadcrumb-link">Dashboard</a></li>
                                                <li aria-current="page" class="breadcrumb-current">Products</li>
                                            </ol>
                                        </nav>
                                    </div>
                                    <h1 id="page-title" class="page-title">
                                        <i class="fas fa-cube" aria-hidden="true"></i>
                                        Product Management
                                    </h1>
                                    <p class="page-description">Manage your phone inventory with comprehensive tools and
                                        insights</p>
                                </div>

                                <div class="header-actions">
                                    <button class="btn btn-outline btn-icon-text" onclick="exportProducts()"
                                        aria-label="Export all products" title="Export products to CSV">
                                        <i class="fas fa-download" aria-hidden="true"></i>
                                        <span>Export</span>
                                    </button>
                                    <button class="btn btn-primary btn-icon-text" onclick="openAddProductModal()"
                                        aria-label="Add new product" title="Create a new product">
                                        <i class="fas fa-plus" aria-hidden="true"></i>
                                        <span>Add Product</span>
                                    </button>
                                </div>
                            </div>
                        </header>

                        <!-- Alert Messages with Enhanced Accessibility -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-error" role="alert" aria-live="assertive">
                                <div class="alert-content">
                                    <i class="fas fa-exclamation-circle" aria-hidden="true"></i>
                                    <div class="alert-text">
                                        <strong>Error:</strong>
                                        <span>${error}</span>
                                    </div>
                                </div>
                                <button class="alert-close" onclick="this.parentElement.remove()"
                                    aria-label="Close error message" title="Dismiss error">
                                    <i class="fas fa-times" aria-hidden="true"></i>
                                </button>
                            </div>
                        </c:if>

                        <c:if test="${not empty success}">
                            <div class="alert alert-success" role="alert" aria-live="polite">
                                <div class="alert-content">
                                    <i class="fas fa-check-circle" aria-hidden="true"></i>
                                    <div class="alert-text">
                                        <strong>Success:</strong>
                                        <span>${success}</span>
                                    </div>
                                </div>
                                <button class="alert-close" onclick="this.parentElement.remove()"
                                    aria-label="Close success message" title="Dismiss success message">
                                    <i class="fas fa-times" aria-hidden="true"></i>
                                </button>
                            </div>
                        </c:if>

                        <!-- Success message placeholder -->
                        <div id="successAlert" class="alert alert-success" style="display: none;" role="alert"
                            aria-live="polite">
                            <div class="alert-content">
                                <i class="fas fa-check-circle" aria-hidden="true"></i>
                                <div class="alert-text">
                                    <span id="successMessage"></span>
                                </div>
                            </div>
                            <button class="alert-close" onclick="this.parentElement.style.display='none'"
                                aria-label="Close success message" title="Dismiss message">
                                <i class="fas fa-times" aria-hidden="true"></i>
                            </button>
                        </div>

                        <!-- Enhanced Filters and Search Section -->
                        <section class="filters-section" aria-label="Product filters and search tools">
                            <div class="filters-card">
                                <div class="filters-header">
                                    <h2 class="filters-title">
                                        <i class="fas fa-filter" aria-hidden="true"></i>
                                        Search & Filter
                                    </h2>
                                    <div class="filters-status" id="filtersStatus" aria-live="polite">
                                        <span class="active-filters-count" id="activeFiltersCount">No filters
                                            applied</span>
                                    </div>
                                </div>

                                <div class="filters-container">
                                    <!-- Enhanced Search -->
                                    <div class="search-group">
                                        <label for="productSearch" class="search-label">
                                            <i class="fas fa-search" aria-hidden="true"></i>
                                            Search Products
                                        </label>
                                        <div class="search-box">
                                            <input type="text" id="productSearch" class="search-input"
                                                placeholder="Search by name, ID, brand, or description..."
                                                aria-describedby="search-help" autocomplete="off" role="searchbox">
                                            <button class="search-btn" id="searchBtn" type="button"
                                                aria-label="Execute search" title="Search products">
                                                <i class="fas fa-search" aria-hidden="true"></i>
                                            </button>
                                            <button class="search-clear-btn" id="clearSearchBtn" type="button"
                                                aria-label="Clear search" title="Clear search" style="display: none;">
                                                <i class="fas fa-times" aria-hidden="true"></i>
                                            </button>
                                        </div>
                                        <div id="search-help" class="form-help">
                                            <i class="fas fa-info-circle" aria-hidden="true"></i>
                                            Use keywords to find products quickly. Try searching by brand, model, or
                                            category.
                                        </div>
                                    </div>

                                    <!-- Filter Row -->
                                    <div class="filters-row">
                                        <div class="filter-group">
                                            <label for="categoryFilter" class="filter-label">
                                                <i class="fas fa-tags" aria-hidden="true"></i>
                                                Category
                                            </label>
                                            <select class="form-select" id="categoryFilter"
                                                aria-label="Filter products by category">
                                                <option value="">All Categories</option>
                                                <option value="flagship">📱 Flagship Phones</option>
                                                <option value="midrange">📲 Mid-Range Phones</option>
                                                <option value="budget">💰 Budget Phones</option>
                                                <option value="gaming">🎮 Gaming Phones</option>
                                                <option value="business">💼 Business Phones</option>
                                                <option value="accessories">🔌 Accessories</option>
                                            </select>
                                        </div>

                                        <div class="filter-group">
                                            <label for="stockFilter" class="filter-label">
                                                <i class="fas fa-boxes" aria-hidden="true"></i>
                                                Stock Level
                                            </label>
                                            <select class="form-select" id="stockFilter"
                                                aria-label="Filter products by stock level">
                                                <option value="">All Stock Levels</option>
                                                <option value="in-stock">✅ In Stock (≥ 10)</option>
                                                <option value="low-stock">⚠️ Low Stock (1-9)</option>
                                                <option value="out-of-stock">❌ Out of Stock (0)</option>
                                                <option value="high-stock">📦 High Stock (≥ 50)</option>
                                            </select>
                                        </div>

                                        <div class="filter-group">
                                            <label for="statusFilter" class="filter-label">
                                                <i class="fas fa-toggle-on" aria-hidden="true"></i>
                                                Status
                                            </label>
                                            <select class="form-select" id="statusFilter"
                                                aria-label="Filter products by status">
                                                <option value="">All Status</option>
                                                <option value="active">🟢 Active</option>
                                                <option value="inactive">🔴 Inactive</option>
                                                <option value="discontinued">⛔ Discontinued</option>
                                            </select>
                                        </div>

                                        <div class="filter-actions">
                                            <button class="btn btn-outline btn-icon-text" onclick="clearAllFilters()"
                                                type="button" title="Clear all active filters">
                                                <i class="fas fa-times" aria-hidden="true"></i>
                                                <span>Clear</span>
                                            </button>
                                            <button class="btn btn-secondary btn-icon-text"
                                                onclick="toggleAdvancedFilters()" type="button" aria-expanded="false"
                                                aria-controls="advanced-filters"
                                                title="Show advanced filtering options">
                                                <i class="fas fa-sliders-h" aria-hidden="true"></i>
                                                <span>Advanced</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Enhanced Advanced Filters -->
                                <section id="advanced-filters" class="advanced-filters" style="display: none;"
                                    aria-labelledby="advanced-filters-title">
                                    <div class="advanced-filters-header">
                                        <h3 id="advanced-filters-title" class="advanced-filters-title">
                                            <i class="fas fa-cogs" aria-hidden="true"></i>
                                            Advanced Filters
                                        </h3>
                                        <p class="advanced-filters-description">Fine-tune your search with additional
                                            criteria</p>
                                    </div>
                                    <div class="advanced-filters-grid">
                                        <div class="filter-group">
                                            <label for="priceMin" class="filter-label">
                                                <i class="fas fa-dollar-sign" aria-hidden="true"></i>
                                                Min Price ($)
                                            </label>
                                            <input type="number" id="priceMin" class="form-input" min="0" step="0.01"
                                                placeholder="0.00" aria-label="Minimum price filter"
                                                title="Filter products with price greater than or equal to this value">
                                        </div>
                                        <div class="filter-group">
                                            <label for="priceMax" class="filter-label">
                                                <i class="fas fa-dollar-sign" aria-hidden="true"></i>
                                                Max Price ($)
                                            </label>
                                            <input type="number" id="priceMax" class="form-input" min="0" step="0.01"
                                                placeholder="9999.99" aria-label="Maximum price filter"
                                                title="Filter products with price less than or equal to this value">
                                        </div>
                                        <div class="filter-group">
                                            <label for="dateFilter" class="filter-label">
                                                <i class="fas fa-calendar" aria-hidden="true"></i>
                                                Date Added
                                            </label>
                                            <select class="form-select" id="dateFilter"
                                                aria-label="Filter by date added"
                                                title="Filter products by when they were added to inventory">
                                                <option value="">Any Time</option>
                                                <option value="today">📅 Today</option>
                                                <option value="week">📅 This Week</option>
                                                <option value="month">📅 This Month</option>
                                                <option value="quarter">📅 This Quarter</option>
                                                <option value="year">📅 This Year</option>
                                            </select>
                                        </div>
                                        <div class="filter-group">
                                            <label for="brandFilter" class="filter-label">
                                                <i class="fas fa-tag" aria-hidden="true"></i>
                                                Brand
                                            </label>
                                            <select class="form-select" id="brandFilter" aria-label="Filter by brand"
                                                title="Filter products by manufacturer brand">
                                                <option value="">All Brands</option>
                                                <option value="apple">🍎 Apple</option>
                                                <option value="samsung">📱 Samsung</option>
                                                <option value="google">🎯 Google</option>
                                                <option value="oneplus">1️⃣ OnePlus</option>
                                                <option value="xiaomi">📲 Xiaomi</option>
                                                <option value="other">🏷️ Other</option>
                                            </select>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </section>

                        <!-- Enhanced Products Table Section -->
                        <section class="products-table-section" aria-labelledby="products-table-title">
                            <div class="table-card">
                                <div class="table-header">
                                    <div class="table-title-group">
                                        <h2 id="products-table-title" class="table-title">
                                            <i class="fas fa-cube" aria-hidden="true"></i>
                                            Products Inventory
                                        </h2>
                                        <div class="results-summary" id="resultsCount" aria-live="polite">
                                            <c:choose>
                                                <c:when test="${not empty products}">
                                                    <span class="results-count">
                                                        <strong>${products.size()}</strong> products found
                                                    </span>
                                                    <span class="results-meta">
                                                        <i class="fas fa-info-circle" aria-hidden="true"></i>
                                                        Updated just now
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="results-count no-results">
                                                        <i class="fas fa-exclamation-triangle" aria-hidden="true"></i>
                                                        No products found
                                                    </span>
                                                    <span class="results-meta">Try adjusting your filters</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="table-actions">
                                        <div class="view-controls" role="tablist" aria-label="Table view options">
                                            <button class="view-toggle active" id="tableViewBtn" role="tab"
                                                aria-selected="true" aria-controls="tableContainer"
                                                onclick="switchView('table')" title="Table view">
                                                <i class="fas fa-table" aria-hidden="true"></i>
                                                <span class="sr-only">Table view</span>
                                            </button>
                                            <button class="view-toggle" id="cardViewBtn" role="tab"
                                                aria-selected="false" aria-controls="tableContainer"
                                                onclick="switchView('card')" title="Card view">
                                                <i class="fas fa-th-large" aria-hidden="true"></i>
                                                <span class="sr-only">Card view</span>
                                            </button>
                                        </div>
                                        <div class="action-buttons">
                                            <button class="btn btn-outline btn-sm btn-icon-text"
                                                onclick="exportProducts()" type="button"
                                                aria-label="Export products data" title="Export to CSV">
                                                <i class="fas fa-download" aria-hidden="true"></i>
                                                <span>Export</span>
                                            </button>
                                            <button class="btn btn-secondary btn-sm btn-icon-text"
                                                onclick="toggleBulkActions()" type="button" aria-expanded="false"
                                                aria-controls="bulk-actions" aria-label="Show bulk actions"
                                                title="Bulk operations">
                                                <i class="fas fa-tasks" aria-hidden="true"></i>
                                                <span>Bulk Actions</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Bulk Actions Panel -->
                                <div id="bulk-actions" class="bulk-actions-panel" style="display: none;">
                                    <div class="bulk-actions-content">
                                        <span class="bulk-selected-count" id="bulkSelectedCount">0 items selected</span>
                                        <div class="bulk-actions-buttons">
                                            <button class="btn btn-sm btn-outline" onclick="bulkActivate()"
                                                type="button">
                                                <i class="fas fa-eye" aria-hidden="true"></i>
                                                Activate
                                            </button>
                                            <button class="btn btn-sm btn-outline" onclick="bulkDeactivate()"
                                                type="button">
                                                <i class="fas fa-eye-slash" aria-hidden="true"></i>
                                                Deactivate
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="bulkDelete()" type="button">
                                                <i class="fas fa-trash" aria-hidden="true"></i>
                                                Delete Selected
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Table Container -->
                                <div class="table-container" id="tableContainer">
                                    <c:choose>
                                        <c:when test="${empty products}">
                                            <div class="empty-state">
                                                <div class="empty-state-icon">
                                                    <i class="fas fa-mobile-alt" aria-hidden="true"></i>
                                                </div>
                                                <h3>No Products Found</h3>
                                                <p>Start building your inventory by adding your first product.</p>
                                                <div class="empty-state-actions">
                                                    <button class="btn btn-primary" onclick="openAddProductModal()"
                                                        type="button">
                                                        <i class="fas fa-plus" aria-hidden="true"></i>
                                                        Add Your First Product
                                                    </button>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table class="data-table" role="table"
                                                    aria-label="Products inventory table">
                                                    <thead>
                                                        <tr role="row">
                                                            <th scope="col" style="width: 50px;">
                                                                <div class="checkbox-wrapper">
                                                                    <input type="checkbox" id="selectAll"
                                                                        class="checkbox-input"
                                                                        onchange="toggleSelectAll(this)"
                                                                        aria-label="Select all products">
                                                                </div>
                                                            </th>
                                                            <th scope="col" class="sortable" onclick="sortTable('name')"
                                                                onkeypress="if(event.key==='Enter') sortTable('name')"
                                                                aria-sort="none" tabindex="0">
                                                                <span>Product</span>
                                                                <i class="fas fa-sort sort-icon" aria-hidden="true"></i>
                                                            </th>
                                                            <th scope="col" class="sortable"
                                                                onclick="sortTable('price')"
                                                                onkeypress="if(event.key==='Enter') sortTable('price')"
                                                                aria-sort="none" tabindex="0">
                                                                <span>Price</span>
                                                                <i class="fas fa-sort sort-icon" aria-hidden="true"></i>
                                                            </th>
                                                            <th scope="col" class="sortable"
                                                                onclick="sortTable('stock')"
                                                                onkeypress="if(event.key==='Enter') sortTable('stock')"
                                                                aria-sort="none" tabindex="0">
                                                                <span>Stock</span>
                                                                <i class="fas fa-sort sort-icon" aria-hidden="true"></i>
                                                            </th>
                                                            <th scope="col">Category</th>
                                                            <th scope="col">Status</th>
                                                            <th scope="col" style="width: 140px;">Actions</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="product" items="${products}" varStatus="status">
                                                            <tr role="row" data-product-id="${product.id}"
                                                                class="product-row">
                                                                <td>
                                                                    <div class="checkbox-wrapper">
                                                                        <input type="checkbox"
                                                                            id="product-${product.id}"
                                                                            class="checkbox-input product-checkbox"
                                                                            value="${product.id}"
                                                                            onchange="updateBulkSelection()"
                                                                            aria-label="Select product ${product.name}">
                                                                        <label for="product-${product.id}"
                                                                            class="checkbox-label"></label>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div class="product-info">
                                                                        <div class="product-image">
                                                                            <img src="${not empty product.imageUrl ? product.imageUrl : pageContext.request.contextPath.concat('/images/placeholder-phone.jpg')}"
                                                                                alt="${product.name}" loading="lazy">
                                                                        </div>
                                                                        <div class="product-details">
                                                                            <div class="product-name">${product.name}
                                                                            </div>
                                                                            <div class="product-meta">
                                                                                <span class="product-id">ID:
                                                                                    ${product.id}</span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div class="price-display">
                                                                        <fmt:formatNumber value="${product.price}"
                                                                            type="currency" currencySymbol="$" />
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div class="stock-info">
                                                                        <span
                                                                            class="stock-number">${product.stockQuantity}</span>
                                                                        <span
                                                                            class="stock-badge ${product.stockQuantity == 0 ? 'out-of-stock' : product.stockQuantity < 10 ? 'low-stock' : 'in-stock'}">
                                                                            <c:choose>
                                                                                <c:when
                                                                                    test="${product.stockQuantity == 0}">
                                                                                    <i class="fas fa-times-circle"
                                                                                        aria-hidden="true"></i>
                                                                                    Out of Stock
                                                                                </c:when>
                                                                                <c:when
                                                                                    test="${product.stockQuantity < 10}">
                                                                                    <i class="fas fa-exclamation-triangle"
                                                                                        aria-hidden="true"></i>
                                                                                    Low Stock
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <i class="fas fa-check-circle"
                                                                                        aria-hidden="true"></i>
                                                                                    In Stock
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </span>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <span class="category-tag">
                                                                        ${not empty product.categoryName ?
                                                                        product.categoryName
                                                                        : 'Uncategorized'}
                                                                    </span>
                                                                </td>
                                                                <td>
                                                                    <div class="status-wrapper">
                                                                        <span
                                                                            class="status-badge ${product.active ? 'active' : 'inactive'}">
                                                                            <i class="fas ${product.active ? 'fa-check-circle' : 'fa-times-circle'}"
                                                                                aria-hidden="true"></i>
                                                                            ${product.active ? 'Active' : 'Inactive'}
                                                                        </span>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div class="action-buttons">
                                                                        <button class="btn-icon btn-icon-primary"
                                                                            data-product-id="${product.id}"
                                                                            onclick="viewProduct(this.dataset.productId)"
                                                                            title="View product details"
                                                                            aria-label="View details for ${product.name}">
                                                                            <i class="fas fa-eye"
                                                                                aria-hidden="true"></i>
                                                                        </button>
                                                                        <button class="btn-icon btn-icon-secondary"
                                                                            data-product-id="${product.id}"
                                                                            onclick="editProduct(this.dataset.productId)"
                                                                            title="Edit product"
                                                                            aria-label="Edit ${product.name}">
                                                                            <i class="fas fa-edit"
                                                                                aria-hidden="true"></i>
                                                                        </button>
                                                                        <button class="btn-icon btn-icon-danger"
                                                                            data-product-id="${product.id}"
                                                                            data-product-name="${product.name}"
                                                                            onclick="confirmDeleteProduct(this.dataset.productId, this.dataset.productName)"
                                                                            title="Delete product"
                                                                            aria-label="Delete ${product.name}">
                                                                            <i class="fas fa-trash"
                                                                                aria-hidden="true"></i>
                                                                        </button>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Pagination -->
                                <c:if test="${not empty products}">
                                    <div class="pagination-section">
                                        <div class="pagination-info">
                                            <span>Showing ${products.size()} products</span>
                                        </div>

                                        <nav class="pagination-nav" aria-label="Products pagination">
                                            <button class="pagination-btn" ${currentPage <=1 ? 'disabled' : '' }
                                                data-page="${currentPage - 1}" onclick="goToPage(this.dataset.page)"
                                                aria-label="Go to previous page">
                                                <i class="fas fa-chevron-left" aria-hidden="true"></i>
                                                Previous
                                            </button>

                                            <span class="current-page">Page ${currentPage}</span>

                                            <button class="pagination-btn" data-page="${currentPage + 1}"
                                                onclick="goToPage(this.dataset.page)" aria-label="Go to next page">
                                                Next
                                                <i class="fas fa-chevron-right" aria-hidden="true"></i>
                                            </button>
                                        </nav>
                                    </div>
                                </c:if>
                        </section>

                        <!-- Quick Add Product Modal -->
                        <div id="addProductModal" class="modal" role="dialog" aria-labelledby="modalTitle"
                            aria-hidden="true" style="display: none;">
                            <div class="modal-backdrop" onclick="closeAddProductModal()"></div>
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h3 id="modalTitle">
                                        <i class="fas fa-plus" aria-hidden="true"></i>
                                        Quick Add Product
                                    </h3>
                                    <button class="modal-close" onclick="closeAddProductModal()"
                                        aria-label="Close modal">
                                        <i class="fas fa-times" aria-hidden="true"></i>
                                    </button>
                                </div>

                                <form id="quickAddForm" class="modal-body" novalidate>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="quickProductName" class="required">Product Name</label>
                                            <input type="text" id="quickProductName" name="name" class="form-input"
                                                required aria-describedby="nameError"
                                                placeholder="e.g., iPhone 14 Pro Max">
                                            <div id="nameError" class="field-error" role="alert"></div>
                                        </div>

                                        <div class="form-group">
                                            <label for="quickProductCategory">Category</label>
                                            <select id="quickProductCategory" name="category" class="form-select">
                                                <option value="">Select category</option>
                                                <option value="flagship">Flagship Phones</option>
                                                <option value="midrange">Mid-Range Phones</option>
                                                <option value="budget">Budget Phones</option>
                                                <option value="gaming">Gaming Phones</option>
                                                <option value="business">Business Phones</option>
                                                <option value="accessories">Accessories</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="quickProductPrice" class="required">Price ($)</label>
                                            <input type="number" id="quickProductPrice" name="price" class="form-input"
                                                required min="0" step="0.01" aria-describedby="priceError"
                                                placeholder="0.00">
                                            <div id="priceError" class="field-error" role="alert"></div>
                                        </div>

                                        <div class="form-group">
                                            <label for="quickProductStock" class="required">Stock Quantity</label>
                                            <input type="number" id="quickProductStock" name="stock" class="form-input"
                                                required min="0" aria-describedby="stockError" placeholder="0">
                                            <div id="stockError" class="field-error" role="alert"></div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="quickProductDescription">Description</label>
                                        <textarea id="quickProductDescription" name="description" class="form-textarea"
                                            rows="3" placeholder="Brief description of the product..."></textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="quickProductImage">Product Image</label>

                                        <!-- Tab Navigation for Image Input -->
                                        <div class="image-input-tabs">
                                            <button type="button" class="tab-btn active"
                                                onclick="switchImageInputTab('upload')" id="uploadTab">Upload
                                                File</button>
                                            <button type="button" class="tab-btn" onclick="switchImageInputTab('url')"
                                                id="urlTab">Image URL</button>
                                        </div>

                                        <!-- File Upload Section -->
                                        <div id="uploadSection" class="image-input-section">
                                            <div class="file-upload-area"
                                                onclick="document.getElementById('imageFileInput').click()">
                                                <div class="file-upload-content">
                                                    <i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
                                                    <p class="file-upload-text">Click to select an image file</p>
                                                    <p class="file-upload-hint">Supports: JPG, PNG, GIF, WebP (Max:
                                                        10MB)</p>
                                                </div>
                                                <input type="file" id="imageFileInput" name="imageFile" accept="image/*"
                                                    style="display: none;" onchange="handleFileSelect(this)">
                                            </div>
                                            <div id="filePreview" class="file-preview" style="display: none;">
                                                <img id="previewImage" src="" alt="Preview" class="preview-img">
                                                <div class="file-info">
                                                    <span id="fileName"></span>
                                                    <button type="button" class="remove-file-btn"
                                                        onclick="removeSelectedFile()"
                                                        aria-label="Remove selected file">
                                                        <i class="fas fa-times" aria-hidden="true"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- URL Input Section -->
                                        <div id="urlSection" class="image-input-section" style="display: none;">
                                            <input type="url" id="quickProductImage" name="imageUrl" class="form-input"
                                                placeholder="https://example.com/image.jpg">
                                            <div class="form-help">Enter a direct link to an image file</div>
                                        </div>
                                    </div>
                                </form>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" onclick="closeAddProductModal()">
                                        Cancel
                                    </button>
                                    <button type="submit" class="btn btn-primary" onclick="submitQuickAdd()">
                                        <i class="fas fa-plus" aria-hidden="true"></i>
                                        Add Product
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- Confirmation Modal -->
                        <div id="confirmModal" class="modal" role="dialog" aria-labelledby="confirmTitle"
                            aria-hidden="true" style="display: none;">
                            <div class="modal-backdrop" onclick="closeConfirmModal()"></div>
                            <div class="modal-content confirm-modal">
                                <div class="modal-header">
                                    <h3 id="confirmTitle">Confirm Action</h3>
                                </div>

                                <div class="modal-body">
                                    <div class="confirm-icon">
                                        <i class="fas fa-exclamation-triangle" aria-hidden="true"></i>
                                    </div>
                                    <p id="confirmMessage">Are you sure you want to perform this action?</p>
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" onclick="closeConfirmModal()">
                                        Cancel
                                    </button>
                                    <button type="button" id="confirmButton" class="btn btn-danger">
                                        Confirm
                                    </button>
                                </div>
                            </div>
                        </div>
                    </main>
                </div> <!-- End admin-main -->

                <script>
                    // Global variables
                    let currentSort = { column: null, direction: 'asc' };
                    let selectedProducts = new Set();
                    let currentView = 'table'; // 'table' or 'card'

                    // DOM Ready
                    document.addEventListener('DOMContentLoaded', function () {
                        initializeFilters();
                        initializeSearch();
                        initializeKeyboardNavigation();
                        initializeEnhancedUX();
                        initializeModals();
                        hideLoadingSpinner();
                    });

                    // Initialize Modals
                    function initializeModals() {
                        // Ensure modals are hidden on page load
                        const addModal = document.getElementById('addProductModal');
                        const confirmModal = document.getElementById('confirmModal');

                        if (addModal) {
                            addModal.style.display = 'none';
                            addModal.setAttribute('aria-hidden', 'true');
                        }

                        if (confirmModal) {
                            confirmModal.style.display = 'none';
                            confirmModal.setAttribute('aria-hidden', 'true');
                        }

                        // Remove any loading classes that might interfere
                        document.body.style.overflow = 'auto';
                    }

                    // Enhanced UX Initialization
                    function initializeEnhancedUX() {
                        initializeFilterCounter();
                        initializeTooltips();
                        initializeAccessibility();
                        updateResultsStatus();
                    }

                    // Filter Counter
                    function initializeFilterCounter() {
                        const filterElements = [
                            'productSearch', 'categoryFilter', 'stockFilter', 'statusFilter',
                            'priceMin', 'priceMax', 'dateFilter', 'brandFilter'
                        ];

                        filterElements.forEach(id => {
                            const element = document.getElementById(id);
                            if (element) {
                                element.addEventListener('change', updateActiveFiltersCount);
                                element.addEventListener('input', updateActiveFiltersCount);
                            }
                        });
                    }

                    function updateActiveFiltersCount() {
                        let activeCount = 0;
                        const statusElement = document.getElementById('filtersStatus');
                        const countElement = document.getElementById('activeFiltersCount');

                        // Count active filters
                        const filters = [
                            { id: 'productSearch', type: 'input' },
                            { id: 'categoryFilter', type: 'select' },
                            { id: 'stockFilter', type: 'select' },
                            { id: 'statusFilter', type: 'select' },
                            { id: 'priceMin', type: 'input' },
                            { id: 'priceMax', type: 'input' },
                            { id: 'dateFilter', type: 'select' },
                            { id: 'brandFilter', type: 'select' }
                        ];

                        filters.forEach(filter => {
                            const element = document.getElementById(filter.id);
                            if (element && element.value && element.value.trim() !== '') {
                                activeCount++;
                            }
                        });

                        // Update display
                        if (activeCount === 0) {
                            countElement.textContent = 'No filters applied';
                            statusElement.className = 'filters-status';
                        } else {
                            countElement.textContent = activeCount + ' filter' + (activeCount > 1 ? 's' : '') + ' applied';
                            statusElement.className = 'filters-status active';
                        }
                    }

                    // Enhanced View Switching
                    function switchView(viewType) {
                        currentView = viewType;
                        const tableBtn = document.getElementById('tableViewBtn');
                        const cardBtn = document.getElementById('cardViewBtn');

                        if (viewType === 'table') {
                            tableBtn.classList.add('active');
                            tableBtn.setAttribute('aria-selected', 'true');
                            cardBtn.classList.remove('active');
                            cardBtn.setAttribute('aria-selected', 'false');
                            showTableView();
                        } else {
                            cardBtn.classList.add('active');
                            cardBtn.setAttribute('aria-selected', 'true');
                            tableBtn.classList.remove('active');
                            tableBtn.setAttribute('aria-selected', 'false');
                            showCardView();
                        }

                        // Announce view change to screen readers
                        announceToScreenReader('View changed to ' + viewType + ' layout');
                    }

                    function showTableView() {
                        const container = document.getElementById('tableContainer');
                        container.className = 'table-container table-view';

                        // Restore original table HTML if needed
                        restoreTableViewHTML();
                    }

                    function restoreTableViewHTML() {
                        const container = document.getElementById('tableContainer');

                        // Check if we need to restore table view (if currently showing cards)
                        if (container.querySelector('.products-grid')) {
                            // Reload the page to restore table view for now
                            // In a production app, you'd store the original HTML or use a more sophisticated approach
                            location.reload();
                        }

                        announceToScreenReader('Switched to table view');
                    }

                    function showCardView() {
                        const container = document.getElementById('tableContainer');
                        container.className = 'table-container card-view';

                        // Generate card view HTML
                        generateCardViewHTML();
                    }

                    function generateCardViewHTML() {
                        const container = document.getElementById('tableContainer');
                        const products = document.querySelectorAll('.product-row');

                        if (products.length === 0) {
                            return; // Keep empty state
                        }

                        let cardsHTML = '<div class="products-grid">';

                        products.forEach(row => {
                            const productId = row.dataset.productId;
                            const img = row.querySelector('.product-image img');
                            const name = row.querySelector('.product-name').textContent;
                            const price = row.querySelector('.price-display').textContent;
                            const stock = row.querySelector('.stock-number').textContent;
                            const stockBadge = row.querySelector('.stock-badge');
                            const category = row.querySelector('.category-tag').textContent;
                            const statusBadge = row.querySelector('.status-badge');

                            cardsHTML += `
                                <div class="product-card" data-product-id="${productId}">
                                    <div class="product-card-header">
                                        <div class="product-card-checkbox">
                                            <input type="checkbox" id="card-product-${productId}" 
                                                   class="checkbox-input product-checkbox" 
                                                   value="${productId}" 
                                                   onchange="updateBulkSelection()"
                                                   aria-label="Select product ${name}">
                                            <label for="card-product-${productId}" class="checkbox-label"></label>
                                        </div>
                                        <div class="product-card-status">
                                            ${statusBadge.outerHTML}
                                        </div>
                                    </div>
                                    
                                    <div class="product-card-image">
                                        <img src="${img.src}" alt="${name}" loading="lazy">
                                    </div>
                                    
                                    <div class="product-card-content">
                                        <div class="product-card-category">${category}</div>
                                        <h3 class="product-card-title">${name}</h3>
                                        <div class="product-card-price">${price}</div>
                                        
                                        <div class="product-card-stock">
                                            <span class="stock-number">${stock}</span>
                                            ${stockBadge.outerHTML}
                                        </div>
                                    </div>
                                    
                                    <div class="product-card-actions">
                                        <button class="btn-icon btn-icon-primary" 
                                                onclick="viewProduct('${productId}')" 
                                                title="View product details"
                                                aria-label="View details for ${name}">
                                            <i class="fas fa-eye" aria-hidden="true"></i>
                                        </button>
                                        <button class="btn-icon btn-icon-secondary" 
                                                onclick="editProduct('${productId}')" 
                                                title="Edit product"
                                                aria-label="Edit ${name}">
                                            <i class="fas fa-edit" aria-hidden="true"></i>
                                        </button>
                                        <button class="btn-icon btn-icon-danger" 
                                                onclick="confirmDeleteProduct('${productId}', '${name}')" 
                                                title="Delete product"
                                                aria-label="Delete ${name}">
                                            <i class="fas fa-trash" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                </div>
                            `;
                        });

                        cardsHTML += '</div>';
                        container.innerHTML = cardsHTML;

                        announceToScreenReader('Switched to card view');
                    }

                    // Accessibility Enhancements
                    function initializeAccessibility() {
                        // Add keyboard navigation for custom elements
                        document.addEventListener('keydown', handleKeyboardNavigation);

                        // Improve focus management
                        enhanceFocusManagement();
                    }

                    function handleKeyboardNavigation(event) {
                        // Enhanced keyboard navigation
                        if (event.key === 'Tab') {
                            // Ensure proper focus indicators
                            setTimeout(() => {
                                const focused = document.activeElement;
                                if (focused && focused.classList.contains('btn')) {
                                    focused.style.outline = '2px solid #3b82f6';
                                    focused.style.outlineOffset = '2px';
                                }
                            }, 10);
                        }

                        // Quick access keys with better user feedback
                        if (event.ctrlKey || event.metaKey) {
                            switch (event.key) {
                                case 'k':
                                    event.preventDefault();
                                    document.getElementById('productSearch').focus();
                                    showNotification('Search focused (Ctrl+K)', 'info');
                                    announceToScreenReader('Search field focused');
                                    break;
                                case 'n':
                                    event.preventDefault();
                                    openAddProductModal();
                                    showNotification('Add product modal opened (Ctrl+N)', 'info');
                                    break;
                                case '/':
                                    event.preventDefault();
                                    // Show keyboard shortcuts help
                                    showKeyboardShortcuts();
                                    break;
                            }
                        }

                        // Escape key handling
                        if (event.key === 'Escape') {
                            // Close modals
                            closeAddProductModal();
                            closeConfirmModal();

                            // Clear focus from search if active
                            if (document.activeElement === document.getElementById('productSearch')) {
                                document.activeElement.blur();
                            }
                        }
                    }

                    function enhanceFocusManagement() {
                        // Remove outline on mouse interaction, add on keyboard
                        document.addEventListener('mousedown', () => {
                            document.body.classList.add('using-mouse');
                        });

                        document.addEventListener('keydown', (e) => {
                            if (e.key === 'Tab') {
                                document.body.classList.remove('using-mouse');
                            }
                        });
                    }

                    function announceToScreenReader(message) {
                        const announcement = document.createElement('div');
                        announcement.setAttribute('aria-live', 'polite');
                        announcement.setAttribute('aria-atomic', 'true');
                        announcement.className = 'sr-only';
                        announcement.textContent = message;

                        document.body.appendChild(announcement);

                        setTimeout(() => {
                            document.body.removeChild(announcement);
                        }, 1000);
                    }

                    // Enhanced Results Status
                    function updateResultsStatus() {
                        const timestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                        const metaElements = document.querySelectorAll('.results-meta');
                        metaElements.forEach(el => {
                            if (el.textContent.includes('Updated')) {
                                el.innerHTML = '<i class="fas fa-clock" aria-hidden="true"></i> Updated at ' + timestamp;
                            }
                        });
                    }

                    // Enhanced Tooltips
                    function initializeTooltips() {
                        const elementsWithTooltips = document.querySelectorAll('[title]');
                        elementsWithTooltips.forEach(element => {
                            element.addEventListener('mouseenter', showTooltip);
                            element.addEventListener('mouseleave', hideTooltip);
                            element.addEventListener('focus', showTooltip);
                            element.addEventListener('blur', hideTooltip);
                        });
                    }

                    function showTooltip(event) {
                        const element = event.target;
                        const title = element.getAttribute('title');
                        if (!title) return;

                        // Create tooltip
                        const tooltip = document.createElement('div');
                        tooltip.className = 'tooltip';
                        tooltip.textContent = title;
                        tooltip.id = 'tooltip-' + Date.now();

                        document.body.appendChild(tooltip);

                        // Position tooltip
                        const rect = element.getBoundingClientRect();
                        tooltip.style.position = 'absolute';
                        tooltip.style.top = (rect.top + window.scrollY - tooltip.offsetHeight - 5) + 'px';
                        tooltip.style.left = (rect.left + window.scrollX + rect.width / 2 - tooltip.offsetWidth / 2) + 'px';

                        // Store reference
                        element._tooltip = tooltip;

                        // Add ARIA description
                        element.setAttribute('aria-describedby', tooltip.id);
                    }

                    function hideTooltip(event) {
                        const element = event.target;
                        if (element._tooltip) {
                            element.removeAttribute('aria-describedby');
                            document.body.removeChild(element._tooltip);
                            element._tooltip = null;
                        }
                    }

                    // Loading spinner
                    function showLoadingSpinner() {
                        document.getElementById('loadingSpinner').style.display = 'flex';
                    }

                    function hideLoadingSpinner() {
                        document.getElementById('loadingSpinner').style.display = 'none';
                    }

                    // Modal functions
                    function openAddProductModal() {
                        const modal = document.getElementById('addProductModal');
                        modal.style.display = 'flex';
                        modal.setAttribute('aria-hidden', 'false');
                        document.body.style.overflow = 'hidden';

                        // Focus on first input
                        setTimeout(() => {
                            document.getElementById('quickProductName').focus();
                        }, 100);
                    }

                    function closeAddProductModal() {
                        const modal = document.getElementById('addProductModal');
                        modal.style.display = 'none';
                        modal.setAttribute('aria-hidden', 'true');
                        document.body.style.overflow = 'auto';

                        // Reset form
                        document.getElementById('quickAddForm').reset();
                        clearFormErrors();
                    }

                    // Form validation
                    function validateForm() {
                        let isValid = true;
                        clearFormErrors();

                        const name = document.getElementById('quickProductName').value.trim();
                        const price = document.getElementById('quickProductPrice').value;
                        const stock = document.getElementById('quickProductStock').value;

                        if (!name) {
                            showFieldError('nameError', 'Product name is required');
                            isValid = false;
                        }

                        if (!price || parseFloat(price) <= 0) {
                            showFieldError('priceError', 'Price must be greater than 0');
                            isValid = false;
                        }

                        if (!stock || parseInt(stock) < 0) {
                            showFieldError('stockError', 'Stock quantity cannot be negative');
                            isValid = false;
                        }

                        return isValid;
                    }

                    function showFieldError(errorId, message) {
                        const errorElement = document.getElementById(errorId);
                        errorElement.textContent = message;
                        errorElement.style.display = 'block';
                    }

                    function clearFormErrors() {
                        const errors = document.querySelectorAll('.field-error');
                        errors.forEach(error => {
                            error.textContent = '';
                            error.style.display = 'none';
                        });
                    }

                    // Quick add product
                    function submitQuickAdd() {
                        console.log('=== SubmitQuickAdd Debug Info ===');

                        if (!validateForm()) {
                            console.log('Form validation failed');
                            return;
                        }

                        const form = document.getElementById('quickAddForm');

                        // Create form data manually to ensure all fields are included
                        const manualFormData = new FormData();
                        const nameInput = document.getElementById('quickProductName');
                        const priceInput = document.getElementById('quickProductPrice');
                        const stockInput = document.getElementById('quickProductStock');
                        const categoryInput = document.getElementById('quickProductCategory');
                        const descriptionInput = document.getElementById('quickProductDescription');
                        const imageInput = document.getElementById('quickProductImage');
                        const imageFileInput = document.getElementById('imageFileInput');

                        // Check which image input method is active
                        const uploadSection = document.getElementById('uploadSection');
                        const isFileUpload = uploadSection.style.display !== 'none';

                        console.log('Manual form data creation:');
                        console.log(`  name: '${nameInput.value}'`);
                        console.log(`  price: '${priceInput.value}'`);
                        console.log(`  stock: '${stockInput.value}'`);
                        console.log(`  category: '${categoryInput.value}'`);
                        console.log(`  description: '${descriptionInput.value}'`);
                        console.log(`  imageInput method: ${isFileUpload ? 'file upload' : 'url'}`);

                        manualFormData.append('name', nameInput.value.trim());
                        manualFormData.append('price', priceInput.value.trim());
                        manualFormData.append('stock', stockInput.value.trim());
                        manualFormData.append('category', categoryInput.value.trim());
                        manualFormData.append('description', descriptionInput.value.trim());

                        // Handle image input
                        if (isFileUpload && imageFileInput.files.length > 0) {
                            const file = imageFileInput.files[0];
                            console.log(`  imageFile: ${file.name} (${file.size} bytes, ${file.type})`);
                            manualFormData.append('imageFile', file);
                            manualFormData.append('imageUrl', ''); // Empty URL when using file upload
                        } else {
                            console.log(`  imageUrl: '${imageInput.value}'`);
                            manualFormData.append('imageUrl', imageInput.value.trim());
                        }

                        console.log('===================================');

                        showLoadingSpinner();

                        fetch('${pageContext.request.contextPath}/admin/product/save', {
                            method: 'POST',
                            body: manualFormData  // Use manually created FormData
                        })
                            .then(response => {
                                console.log('Response status:', response.status);
                                console.log('Response headers:', response.headers);
                                return response.json();
                            })
                            .then(data => {
                                console.log('Response data:', data);
                                hideLoadingSpinner();
                                if (data.success) {
                                    showNotification('Product added successfully!', 'success');
                                    closeAddProductModal();
                                    // Reload page to show new product
                                    setTimeout(() => {
                                        window.location.reload();
                                    }, 1000);
                                } else {
                                    showNotification(data.message || 'Failed to add product', 'error');
                                }
                            })
                            .catch(error => {
                                hideLoadingSpinner();
                                console.error('Fetch error:', error);
                                showNotification('Error adding product: ' + error.message, 'error');
                            });
                    }

                    // Product actions
                    function viewProduct(productId) {
                        window.open('${pageContext.request.contextPath}/product/' + productId, '_blank');
                    }

                    function editProduct(productId) {
                        window.location.href = '${pageContext.request.contextPath}/admin/product/' + productId;
                    }

                    function confirmDeleteProduct(productId, productName) {
                        const modal = document.getElementById('confirmModal');
                        const message = document.getElementById('confirmMessage');
                        const confirmBtn = document.getElementById('confirmButton');

                        message.innerHTML = 'Are you sure you want to delete "<strong>' + productName + '</strong>"?<br><small>This action cannot be undone.</small>';

                        confirmBtn.onclick = () => deleteProduct(productId);

                        modal.style.display = 'flex';
                        modal.setAttribute('aria-hidden', 'false');
                    }

                    function closeConfirmModal() {
                        const modal = document.getElementById('confirmModal');
                        modal.style.display = 'none';
                        modal.setAttribute('aria-hidden', 'true');
                    }

                    function deleteProduct(productId) {
                        closeConfirmModal();
                        showLoadingSpinner();

                        fetch('${pageContext.request.contextPath}/admin/product/delete', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: 'id=' + productId
                        })
                            .then(response => response.json())
                            .then(data => {
                                hideLoadingSpinner();
                                if (data.success) {
                                    showNotification('Product deleted successfully', 'success');
                                    // Remove the row from the table
                                    const row = document.querySelector('tr[data-product-id="' + productId + '"]');
                                    if (row) {
                                        row.style.animation = 'fadeOut 0.3s ease-out';
                                        setTimeout(() => row.remove(), 300);
                                    }
                                } else {
                                    showNotification(data.message || 'Failed to delete product', 'error');
                                }
                            })
                            .catch(error => {
                                hideLoadingSpinner();
                                console.error('Error:', error);
                                showNotification('Error deleting product', 'error');
                            });
                    }

                    // Selection functions
                    function toggleSelectAll(checkbox) {
                        const productCheckboxes = document.querySelectorAll('.product-checkbox');
                        productCheckboxes.forEach(cb => {
                            cb.checked = checkbox.checked;
                            if (checkbox.checked) {
                                selectedProducts.add(cb.value);
                            } else {
                                selectedProducts.delete(cb.value);
                            }
                        });
                        updateBulkSelection();
                    }

                    function updateBulkSelection() {
                        const checkboxes = document.querySelectorAll('.product-checkbox');
                        const checkedBoxes = document.querySelectorAll('.product-checkbox:checked');
                        const selectAllCheckbox = document.getElementById('selectAll');

                        // Update selectedProducts set
                        selectedProducts.clear();
                        checkedBoxes.forEach(cb => selectedProducts.add(cb.value));

                        // Update select all checkbox
                        if (checkedBoxes.length === 0) {
                            selectAllCheckbox.indeterminate = false;
                            selectAllCheckbox.checked = false;
                        } else if (checkedBoxes.length === checkboxes.length) {
                            selectAllCheckbox.indeterminate = false;
                            selectAllCheckbox.checked = true;
                        } else {
                            selectAllCheckbox.indeterminate = true;
                        }

                        // Update bulk actions panel
                        updateBulkActionsPanel();
                    }

                    function updateBulkActionsPanel() {
                        const panel = document.getElementById('bulk-actions');
                        const count = document.getElementById('bulkSelectedCount');

                        if (selectedProducts.size > 0) {
                            count.textContent = selectedProducts.size + ' item' + (selectedProducts.size > 1 ? 's' : '') + ' selected';
                            panel.style.display = 'block';
                        } else {
                            panel.style.display = 'none';
                        }
                    }

                    // Filter functions
                    function initializeFilters() {
                        document.getElementById('categoryFilter').addEventListener('change', performSearch);
                        document.getElementById('stockFilter').addEventListener('change', performSearch);
                        document.getElementById('statusFilter').addEventListener('change', performSearch);
                    }

                    function initializeSearch() {
                        const searchInput = document.getElementById('productSearch');
                        const clearBtn = document.getElementById('clearSearchBtn');

                        searchInput.addEventListener('input', function () {
                            if (this.value.length > 0) {
                                clearBtn.style.display = 'block';
                            } else {
                                clearBtn.style.display = 'none';
                            }
                        });

                        searchInput.addEventListener('keypress', function (e) {
                            if (e.key === 'Enter') {
                                performSearch();
                            }
                        });

                        clearBtn.addEventListener('click', function () {
                            searchInput.value = '';
                            this.style.display = 'none';
                            performSearch();
                        });
                    }

                    function performSearch() {
                        const query = document.getElementById('productSearch').value;
                        const category = document.getElementById('categoryFilter').value;
                        const stock = document.getElementById('stockFilter').value;
                        const status = document.getElementById('statusFilter').value;

                        let url = '${pageContext.request.contextPath}/admin/products?';
                        if (query) url += 'search=' + encodeURIComponent(query) + '&';
                        if (category) url += 'category=' + encodeURIComponent(category) + '&';
                        if (stock) url += 'stock=' + encodeURIComponent(stock) + '&';
                        if (status) url += 'status=' + encodeURIComponent(status) + '&';

                        window.location.href = url;
                    }

                    function clearAllFilters() {
                        document.getElementById('productSearch').value = '';
                        document.getElementById('categoryFilter').value = '';
                        document.getElementById('stockFilter').value = '';
                        document.getElementById('statusFilter').value = '';
                        document.getElementById('clearSearchBtn').style.display = 'none';
                        window.location.href = '${pageContext.request.contextPath}/admin/products';
                    }

                    function toggleAdvancedFilters() {
                        const panel = document.getElementById('advanced-filters');
                        const button = event.target.closest('button');

                        if (panel.style.display === 'none') {
                            panel.style.display = 'block';
                            button.setAttribute('aria-expanded', 'true');
                        } else {
                            panel.style.display = 'none';
                            button.setAttribute('aria-expanded', 'false');
                        }
                    }

                    // Bulk actions
                    function toggleBulkActions() {
                        const panel = document.getElementById('bulk-actions');
                        const button = event.target.closest('button');

                        if (selectedProducts.size === 0) {
                            showNotification('Please select products first', 'warning');
                            return;
                        }

                        if (panel.style.display === 'none') {
                            panel.style.display = 'block';
                            button.setAttribute('aria-expanded', 'true');
                        } else {
                            panel.style.display = 'none';
                            button.setAttribute('aria-expanded', 'false');
                        }
                    }

                    function bulkDelete() {
                        if (selectedProducts.size === 0) {
                            showNotification('Please select products first', 'warning');
                            return;
                        }

                        const count = selectedProducts.size;
                        const modal = document.getElementById('confirmModal');
                        const message = document.getElementById('confirmMessage');
                        const confirmBtn = document.getElementById('confirmButton');

                        message.innerHTML = 'Are you sure you want to delete ' + count + ' product' + (count > 1 ? 's' : '') + '?<br><small>This action cannot be undone.</small>';

                        confirmBtn.onclick = () => {
                            closeConfirmModal();
                            showNotification('Bulk delete feature coming soon!', 'info');
                        };

                        modal.style.display = 'flex';
                    }

                    function bulkActivate() {
                        showNotification('Bulk activate feature coming soon!', 'info');
                    }

                    function bulkDeactivate() {
                        showNotification('Bulk deactivate feature coming soon!', 'info');
                    }

                    // Sorting
                    function sortTable(column) {
                        // Update sort direction
                        if (currentSort.column === column) {
                            currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
                        } else {
                            currentSort.column = column;
                            currentSort.direction = 'asc';
                        }

                        // Update sort icons
                        updateSortIcons();

                        // For now, just show notification - real sorting would be implemented server-side
                        showNotification('Sorting by ' + column + ' (' + currentSort.direction + ')', 'info');
                    }

                    function updateSortIcons() {
                        // Reset all sort icons
                        document.querySelectorAll('.sort-icon').forEach(icon => {
                            icon.className = 'fas fa-sort sort-icon';
                        });

                        // Update current sort icon
                        if (currentSort.column) {
                            const sortableHeader = document.querySelector('th[onclick="sortTable(\'' + currentSort.column + '\')"] .sort-icon');
                            if (sortableHeader) {
                                sortableHeader.className = 'fas fa-sort-' + (currentSort.direction == 'asc' ? 'up' : 'down') + ' sort-icon';
                            }
                        }
                    }

                    // Pagination
                    function goToPage(page) {
                        if (page < 1) return;

                        const currentUrl = new URL(window.location);
                        currentUrl.searchParams.set('page', page);
                        window.location.href = currentUrl.toString();
                    }

                    // Keyboard navigation
                    function initializeKeyboardNavigation() {
                        // Modal keyboard handling
                        document.addEventListener('keydown', function (e) {
                            if (e.key === 'Escape') {
                                closeAddProductModal();
                                closeConfirmModal();
                            }
                        });
                    }

                    // Utility functions
                    function exportProducts() {
                        showNotification('Export feature coming soon!', 'info');
                    }

                    function showNotification(message, type = 'info') {
                        const container = document.getElementById('notificationContainer');
                        const notification = document.createElement('div');

                        notification.className = 'notification notification-' + type;
                        notification.innerHTML =
                            '<div class="notification-content">' +
                            '<i class="fas ' + getNotificationIcon(type) + '" aria-hidden="true"></i>' +
                            '<span>' + message + '</span>' +
                            '</div>' +
                            '<button class="notification-close" onclick="this.parentElement.remove()" aria-label="Close notification">' +
                            '<i class="fas fa-times" aria-hidden="true"></i>' +
                            '</button>';

                        container.appendChild(notification);

                        // Auto remove after 5 seconds
                        setTimeout(() => {
                            if (notification.parentElement) {
                                notification.style.animation = 'slideOut 0.3s ease-out';
                                setTimeout(() => notification.remove(), 300);
                            }
                        }, 5000);
                    }

                    function getNotificationIcon(type) {
                        switch (type) {
                            case 'success': return 'fa-check-circle';
                            case 'error': return 'fa-exclamation-circle';
                            case 'warning': return 'fa-exclamation-triangle';
                            default: return 'fa-info-circle';
                        }
                    }

                    // Image upload functions
                    function switchImageInputTab(tabType) {
                        const uploadTab = document.getElementById('uploadTab');
                        const urlTab = document.getElementById('urlTab');
                        const uploadSection = document.getElementById('uploadSection');
                        const urlSection = document.getElementById('urlSection');

                        // Reset active states
                        uploadTab.classList.remove('active');
                        urlTab.classList.remove('active');

                        if (tabType === 'upload') {
                            uploadTab.classList.add('active');
                            uploadSection.style.display = 'block';
                            urlSection.style.display = 'none';
                        } else {
                            urlTab.classList.add('active');
                            uploadSection.style.display = 'none';
                            urlSection.style.display = 'block';
                        }
                    }

                    function handleFileSelect(input) {
                        const file = input.files[0];
                        if (!file) return;

                        // Validate file type
                        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
                        if (!allowedTypes.includes(file.type)) {
                            showNotification('Please select a valid image file (JPG, PNG, GIF, WebP)', 'error');
                            input.value = ''; // Clear the input
                            return;
                        }

                        // Validate file size (10MB max)
                        const maxSize = 10 * 1024 * 1024; // 10MB in bytes
                        if (file.size > maxSize) {
                            showNotification('File size must be less than 10MB', 'error');
                            input.value = ''; // Clear the input
                            return;
                        }

                        // Show file preview
                        const reader = new FileReader();
                        reader.onload = function (e) {
                            const preview = document.getElementById('filePreview');
                            const previewImg = document.getElementById('previewImage');
                            const fileName = document.getElementById('fileName');

                            previewImg.src = e.target.result;
                            fileName.textContent = file.name;
                            preview.style.display = 'flex';

                            // Hide the upload area
                            document.querySelector('.file-upload-area').style.display = 'none';
                        };
                        reader.readAsDataURL(file);
                    }

                    function removeSelectedFile() {
                        const input = document.getElementById('imageFileInput');
                        const preview = document.getElementById('filePreview');
                        const uploadArea = document.querySelector('.file-upload-area');

                        // Clear the input
                        input.value = '';

                        // Hide preview and show upload area
                        preview.style.display = 'none';
                        uploadArea.style.display = 'block';
                    }

                    // Keyboard shortcuts help
                    function showKeyboardShortcuts() {
                        const shortcuts = [
                            { key: 'Ctrl + K', description: 'Focus search field' },
                            { key: 'Ctrl + N', description: 'Add new product' },
                            { key: 'Ctrl + /', description: 'Show keyboard shortcuts' },
                            { key: 'Escape', description: 'Close modals or clear focus' },
                            { key: 'Tab', description: 'Navigate between elements' },
                            { key: 'Enter', description: 'Activate focused element' }
                        ];

                        let message = '<div style="text-align: left;"><h4 style="margin: 0 0 1rem 0;">Keyboard Shortcuts:</h4>';
                        shortcuts.forEach(shortcut => {
                            message += `<div style="margin: 0.5rem 0;"><strong>${shortcut.key}</strong> - ${shortcut.description}</div>`;
                        });
                        message += '</div>';

                        // Create a temporary modal-like notification
                        const notification = document.createElement('div');
                        notification.className = 'keyboard-shortcuts-modal';
                        notification.innerHTML = `
                            <div class="keyboard-shortcuts-content">
                                ${message}
                                <button onclick="this.parentElement.parentElement.remove()" 
                                        class="btn btn-primary" style="margin-top: 1rem;">
                                    Got it!
                                </button>
                            </div>
                        `;

                        document.body.appendChild(notification);

                        // Auto remove after 10 seconds
                        setTimeout(() => {
                            if (notification.parentElement) {
                                notification.remove();
                            }
                        }, 10000);
                    }
                </script>

                <style>
                    /* Enhanced UX/UI Styles */

                    /* Header Enhancements */
                    .page-header {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        padding: 2.5rem 2rem;
                        border-radius: 1rem;
                        margin-bottom: 2rem;
                        box-shadow: 0 10px 25px rgba(102, 126, 234, 0.15);
                    }

                    .header-content {
                        display: flex;
                        justify-content: space-between;
                        align-items: flex-start;
                        gap: 2rem;
                    }

                    .header-info {
                        flex: 1;
                    }

                    .breadcrumb {
                        margin-bottom: 1rem;
                    }

                    .breadcrumb-list {
                        display: flex;
                        list-style: none;
                        padding: 0;
                        margin: 0;
                        gap: 0.5rem;
                        align-items: center;
                    }

                    .breadcrumb-list li {
                        color: rgba(255, 255, 255, 0.8);
                        font-size: 0.875rem;
                    }

                    .breadcrumb-list li:not(:last-child)::after {
                        content: '>';
                        margin-left: 0.5rem;
                        opacity: 0.6;
                    }

                    .breadcrumb-link {
                        color: rgba(255, 255, 255, 0.9);
                        text-decoration: none;
                        transition: color 0.2s ease;
                    }

                    .breadcrumb-link:hover {
                        color: white;
                    }

                    .breadcrumb-current {
                        color: white;
                        font-weight: 500;
                    }

                    .page-title {
                        font-size: 2.5rem;
                        font-weight: 700;
                        margin: 0 0 0.5rem 0;
                        display: flex;
                        align-items: center;
                        gap: 0.75rem;
                    }

                    .page-description {
                        font-size: 1.125rem;
                        opacity: 0.9;
                        margin: 0;
                        font-weight: 300;
                    }

                    .header-actions {
                        display: flex;
                        gap: 1rem;
                        align-items: flex-start;
                    }

                    /* Enhanced Button Styles */
                    .btn {
                        display: inline-flex;
                        align-items: center;
                        justify-content: center;
                        gap: 0.5rem;
                        padding: 0.75rem 1.5rem;
                        border: none;
                        border-radius: 0.5rem;
                        font-weight: 500;
                        font-size: 0.875rem;
                        line-height: 1;
                        cursor: pointer;
                        transition: all 0.2s ease;
                        text-decoration: none;
                        position: relative;
                        overflow: hidden;
                    }

                    .btn-icon-text {
                        gap: 0.5rem;
                    }

                    .btn-icon-text i {
                        font-size: 0.875rem;
                    }

                    .btn-primary {
                        background: #3b82f6;
                        color: white;
                        box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
                    }

                    .btn-primary:hover {
                        background: #2563eb;
                        transform: translateY(-1px);
                        box-shadow: 0 8px 16px rgba(59, 130, 246, 0.3);
                    }

                    .btn-outline {
                        background: rgba(255, 255, 255, 0.1);
                        color: white;
                        border: 1px solid rgba(255, 255, 255, 0.2);
                        backdrop-filter: blur(10px);
                    }

                    .btn-outline:hover {
                        background: rgba(255, 255, 255, 0.2);
                        border-color: rgba(255, 255, 255, 0.3);
                        transform: translateY(-1px);
                    }

                    .btn-secondary {
                        background: #6b7280;
                        color: white;
                    }

                    .btn-secondary:hover {
                        background: #4b5563;
                        transform: translateY(-1px);
                    }

                    .btn-sm {
                        padding: 0.5rem 1rem;
                        font-size: 0.8125rem;
                    }

                    /* Enhanced Filters Section */
                    .filters-section {
                        margin-bottom: 2rem;
                    }

                    .filters-card {
                        background: white;
                        border-radius: 1rem;
                        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
                        overflow: hidden;
                        border: 1px solid #e5e7eb;
                    }

                    .filters-header {
                        background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
                        padding: 1.5rem 2rem;
                        border-bottom: 1px solid #e5e7eb;
                    }

                    .filters-title {
                        font-size: 1.25rem;
                        font-weight: 600;
                        margin: 0 0 0.5rem 0;
                        color: #1f2937;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .filters-status {
                        font-size: 0.875rem;
                        color: #6b7280;
                    }

                    .active-filters-count {
                        font-weight: 500;
                    }

                    .filters-container {
                        padding: 2rem;
                    }

                    .search-group {
                        margin-bottom: 2rem;
                    }

                    .search-label {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        font-weight: 500;
                        color: #374151;
                        margin-bottom: 0.75rem;
                        font-size: 0.875rem;
                    }

                    .search-box {
                        position: relative;
                        display: flex;
                        align-items: center;
                    }

                    .search-input {
                        flex: 1;
                        padding: 0.875rem 1rem;
                        padding-right: 5rem;
                        border: 2px solid #e5e7eb;
                        border-radius: 0.75rem;
                        font-size: 1rem;
                        transition: all 0.2s ease;
                        background: #f9fafb;
                    }

                    .search-input:focus {
                        outline: none;
                        border-color: #3b82f6;
                        background: white;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }

                    .search-btn,
                    .search-clear-btn {
                        position: absolute;
                        right: 0.5rem;
                        background: none;
                        border: none;
                        padding: 0.5rem;
                        cursor: pointer;
                        color: #6b7280;
                        border-radius: 0.375rem;
                        transition: all 0.2s ease;
                    }

                    .search-clear-btn {
                        right: 2.5rem;
                    }

                    .search-btn:hover,
                    .search-clear-btn:hover {
                        background: #f3f4f6;
                        color: #374151;
                    }

                    .form-help {
                        margin-top: 0.5rem;
                        font-size: 0.8125rem;
                        color: #6b7280;
                        display: flex;
                        align-items: center;
                        gap: 0.375rem;
                    }

                    .filters-row {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                        gap: 1.5rem;
                        align-items: end;
                    }

                    .filter-group {
                        display: flex;
                        flex-direction: column;
                    }

                    .filter-label {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        font-weight: 500;
                        color: #374151;
                        margin-bottom: 0.5rem;
                        font-size: 0.875rem;
                    }

                    .form-select,
                    .form-input {
                        padding: 0.75rem 1rem;
                        border: 1px solid #d1d5db;
                        border-radius: 0.5rem;
                        font-size: 0.875rem;
                        transition: all 0.2s ease;
                        background: white;
                    }

                    .form-select:focus,
                    .form-input:focus {
                        outline: none;
                        border-color: #3b82f6;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }

                    .filter-actions {
                        display: flex;
                        gap: 0.75rem;
                        align-items: center;
                    }

                    .filter-actions .btn {
                        white-space: nowrap;
                    }

                    /* Advanced Filters */
                    .advanced-filters {
                        border-top: 1px solid #e5e7eb;
                        background: #f8fafc;
                    }

                    .advanced-filters-header {
                        padding: 1.5rem 2rem 1rem 2rem;
                    }

                    .advanced-filters-title {
                        font-size: 1.125rem;
                        font-weight: 600;
                        margin: 0 0 0.5rem 0;
                        color: #1f2937;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .advanced-filters-description {
                        font-size: 0.875rem;
                        color: #6b7280;
                        margin: 0;
                    }

                    .advanced-filters-grid {
                        padding: 0 2rem 2rem 2rem;
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                        gap: 1.5rem;
                    }

                    /* Enhanced Table Section */
                    .products-table-section {
                        margin-bottom: 2rem;
                    }

                    .table-card {
                        background: white;
                        border-radius: 1rem;
                        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
                        overflow: hidden;
                        border: 1px solid #e5e7eb;
                    }

                    .table-header {
                        background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
                        padding: 1.5rem 2rem;
                        border-bottom: 1px solid #e5e7eb;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        flex-wrap: wrap;
                        gap: 1rem;
                    }

                    .table-title-group {
                        display: flex;
                        flex-direction: column;
                        gap: 0.5rem;
                    }

                    .table-title {
                        font-size: 1.5rem;
                        font-weight: 600;
                        margin: 0;
                        color: #1f2937;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .results-summary {
                        display: flex;
                        flex-direction: column;
                        gap: 0.25rem;
                    }

                    .results-count {
                        font-size: 0.875rem;
                        color: #374151;
                    }

                    .results-count.no-results {
                        color: #dc2626;
                    }

                    .results-meta {
                        font-size: 0.75rem;
                        color: #6b7280;
                        display: flex;
                        align-items: center;
                        gap: 0.25rem;
                    }

                    .table-actions {
                        display: flex;
                        gap: 1rem;
                        align-items: center;
                        flex-wrap: wrap;
                    }

                    .view-controls {
                        display: flex;
                        background: #f3f4f6;
                        border-radius: 0.5rem;
                        padding: 0.25rem;
                    }

                    .view-toggle {
                        background: none;
                        border: none;
                        padding: 0.5rem;
                        cursor: pointer;
                        border-radius: 0.375rem;
                        color: #6b7280;
                        transition: all 0.2s ease;
                    }

                    .view-toggle.active {
                        background: white;
                        color: #3b82f6;
                        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
                    }

                    .view-toggle:hover:not(.active) {
                        color: #374151;
                    }

                    .action-buttons {
                        display: flex;
                        gap: 0.75rem;
                    }

                    /* Ensure proper admin layout */
                    .admin-sidebar {
                        position: fixed;
                        left: 0;
                        top: 0;
                        width: 250px;
                        height: 100vh;
                        background: #ffffff;
                        border-right: 1px solid #e5e7eb;
                        overflow-y: auto;
                        z-index: 1000;
                        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
                    }

                    .admin-main {
                        margin-left: 250px;
                        min-height: 100vh;
                        background: #f9fafb;
                    }

                    .admin-content {
                        padding: 2rem;
                        max-width: none;
                        margin: 0;
                    }

                    /* Navigation styles */
                    .admin-nav {
                        list-style: none;
                        padding: 0;
                        margin: 0;
                    }

                    .admin-nav li {
                        margin: 0;
                    }

                    .admin-nav a {
                        display: flex;
                        align-items: center;
                        gap: 0.75rem;
                        padding: 1rem 1.5rem;
                        color: #6b7280;
                        text-decoration: none;
                        transition: all 0.2s ease;
                        border-left: 3px solid transparent;
                    }

                    .admin-nav a:hover {
                        background: #f3f4f6;
                        color: #111827;
                        border-left-color: #3b82f6;
                    }

                    .admin-nav a.active {
                        background: #3b82f6;
                        color: white;
                        border-left-color: #3b82f6;
                    }

                    /* Animations */
                    @keyframes slideIn {
                        from {
                            opacity: 0;
                            transform: translateY(-10px);
                        }

                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    @keyframes slideOut {
                        from {
                            opacity: 1;
                            transform: translateY(0);
                        }

                        to {
                            opacity: 0;
                            transform: translateY(-10px);
                        }
                    }

                    @keyframes fadeOut {
                        from {
                            opacity: 1;
                        }

                        to {
                            opacity: 0;
                        }
                    }

                    /* Card View Styles */
                    .products-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                        gap: 1.5rem;
                        padding: 2rem;
                    }

                    .product-card {
                        background: white;
                        border-radius: 1rem;
                        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                        transition: all 0.3s ease;
                        border: 1px solid #e5e7eb;
                        position: relative;
                    }

                    .product-card:hover {
                        transform: translateY(-4px);
                        box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
                    }

                    .product-card-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 1rem 1rem 0.5rem 1rem;
                    }

                    .product-card-checkbox {
                        display: flex;
                        align-items: center;
                    }

                    .product-card-status .status-badge {
                        font-size: 0.75rem;
                        padding: 0.25rem 0.5rem;
                    }

                    .product-card-image {
                        height: 200px;
                        overflow: hidden;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        background: #f8fafc;
                        margin: 0 1rem;
                        border-radius: 0.5rem;
                    }

                    .product-card-image img {
                        width: 100%;
                        height: 100%;
                        object-fit: cover;
                        transition: transform 0.3s ease;
                    }

                    .product-card:hover .product-card-image img {
                        transform: scale(1.05);
                    }

                    .product-card-content {
                        padding: 1rem;
                    }

                    .product-card-category {
                        font-size: 0.75rem;
                        font-weight: 500;
                        color: #6b7280;
                        text-transform: uppercase;
                        letter-spacing: 0.025em;
                        margin-bottom: 0.5rem;
                    }

                    .product-card-title {
                        font-size: 1.125rem;
                        font-weight: 600;
                        color: #1f2937;
                        margin: 0 0 0.75rem 0;
                        line-height: 1.4;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                        line-clamp: 2;
                        overflow: hidden;
                    }

                    .product-card-price {
                        font-size: 1.5rem;
                        font-weight: 700;
                        color: #059669;
                        margin-bottom: 1rem;
                    }

                    .product-card-stock {
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        margin-bottom: 1rem;
                    }

                    .product-card-stock .stock-number {
                        font-weight: 600;
                        color: #374151;
                    }

                    .product-card-stock .stock-badge {
                        font-size: 0.75rem;
                        padding: 0.25rem 0.5rem;
                    }

                    .product-card-actions {
                        display: flex;
                        gap: 0.5rem;
                        padding: 0 1rem 1rem 1rem;
                        justify-content: center;
                    }

                    .product-card-actions .btn-icon {
                        width: 36px;
                        height: 36px;
                        border-radius: 0.5rem;
                    }

                    /* Enhanced button styles for cards */
                    .btn-icon-primary {
                        background: #3b82f6;
                        color: white;
                    }

                    .btn-icon-primary:hover {
                        background: #2563eb;
                        transform: scale(1.05);
                    }

                    .btn-icon-secondary {
                        background: #6b7280;
                        color: white;
                    }

                    .btn-icon-secondary:hover {
                        background: #4b5563;
                        transform: scale(1.05);
                    }

                    .btn-icon-danger {
                        background: #dc2626;
                        color: white;
                    }

                    .btn-icon-danger:hover {
                        background: #b91c1c;
                        transform: scale(1.05);
                    }

                    /* Enhanced Checkbox Styles */
                    .checkbox-wrapper {
                        position: relative;
                        display: inline-block;
                    }

                    .checkbox-input {
                        opacity: 0;
                        position: absolute;
                        z-index: -1;
                    }

                    .checkbox-label {
                        display: inline-block;
                        width: 18px;
                        height: 18px;
                        border: 2px solid #d1d5db;
                        border-radius: 0.25rem;
                        background: white;
                        cursor: pointer;
                        transition: all 0.2s ease;
                        position: relative;
                    }

                    .checkbox-label::after {
                        content: '';
                        position: absolute;
                        top: 2px;
                        left: 5px;
                        width: 6px;
                        height: 10px;
                        border: 2px solid white;
                        border-top: none;
                        border-left: none;
                        transform: rotate(45deg) scale(0);
                        transition: transform 0.2s ease;
                    }

                    .checkbox-input:checked+.checkbox-label {
                        background: #3b82f6;
                        border-color: #3b82f6;
                    }

                    .checkbox-input:checked+.checkbox-label::after {
                        transform: rotate(45deg) scale(1);
                    }

                    .checkbox-input:focus+.checkbox-label {
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }

                    /* Tooltip Styles */
                    .tooltip {
                        position: absolute;
                        background: #1f2937;
                        color: white;
                        padding: 0.5rem 0.75rem;
                        border-radius: 0.375rem;
                        font-size: 0.8125rem;
                        font-weight: 500;
                        white-space: nowrap;
                        z-index: 1000;
                        pointer-events: none;
                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
                    }

                    .tooltip::before {
                        content: '';
                        position: absolute;
                        top: 100%;
                        left: 50%;
                        transform: translateX(-50%);
                        border: 4px solid transparent;
                        border-top-color: #1f2937;
                    }

                    /* Focus Indicator Improvements */
                    .using-mouse *:focus {
                        outline: none !important;
                    }

                    *:focus-visible {
                        outline: 2px solid #3b82f6 !important;
                        outline-offset: 2px !important;
                    }

                    /* Loading Spinner */
                    .loading-spinner {
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background: rgba(0, 0, 0, 0.5);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        z-index: 9999;
                        backdrop-filter: blur(4px);
                    }

                    .spinner {
                        width: 48px;
                        height: 48px;
                        border: 4px solid rgba(255, 255, 255, 0.2);
                        border-top: 4px solid white;
                        border-radius: 50%;
                        animation: spin 1s linear infinite;
                    }

                    @keyframes spin {
                        to {
                            transform: rotate(360deg);
                        }
                    }

                    /* Notification Container */
                    .notification-container {
                        position: fixed;
                        top: 2rem;
                        right: 2rem;
                        z-index: 9999;
                        display: flex;
                        flex-direction: column;
                        gap: 0.75rem;
                        max-width: 400px;
                    }

                    .notification {
                        background: white;
                        border-radius: 0.75rem;
                        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
                        padding: 1rem;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        border-left: 4px solid;
                        animation: slideIn 0.3s ease-out;
                    }

                    .notification-success {
                        border-left-color: #059669;
                    }

                    .notification-error {
                        border-left-color: #dc2626;
                    }

                    .notification-warning {
                        border-left-color: #d97706;
                    }

                    .notification-info {
                        border-left-color: #3b82f6;
                    }

                    .notification-content {
                        display: flex;
                        align-items: center;
                        gap: 0.75rem;
                        flex: 1;
                    }

                    .notification-close {
                        background: none;
                        border: none;
                        color: #6b7280;
                        cursor: pointer;
                        padding: 0.25rem;
                        border-radius: 0.25rem;
                        transition: all 0.2s ease;
                    }

                    .notification-close:hover {
                        background: #f3f4f6;
                        color: #374151;
                    }

                    /* Screen Reader Only */
                    .sr-only {
                        position: absolute;
                        width: 1px;
                        height: 1px;
                        padding: 0;
                        margin: -1px;
                        overflow: hidden;
                        clip: rect(0, 0, 0, 0);
                        white-space: nowrap;
                        border: 0;
                    }

                    /* Keyboard Shortcuts Modal */
                    .keyboard-shortcuts-modal {
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background: rgba(0, 0, 0, 0.5);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        z-index: 10000;
                        backdrop-filter: blur(4px);
                        animation: slideIn 0.3s ease-out;
                    }

                    .keyboard-shortcuts-content {
                        background: white;
                        padding: 2rem;
                        border-radius: 1rem;
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
                        max-width: 400px;
                        width: 90%;
                        text-align: center;
                    }

                    /* Enhanced Alert Styles */
                    .alert {
                        display: flex;
                        align-items: flex-start;
                        justify-content: space-between;
                        padding: 1rem 1.25rem;
                        margin-bottom: 1.5rem;
                        border-radius: 0.75rem;
                        border-left: 4px solid;
                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                        animation: slideIn 0.3s ease-out;
                    }

                    .alert-content {
                        display: flex;
                        align-items: flex-start;
                        gap: 0.75rem;
                        flex: 1;
                    }

                    .alert-text {
                        flex: 1;
                        line-height: 1.5;
                    }

                    .alert-text strong {
                        display: block;
                        margin-bottom: 0.25rem;
                        font-weight: 600;
                    }

                    .alert-close {
                        background: none;
                        border: none;
                        color: inherit;
                        cursor: pointer;
                        padding: 0.25rem;
                        border-radius: 0.25rem;
                        transition: all 0.2s ease;
                        opacity: 0.7;
                        margin-left: 1rem;
                    }

                    .alert-close:hover {
                        opacity: 1;
                        background: rgba(0, 0, 0, 0.1);
                    }

                    .alert-error {
                        background: #fef2f2;
                        color: #991b1b;
                        border-left-color: #dc2626;
                    }

                    .alert-success {
                        background: #f0fdf4;
                        color: #166534;
                        border-left-color: #16a34a;
                    }

                    .alert-warning {
                        background: #fffbeb;
                        color: #92400e;
                        border-left-color: #d97706;
                    }

                    .alert-info {
                        background: #eff6ff;
                        color: #1e40af;
                        border-left-color: #3b82f6;
                    }

                    /* Pagination Styles */
                    .pagination-section {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 1.5rem 2rem;
                        border-top: 1px solid #e5e7eb;
                        background: #f8fafc;
                    }

                    .pagination-info {
                        font-size: 0.875rem;
                        color: #6b7280;
                        font-weight: 500;
                    }

                    .pagination-nav {
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                    }

                    .pagination-btn {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        padding: 0.75rem 1rem;
                        background: white;
                        border: 1px solid #d1d5db;
                        border-radius: 0.5rem;
                        color: #374151;
                        font-size: 0.875rem;
                        font-weight: 500;
                        cursor: pointer;
                        transition: all 0.2s ease;
                        text-decoration: none;
                    }

                    .pagination-btn:hover:not([disabled]) {
                        background: #f3f4f6;
                        border-color: #9ca3af;
                        color: #111827;
                        transform: translateY(-1px);
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    }

                    .pagination-btn[disabled] {
                        opacity: 0.5;
                        cursor: not-allowed;
                        background: #f9fafb;
                        color: #9ca3af;
                    }

                    .current-page {
                        padding: 0.75rem 1rem;
                        background: #3b82f6;
                        color: white;
                        border-radius: 0.5rem;
                        font-weight: 600;
                        font-size: 0.875rem;
                        min-width: 80px;
                        text-align: center;
                    }

                    /* Modal Styles */
                    .modal {
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background: rgba(0, 0, 0, 0.5);
                        display: none;
                        align-items: center;
                        justify-content: center;
                        z-index: 10000;
                        backdrop-filter: blur(4px);
                        animation: fadeIn 0.3s ease-out;
                    }

                    .modal-backdrop {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        cursor: pointer;
                    }

                    .modal-content {
                        background: white;
                        border-radius: 1rem;
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
                        max-width: 600px;
                        width: 90%;
                        max-height: 90vh;
                        overflow-y: auto;
                        position: relative;
                        animation: slideIn 0.3s ease-out;
                    }

                    .modal-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 1.5rem 2rem;
                        border-bottom: 1px solid #e5e7eb;
                        background: #f8fafc;
                        border-radius: 1rem 1rem 0 0;
                    }

                    .modal-header h3 {
                        margin: 0;
                        font-size: 1.25rem;
                        font-weight: 600;
                        color: #1f2937;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .modal-close {
                        background: none;
                        border: none;
                        color: #6b7280;
                        cursor: pointer;
                        padding: 0.5rem;
                        border-radius: 0.375rem;
                        transition: all 0.2s ease;
                        font-size: 1.25rem;
                    }

                    .modal-close:hover {
                        background: #f3f4f6;
                        color: #374151;
                    }

                    .modal-body {
                        padding: 2rem;
                    }

                    .modal-footer {
                        display: flex;
                        justify-content: flex-end;
                        gap: 1rem;
                        padding: 1.5rem 2rem;
                        border-top: 1px solid #e5e7eb;
                        background: #f8fafc;
                        border-radius: 0 0 1rem 1rem;
                    }

                    /* Form Styles */
                    .form-row {
                        display: grid;
                        grid-template-columns: 1fr 1fr;
                        gap: 1.5rem;
                        margin-bottom: 1.5rem;
                    }

                    .form-group {
                        display: flex;
                        flex-direction: column;
                    }

                    .form-group label {
                        font-weight: 500;
                        color: #374151;
                        margin-bottom: 0.5rem;
                        font-size: 0.875rem;
                    }

                    .form-group label.required::after {
                        content: ' *';
                        color: #dc2626;
                        font-weight: 600;
                    }

                    .form-input,
                    .form-select,
                    .form-textarea {
                        padding: 0.75rem 1rem;
                        border: 1px solid #d1d5db;
                        border-radius: 0.5rem;
                        font-size: 0.875rem;
                        transition: all 0.2s ease;
                        background: white;
                    }

                    .form-input:focus,
                    .form-select:focus,
                    .form-textarea:focus {
                        outline: none;
                        border-color: #3b82f6;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }

                    .form-textarea {
                        resize: vertical;
                        min-height: 80px;
                    }

                    .form-help {
                        margin-top: 0.375rem;
                        font-size: 0.75rem;
                        color: #6b7280;
                    }

                    .field-error {
                        margin-top: 0.375rem;
                        font-size: 0.75rem;
                        color: #dc2626;
                        font-weight: 500;
                        display: none;
                    }

                    /* Confirm Modal Styles */
                    .confirm-modal .modal-content {
                        max-width: 400px;
                    }

                    .confirm-icon {
                        text-align: center;
                        margin-bottom: 1rem;
                    }

                    .confirm-icon i {
                        font-size: 3rem;
                        color: #f59e0b;
                    }

                    .confirm-modal .modal-body {
                        text-align: center;
                        padding: 2rem;
                    }

                    .confirm-modal .modal-body p {
                        font-size: 1rem;
                        color: #374151;
                        margin: 0;
                        line-height: 1.5;
                    }

                    /* Image Upload Styles */
                    .image-input-tabs {
                        display: flex;
                        margin-bottom: 1rem;
                        border-bottom: 2px solid #e5e7eb;
                    }

                    .tab-btn {
                        background: none;
                        border: none;
                        padding: 0.75rem 1.5rem;
                        cursor: pointer;
                        color: #6b7280;
                        font-weight: 500;
                        border-bottom: 2px solid transparent;
                        transition: all 0.2s ease;
                    }

                    .tab-btn:hover {
                        color: #4f46e5;
                        background-color: #f8fafc;
                    }

                    .tab-btn.active {
                        color: #4f46e5;
                        border-bottom-color: #4f46e5;
                        background-color: #f8fafc;
                    }

                    .image-input-section {
                        transition: all 0.3s ease;
                    }

                    .file-upload-area {
                        border: 2px dashed #d1d5db;
                        border-radius: 0.75rem;
                        padding: 2rem;
                        text-align: center;
                        cursor: pointer;
                        transition: all 0.2s ease;
                        background-color: #f9fafb;
                    }

                    .file-upload-area:hover {
                        border-color: #4f46e5;
                        background-color: #f0f9ff;
                    }

                    .file-upload-area:active {
                        transform: scale(0.98);
                    }

                    .file-upload-content i {
                        font-size: 2rem;
                        color: #9ca3af;
                        margin-bottom: 0.5rem;
                    }

                    .file-upload-text {
                        font-size: 1rem;
                        font-weight: 500;
                        color: #374151;
                        margin: 0.5rem 0;
                    }

                    .file-upload-hint {
                        font-size: 0.875rem;
                        color: #6b7280;
                        margin: 0;
                    }

                    .file-preview {
                        background: #f9fafb;
                        border: 1px solid #e5e7eb;
                        border-radius: 0.75rem;
                        padding: 1rem;
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                    }

                    .preview-img {
                        width: 4rem;
                        height: 4rem;
                        object-fit: cover;
                        border-radius: 0.5rem;
                        border: 1px solid #e5e7eb;
                    }

                    .file-info {
                        flex: 1;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                    }

                    .file-info span {
                        font-size: 0.875rem;
                        color: #374151;
                        font-weight: 500;
                    }

                    .remove-file-btn {
                        background: #fee2e2;
                        border: 1px solid #fecaca;
                        color: #dc2626;
                        border-radius: 50%;
                        width: 2rem;
                        height: 2rem;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        transition: all 0.2s ease;
                    }

                    .remove-file-btn:hover {
                        background: #fecaca;
                        transform: scale(1.1);
                    }

                    /* Animation Keyframes */
                    @keyframes fadeIn {
                        from {
                            opacity: 0;
                        }

                        to {
                            opacity: 1;
                        }
                    }

                    /* Responsive Design */
                    @media (max-width: 1024px) {
                        .admin-sidebar {
                            transform: translateX(-100%);
                            transition: transform 0.3s ease;
                        }

                        .admin-main {
                            margin-left: 0;
                        }

                        .products-grid {
                            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
                            gap: 1rem;
                            padding: 1rem;
                        }

                        .pagination-section {
                            flex-direction: column;
                            gap: 1rem;
                            text-align: center;
                        }

                        .pagination-nav {
                            justify-content: center;
                        }
                    }

                    @media (max-width: 768px) {
                        .header-content {
                            flex-direction: column;
                            align-items: stretch;
                            gap: 1.5rem;
                        }

                        .table-header {
                            flex-direction: column;
                            align-items: stretch;
                            gap: 1rem;
                        }

                        .filters-row {
                            grid-template-columns: 1fr;
                            gap: 1rem;
                        }

                        .advanced-filters-grid {
                            grid-template-columns: 1fr;
                        }

                        .products-grid {
                            grid-template-columns: 1fr;
                        }

                        .form-row {
                            grid-template-columns: 1fr;
                            gap: 1rem;
                        }

                        .modal-content {
                            width: 95%;
                            margin: 1rem;
                        }

                        .modal-header,
                        .modal-body,
                        .modal-footer {
                            padding: 1rem;
                        }

                        .pagination-btn {
                            padding: 0.5rem 0.75rem;
                            font-size: 0.8125rem;
                        }

                        .current-page {
                            padding: 0.5rem 0.75rem;
                            font-size: 0.8125rem;
                            min-width: 60px;
                        }
                    }

                    @media (max-width: 480px) {
                        .admin-content {
                            padding: 1rem;
                        }

                        .page-header {
                            padding: 1.5rem 1rem;
                        }

                        .filters-card,
                        .table-card {
                            border-radius: 0.5rem;
                        }

                        .filters-container,
                        .table-header {
                            padding: 1rem;
                        }

                        .notification-container {
                            right: 1rem;
                            left: 1rem;
                            max-width: none;
                        }
                    }

                    /* Empty State Styles */
                    .empty-state {
                        text-align: center;
                        padding: 4rem 2rem;
                        color: #6b7280;
                    }

                    .empty-state-icon {
                        font-size: 4rem;
                        margin-bottom: 1.5rem;
                        opacity: 0.6;
                    }

                    .empty-state h3 {
                        font-size: 1.5rem;
                        font-weight: 600;
                        margin: 0 0 0.5rem 0;
                        color: #374151;
                    }

                    .empty-state p {
                        font-size: 1rem;
                        margin: 0 0 2rem 0;
                        max-width: 400px;
                        margin-left: auto;
                        margin-right: auto;
                        line-height: 1.5;
                    }

                    .empty-state-actions {
                        display: flex;
                        justify-content: center;
                        gap: 1rem;
                        flex-wrap: wrap;
                    }

                    /* Bulk Actions Panel */
                    .bulk-actions-panel {
                        background: #f0f9ff;
                        border: 1px solid #0ea5e9;
                        border-radius: 0.5rem;
                        margin: 0 2rem 1rem 2rem;
                        animation: slideIn 0.3s ease-out;
                    }

                    .bulk-actions-content {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 1rem 1.5rem;
                        flex-wrap: wrap;
                        gap: 1rem;
                    }

                    .bulk-selected-count {
                        font-weight: 600;
                        color: #0c4a6e;
                        font-size: 0.875rem;
                    }

                    .bulk-actions-buttons {
                        display: flex;
                        gap: 0.5rem;
                        flex-wrap: wrap;
                    }

                    /* Table Responsive Enhancements */
                    .table-responsive {
                        overflow-x: auto;
                        margin: 0;
                    }

                    .data-table {
                        min-width: 800px;
                    }

                    /* Status and Stock Badges */
                    .status-badge,
                    .stock-badge {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.25rem;
                        padding: 0.25rem 0.5rem;
                        border-radius: 0.375rem;
                        font-size: 0.75rem;
                        font-weight: 600;
                        text-transform: uppercase;
                        letter-spacing: 0.025em;
                    }

                    .status-badge.active {
                        background: #dcfce7;
                        color: #166534;
                    }

                    .status-badge.inactive {
                        background: #fef2f2;
                        color: #991b1b;
                    }

                    .stock-badge.in-stock {
                        background: #dcfce7;
                        color: #166534;
                    }

                    .stock-badge.low-stock {
                        background: #fef3c7;
                        color: #92400e;
                    }

                    .stock-badge.out-of-stock {
                        background: #fef2f2;
                        color: #991b1b;
                    }

                    /* Product Info Styles */
                    .product-info {
                        display: flex;
                        align-items: center;
                        gap: 0.75rem;
                    }

                    .product-image {
                        width: 48px;
                        height: 48px;
                        border-radius: 0.5rem;
                        overflow: hidden;
                        background: #f3f4f6;
                        flex-shrink: 0;
                    }

                    .product-image img {
                        width: 100%;
                        height: 100%;
                        object-fit: cover;
                    }

                    .product-details {
                        flex: 1;
                        min-width: 0;
                    }

                    .product-name {
                        font-weight: 600;
                        color: #1f2937;
                        margin-bottom: 0.25rem;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                    }

                    .product-meta {
                        font-size: 0.75rem;
                        color: #6b7280;
                    }

                    .product-id {
                        font-family: 'Courier New', monospace;
                        background: #f3f4f6;
                        padding: 0.125rem 0.25rem;
                        border-radius: 0.25rem;
                    }

                    /* Category Tag */
                    .category-tag {
                        display: inline-flex;
                        align-items: center;
                        padding: 0.25rem 0.75rem;
                        background: #f3f4f6;
                        color: #374151;
                        border-radius: 1rem;
                        font-size: 0.75rem;
                        font-weight: 500;
                        text-transform: capitalize;
                    }

                    /* Price Display */
                    .price-display {
                        font-weight: 700;
                        font-size: 1rem;
                        color: #059669;
                    }

                    /* Stock Info */
                    .stock-info {
                        display: flex;
                        flex-direction: column;
                        gap: 0.25rem;
                        align-items: flex-start;
                    }

                    .stock-number {
                        font-weight: 600;
                        color: #1f2937;
                        font-size: 0.875rem;
                    }
                </style>
            </body>

            </html>