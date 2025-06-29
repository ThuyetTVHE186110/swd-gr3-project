<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Category Management - PhoneHub Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            </head>

            <body class="admin-body">
                <!-- Admin Header -->
                <header class="admin-header">
                    <div class="admin-nav">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-logo">
                            <i class="fas fa-mobile-alt"></i> PhoneHub Admin
                        </a>
                        <nav class="admin-menu">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                            <a href="${pageContext.request.contextPath}/admin/products">Products</a>
                            <a href="${pageContext.request.contextPath}/admin/categories" class="active">Categories</a>
                            <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
                        </nav>
                    </div>
                </header>

                <main class="admin-main">
                    <div class="admin-container">
                        <!-- Header -->
                        <div class="admin-page-header">
                            <h1><i class="fas fa-tags"></i> Category Management</h1>
                            <button class="btn btn-primary" onclick="openCategoryModal()">
                                <i class="fas fa-plus"></i> Add Category
                            </button>
                        </div>

                        <!-- Categories Table -->
                        <div class="admin-card">
                            <div class="admin-card-header">
                                <h2>Categories</h2>
                            </div>
                            <div class="admin-table-container">
                                <table class="admin-table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Description</th>
                                            <th>Products</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="category" items="${categories}">
                                            <tr>
                                                <td>${category.id}</td>
                                                <td>
                                                    <div class="category-info">
                                                        <c:if test="${not empty category.imageUrl}">
                                                            <img src="${category.imageUrl}" alt="${category.name}"
                                                                class="category-thumb">
                                                        </c:if>
                                                        <span class="category-name">${category.name}</span>
                                                    </div>
                                                </td>
                                                <td class="category-description">${category.description}</td>
                                                <td>
                                                    <span class="badge badge-info">${category.products.size()}
                                                        products</span>
                                                </td>
                                                <td>
                                                    <span
                                                        class="badge ${category.active ? 'badge-success' : 'badge-secondary'}">
                                                        ${category.active ? 'Active' : 'Inactive'}
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <button class="btn btn-sm btn-outline"
                                                            onclick="editCategory(${category.id}, '${category.name}', '${category.description}')">
                                                            <i class="fas fa-edit"></i>
                                                        </button>
                                                        <button class="btn btn-sm btn-danger"
                                                            onclick="deleteCategory(${category.id}, '${category.name}')">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </main>

                <!-- Category Modal -->
                <div id="categoryModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 id="modalTitle">Add Category</h3>
                            <button class="modal-close" onclick="closeCategoryModal()">&times;</button>
                        </div>
                        <form id="categoryForm" method="post">
                            <div class="form-group">
                                <label for="categoryName">Category Name</label>
                                <input type="text" id="categoryName" name="name" required>
                            </div>
                            <div class="form-group">
                                <label for="categoryDescription">Description</label>
                                <textarea id="categoryDescription" name="description" rows="3"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                    onclick="closeCategoryModal()">Cancel</button>
                                <button type="submit" class="btn btn-primary">
                                    <span id="submitText">Add Category</span>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <script>
                    let editingCategoryId = null;

                    function openCategoryModal() {
                        editingCategoryId = null;
                        document.getElementById('modalTitle').textContent = 'Add Category';
                        document.getElementById('submitText').textContent = 'Add Category';
                        document.getElementById('categoryForm').reset();
                        document.getElementById('categoryModal').style.display = 'flex';
                    }

                    function editCategory(id, name, description) {
                        editingCategoryId = id;
                        document.getElementById('modalTitle').textContent = 'Edit Category';
                        document.getElementById('submitText').textContent = 'Update Category';
                        document.getElementById('categoryName').value = name;
                        document.getElementById('categoryDescription').value = description;
                        document.getElementById('categoryModal').style.display = 'flex';
                    }

                    function closeCategoryModal() {
                        document.getElementById('categoryModal').style.display = 'none';
                        editingCategoryId = null;
                    }

                    function deleteCategory(id, name) {
                        if (confirm(`Are you sure you want to delete category "${name}"?`)) {
                            fetch(`${window.location.origin}/api/categories/${id}`, {
                                method: 'DELETE'
                            })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        location.reload();
                                    } else {
                                        alert('Failed to delete category');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert('Failed to delete category');
                                });
                        }
                    }

                    // Handle form submission
                    document.getElementById('categoryForm').addEventListener('submit', function (e) {
                        e.preventDefault();

                        const formData = new FormData(this);
                        const url = editingCategoryId
                            ? `${window.location.origin}/api/categories/${editingCategoryId}`
                            : `${window.location.origin}/api/categories/`;

                        const method = editingCategoryId ? 'PUT' : 'POST';

                        fetch(url, {
                            method: method,
                            body: formData
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.category) {
                                    location.reload();
                                } else {
                                    alert('Failed to save category');
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('Failed to save category');
                            });
                    });

                    // Close modal on background click
                    document.getElementById('categoryModal').addEventListener('click', function (e) {
                        if (e.target === this) {
                            closeCategoryModal();
                        }
                    });
                </script>

                <style>
                    .category-info {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .category-thumb {
                        width: 32px;
                        height: 32px;
                        border-radius: 4px;
                        object-fit: cover;
                    }

                    .category-name {
                        font-weight: 500;
                    }

                    .category-description {
                        max-width: 200px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                    }

                    .badge {
                        padding: 0.25rem 0.5rem;
                        border-radius: 0.25rem;
                        font-size: 0.75rem;
                        font-weight: 500;
                    }

                    .badge-info {
                        background-color: #3b82f6;
                        color: white;
                    }

                    .badge-success {
                        background-color: #10b981;
                        color: white;
                    }

                    .badge-secondary {
                        background-color: #6b7280;
                        color: white;
                    }

                    .action-buttons {
                        display: flex;
                        gap: 0.5rem;
                    }

                    .modal {
                        display: none;
                        position: fixed;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        background-color: rgba(0, 0, 0, 0.5);
                        z-index: 1000;
                        align-items: center;
                        justify-content: center;
                    }

                    .modal-content {
                        background: white;
                        border-radius: 0.5rem;
                        width: 90%;
                        max-width: 500px;
                        max-height: 90vh;
                        overflow: auto;
                    }

                    .modal-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 1rem;
                        border-bottom: 1px solid #e5e7eb;
                    }

                    .modal-close {
                        background: none;
                        border: none;
                        font-size: 1.5rem;
                        cursor: pointer;
                        color: #6b7280;
                    }

                    .modal-footer {
                        display: flex;
                        gap: 0.5rem;
                        justify-content: flex-end;
                        padding: 1rem;
                        border-top: 1px solid #e5e7eb;
                    }

                    .form-group {
                        padding: 0 1rem;
                        margin-bottom: 1rem;
                    }

                    .form-group label {
                        display: block;
                        margin-bottom: 0.5rem;
                        font-weight: 500;
                    }

                    .form-group input,
                    .form-group textarea {
                        width: 100%;
                        padding: 0.5rem;
                        border: 1px solid #d1d5db;
                        border-radius: 0.25rem;
                        font-size: 0.875rem;
                    }

                    .form-group input:focus,
                    .form-group textarea:focus {
                        outline: none;
                        border-color: #3b82f6;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }
                </style>
            </body>

            </html>