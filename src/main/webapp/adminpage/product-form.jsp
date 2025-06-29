<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>${product != null ? 'Edit Product' : 'Add New Product'} - PhoneHub Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

                <!-- Cloudinary Upload Widget -->
                <script src="https://upload-widget.cloudinary.com/global/all.js" type="text/javascript"></script>

                <!-- Include Cloudinary Configuration -->
                <%@ include file="../includes/cloudinary-config.jsp" %>
            </head>

            <body style="margin: 0; background: var(--background-color);">
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
                        <header
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                            <div>
                                <h1 style="color: var(--text-primary); margin-bottom: 0.5rem;">
                                    <i class="fas fa-${product != null ? 'edit' : 'plus'}"></i>
                                    ${product != null ? 'Edit Product' : 'Add New Product'}
                                </h1>
                                <p style="color: var(--text-secondary);">
                                    ${product != null ? 'Update product information' : 'Add a new phone to your
                                    inventory'}
                                </p>
                            </div>

                            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i>
                                Back to Products
                            </a>
                        </header>

                        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 2rem;">
                            <!-- Product Form -->
                            <section
                                style="background: var(--surface-color); padding: 2rem; border-radius: 1rem; box-shadow: var(--shadow);">
                                <h3 style="margin-bottom: 1.5rem; color: var(--text-primary);">
                                    <i class="fas fa-info-circle"></i>
                                    Product Information
                                </h3>

                                <form id="productForm" onsubmit="saveProduct(event)">
                                    <input type="hidden" id="productId" name="id"
                                        value="${product != null ? product.id : ''}">

                                    <div class="form-group">
                                        <label for="productName" class="form-label">Product Name *</label>
                                        <input type="text" id="productName" name="name" class="form-input"
                                            value="${product != null ? product.name : ''}"
                                            placeholder="e.g., iPhone 15 Pro Max" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="productDescription" class="form-label">Description</label>
                                        <textarea id="productDescription" name="description" class="form-textarea"
                                            rows="4"
                                            placeholder="Detailed product description...">${product != null ? product.description : ''}</textarea>
                                    </div>

                                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                        <div class="form-group">
                                            <label for="productPrice" class="form-label">Price ($) *</label>
                                            <input type="number" id="productPrice" name="price" class="form-input"
                                                value="${product != null ? product.price : ''}" step="0.01" min="0"
                                                placeholder="999.99" required>
                                        </div>

                                        <div class="form-group">
                                            <label for="productStock" class="form-label">Stock Quantity *</label>
                                            <input type="number" id="productStock" name="stock" class="form-input"
                                                value="${product != null ? product.stockQuantity : ''}" min="0"
                                                placeholder="50" required>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="productCategory" class="form-label">Category</label>
                                        <select id="productCategory" name="category" class="form-select">
                                            <option value="">Select Category</option>
                                            <option value="flagship" ${product !=null &&
                                                product.categoryName=='flagship' ? 'selected' : '' }>Flagship</option>
                                            <option value="midrange" ${product !=null &&
                                                product.categoryName=='midrange' ? 'selected' : '' }>Mid-Range</option>
                                            <option value="budget" ${product !=null && product.categoryName=='budget'
                                                ? 'selected' : '' }>Budget</option>
                                            <option value="gaming" ${product !=null && product.categoryName=='gaming'
                                                ? 'selected' : '' }>Gaming</option>
                                            <option value="business" ${product !=null &&
                                                product.categoryName=='business' ? 'selected' : '' }>Business</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="productImageUrl" class="form-label">Main Image URL</label>
                                        <div style="display: flex; gap: 0.5rem;">
                                            <input type="url" id="productImageUrl" name="imageUrl" class="form-input"
                                                value="${product != null ? product.mainImageUrl : ''}"
                                                placeholder="https://res.cloudinary.com/...">
                                            <button type="button" class="btn btn-secondary" onclick="uploadMainImage()">
                                                <i class="fas fa-cloud-upload-alt"></i>
                                                Upload
                                            </button>
                                        </div>
                                    </div>

                                    <!-- Product Specifications -->
                                    <div class="form-group">
                                        <label class="form-label">
                                            <i class="fas fa-cogs"></i>
                                            Product Specifications
                                        </label>
                                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                            <input type="text" class="form-input" placeholder="Brand (e.g., Apple)"
                                                name="brand">
                                            <input type="text" class="form-input" placeholder="Model (e.g., iPhone 15)"
                                                name="model">
                                            <input type="text" class="form-input" placeholder="Storage (e.g., 256GB)"
                                                name="storage">
                                            <input type="text" class="form-input" placeholder="RAM (e.g., 8GB)"
                                                name="ram">
                                            <input type="text" class="form-input"
                                                placeholder="Screen Size (e.g., 6.1 inch)" name="screenSize">
                                            <input type="text" class="form-input"
                                                placeholder="Color (e.g., Natural Titanium)" name="color">
                                        </div>
                                    </div>

                                    <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i>
                                            ${product != null ? 'Update Product' : 'Create Product'}
                                        </button>
                                        <button type="button" class="btn btn-secondary" onclick="resetForm()">
                                            <i class="fas fa-undo"></i>
                                            Reset
                                        </button>
                                    </div>
                                </form>
                            </section>

                            <!-- Image Preview & Additional Images -->
                            <section
                                style="background: var(--surface-color); padding: 2rem; border-radius: 1rem; box-shadow: var(--shadow);">
                                <h3 style="margin-bottom: 1.5rem; color: var(--text-primary);">
                                    <i class="fas fa-images"></i>
                                    Product Images
                                </h3>

                                <!-- Main Image Preview -->
                                <div class="form-group">
                                    <label class="form-label">Main Image Preview</label>
                                    <div id="mainImagePreview"
                                        style="border: 2px dashed var(--border-color); border-radius: 0.5rem; padding: 2rem; text-align: center; background: var(--background-color);">
                                        <c:choose>
                                            <c:when test="${product != null && not empty product.mainImageUrl}">
                                                <img src="${product.mainImageUrl}" alt="Product Preview"
                                                    style="max-width: 100%; max-height: 200px; border-radius: 0.5rem;">
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-image"
                                                    style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                                                <p style="color: var(--text-secondary); margin: 0;">No image selected
                                                </p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <!-- Additional Images -->
                                <div class="form-group">
                                    <label class="form-label">Additional Images</label>
                                    <div id="additionalImages"
                                        style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.5rem; margin-bottom: 1rem;">
                                        <!-- Additional images will be added here -->
                                    </div>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="uploadAdditionalImage()" style="width: 100%;">
                                        <i class="fas fa-plus"></i>
                                        Add More Images
                                    </button>
                                </div>

                                <!-- Upload Guidelines -->
                                <div
                                    style="background: var(--background-color); padding: 1rem; border-radius: 0.5rem; margin-top: 1.5rem;">
                                    <h4 style="margin-bottom: 0.5rem; color: var(--text-primary); font-size: 0.875rem;">
                                        <i class="fas fa-info-circle"></i>
                                        Image Guidelines
                                    </h4>
                                    <ul
                                        style="font-size: 0.75rem; color: var(--text-secondary); margin: 0; padding-left: 1rem;">
                                        <li>Recommended size: 800x800 pixels</li>
                                        <li>Format: JPG, PNG, WebP</li>
                                        <li>Max file size: 5MB</li>
                                        <li>Use high-quality, clear images</li>
                                        <li>Show product from multiple angles</li>
                                    </ul>
                                </div>
                            </section>
                        </div>
                    </main>
                </div>

                <script>
                    // Cloudinary uploaders
                    let productUploader;
                    let currentUploadTarget = 'main'; // 'main' or 'additional'

                    // Initialize Cloudinary uploaders when page loads
                    document.addEventListener('DOMContentLoaded', function () {
                        if (window.CloudinaryUploader) {
                            productUploader = window.CloudinaryUploader.createProductUploader(
                                handleImageUpload,
                                function (error) {
                                    console.error('Upload error:', error);
                                    showNotification('Failed to upload image', 'error');
                                }
                            );
                        } else {
                            console.error('Cloudinary configuration not loaded');
                        }
                    });

                    function uploadMainImage() {
                        currentUploadTarget = 'main';
                        if (productUploader) {
                            productUploader.open();
                        } else {
                            showNotification('Upload service not available', 'error');
                        }
                    }

                    function uploadAdditionalImage() {
                        currentUploadTarget = 'additional';
                        if (productUploader) {
                            productUploader.open();
                        } else {
                            showNotification('Upload service not available', 'error');
                        }
                    }

                    function handleImageUpload(imageInfo) {
                        console.log('Image uploaded:', imageInfo);
                        const imageUrl = imageInfo.secure_url;

                        if (currentUploadTarget === 'main') {
                            document.getElementById('productImageUrl').value = imageUrl;
                            updateMainImagePreview(imageUrl);
                            showNotification('Main image uploaded successfully!', 'success');
                        } else {
                            addAdditionalImage(imageUrl);
                            showNotification('Additional image added!', 'success');
                        }
                    }

                    function updateMainImagePreview(imageUrl) {
                        const preview = document.getElementById('mainImagePreview');
                        if (imageUrl) {
                            preview.innerHTML = `<img src="${imageUrl}" alt="Product Preview" style="max-width: 100%; max-height: 200px; border-radius: 0.5rem;">`;
                        } else {
                            preview.innerHTML = `
                    <i class="fas fa-image" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                    <p style="color: var(--text-secondary); margin: 0;">No image selected</p>
                `;
                        }
                    }

                    function addAdditionalImage(imageUrl) {
                        const container = document.getElementById('additionalImages');
                        const imageDiv = document.createElement('div');
                        imageDiv.style.cssText = 'position: relative; border-radius: 0.5rem; overflow: hidden;';
                        imageDiv.innerHTML = `
                <img src="${imageUrl}" alt="Additional Image" style="width: 100%; height: 80px; object-fit: cover;">
                <button type="button" onclick="removeAdditionalImage(this)" 
                        style="position: absolute; top: 0.25rem; right: 0.25rem; background: rgba(0,0,0,0.7); color: white; border: none; border-radius: 50%; width: 20px; height: 20px; cursor: pointer; display: flex; align-items: center; justify-content: center;">
                    <i class="fas fa-times" style="font-size: 0.75rem;"></i>
                </button>
                <input type="hidden" name="additionalImages" value="${imageUrl}">
            `;
                        container.appendChild(imageDiv);
                    }

                    function removeAdditionalImage(button) {
                        button.parentElement.remove();
                    }

                    // Update main image preview when URL changes
                    document.getElementById('productImageUrl').addEventListener('input', function (e) {
                        updateMainImagePreview(e.target.value);
                    });

                    // Form submission
                    function saveProduct(event) {
                        event.preventDefault();

                        const formData = new FormData(event.target);

                        // Show loading state
                        const submitBtn = event.target.querySelector('button[type="submit"]');
                        const originalText = submitBtn.innerHTML;
                        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Saving...';
                        submitBtn.disabled = true;

                        fetch('${pageContext.request.contextPath}/admin/product/save', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    showNotification('Product saved successfully!', 'success');
                                    setTimeout(() => {
                                        window.location.href = '${pageContext.request.contextPath}/admin/products';
                                    }, 1500);
                                } else {
                                    showNotification(data.message || 'Failed to save product', 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                showNotification('Error saving product', 'error');
                            })
                            .finally(() => {
                                submitBtn.innerHTML = originalText;
                                submitBtn.disabled = false;
                            });
                    }

                    function resetForm() {
                        if (confirm('Are you sure you want to reset the form? All changes will be lost.')) {
                            document.getElementById('productForm').reset();
                            updateMainImagePreview('');
                            document.getElementById('additionalImages').innerHTML = '';
                        }
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

                    // Auto-save draft functionality
                    let autoSaveTimer;
                    document.getElementById('productForm').addEventListener('input', function () {
                        clearTimeout(autoSaveTimer);
                        autoSaveTimer = setTimeout(() => {
                            saveDraft();
                        }, 5000); // Auto-save after 5 seconds of inactivity
                    });

                    function saveDraft() {
                        const formData = new FormData(document.getElementById('productForm'));
                        const draftData = {};
                        for (let [key, value] of formData.entries()) {
                            draftData[key] = value;
                        }
                        localStorage.setItem('productDraft', JSON.stringify(draftData));
                        console.log('Draft saved');
                    }

                    function loadDraft() {
                        const draft = localStorage.getItem('productDraft');
                        if (draft && !document.getElementById('productId').value) {
                            const draftData = JSON.parse(draft);
                            Object.keys(draftData).forEach(key => {
                                const element = document.querySelector(`[name="${key}"]`);
                                if (element) element.value = draftData[key];
                            });
                            updateMainImagePreview(draftData.imageUrl);
                        }
                    }

                    function clearDraft() {
                        localStorage.removeItem('productDraft');
                    }

                    // Load draft on page load
                    window.addEventListener('load', loadDraft);

                    // Clear draft when form is successfully submitted
                    window.addEventListener('beforeunload', function () {
                        if (document.getElementById('productId').value) {
                            clearDraft();
                        }
                    });
                </script>

                <style>
                    .form-group {
                        margin-bottom: 1.5rem;
                    }

                    .form-label {
                        display: block;
                        margin-bottom: 0.5rem;
                        font-weight: 600;
                        color: var(--text-primary);
                    }

                    .form-input,
                    .form-select,
                    .form-textarea {
                        width: 100%;
                        padding: 0.75rem;
                        border: 1px solid var(--border-color);
                        border-radius: 0.5rem;
                        font-size: 1rem;
                        transition: border-color 0.2s ease;
                    }

                    .form-input:focus,
                    .form-select:focus,
                    .form-textarea:focus {
                        outline: none;
                        border-color: var(--primary-color);
                        box-shadow: 0 0 0 3px rgba(var(--primary-color-rgb), 0.1);
                    }

                    .form-textarea {
                        resize: vertical;
                        min-height: 100px;
                    }

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
                </style>
            </body>

            </html>